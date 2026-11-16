package com.pdm.dailydev.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdm.dailydev.data.LicaoRepository
import com.pdm.dailydev.data.LicaoRepositoryFirestore
import com.pdm.dailydev.data.ProgressoRepository
import com.pdm.dailydev.data.ProgressoRepositoryFirestore
import com.pdm.dailydev.filtro.FiltroLicoes
import com.pdm.dailydev.model.Licao
import com.pdm.dailydev.model.Usuario
import com.pdm.dailydev.recomendacao.MotorRecomendacao
import com.pdm.dailydev.recomendacao.RecomendadorAdaptativo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel(
    private val repository: LicaoRepository = LicaoRepositoryFirestore(),
    private val progressoRepository: ProgressoRepository = ProgressoRepositoryFirestore(),
    private val motor: MotorRecomendacao = RecomendadorAdaptativo()
) : ViewModel() {

    val usuario = Usuario(
        nome = "Dev",
        email = "aluno@dailydev.com",
        senhaHash = "123"
    )

    private val _catalogoLicoes = MutableStateFlow<List<Licao>>(emptyList())
    val catalogoLicoes: StateFlow<List<Licao>> = _catalogoLicoes.asStateFlow()

    private val _proximaLicao = MutableStateFlow<Licao?>(null)
    val proximaLicao: StateFlow<Licao?> = _proximaLicao.asStateFlow()

    // true enquanto busca as lições no banco
    private val _carregando = MutableStateFlow(false)
    val carregando: StateFlow<Boolean> = _carregando.asStateFlow()

    // mensagem para o usuário quando não foi possível carregar (null = tudo certo)
    private val _erro = MutableStateFlow<String?>(null)
    val erro: StateFlow<String?> = _erro.asStateFlow()

    // Fica aqui (e não na tela) para não se perder ao trocar de aba
    private val _filtro = MutableStateFlow(FiltroLicoes())
    val filtro: StateFlow<FiltroLicoes> = _filtro.asStateFlow()

    init {
        carregarLicoes()
    }

    fun carregarLicoes() {
        viewModelScope.launch {
            _carregando.value = true
            _erro.value = null
            try {
                val licoes = repository.obterTodasLicoes()
                // Histórico antes de recomendar: a recomendação depende do que o aluno já fez
                val historico = progressoRepository.carregarHistorico(usuario.id)
                usuario.carregarHistorico(historico)
                // Se o aluno nunca escolheu um apelido, continua "Dev"
                progressoRepository.carregarApelido()?.let { usuario.nome = it }
                _catalogoLicoes.value = licoes
                _proximaLicao.value = motor.recomendarProxima(usuario, licoes)
                if (licoes.isEmpty()) {
                    _erro.value = "Não foi possível carregar as lições. Verifique sua conexão."
                }
            } catch (e: Exception) {
                // sem o try/catch, um erro do banco (ex.: sem permissão) fecharia o app
                Log.w("AppViewModel", "Erro ao carregar lições/progresso", e)
                _erro.value = "Erro ao carregar as lições. Verifique sua conexão."
            } finally {
                _carregando.value = false
            }
        }
    }

    /** Registra a tentativa e retorna a próxima lição recomendada (ou null se acabaram). */
    fun concluirLicao(
        licaoConcluida: Licao,
        respostasEscolhidas: List<Int>
    ): Licao? {
        val tentativa = usuario.registrarTentativa(licaoConcluida, respostasEscolhidas)

        // Salva no banco em segundo plano; a tela não precisa esperar
        viewModelScope.launch {
            try {
                progressoRepository.salvarTentativa(tentativa)
            } catch (e: Exception) {
                Log.w("AppViewModel", "Não foi possível salvar a tentativa", e)
            }
        }

        val recomendada = motor.recomendarProxima(usuario, _catalogoLicoes.value)
        _proximaLicao.value = recomendada
        return recomendada
    }

    /** Troca o apelido na tela na hora e salva no banco em segundo plano. */
    fun atualizarApelido(novo: String) {
        val apelido = novo.trim().take(20)
        if (apelido.isEmpty()) return

        usuario.nome = apelido

        viewModelScope.launch {
            try {
                progressoRepository.salvarApelido(apelido)
            } catch (e: Exception) {
                Log.w("AppViewModel", "Não foi possível salvar o apelido", e)
            }
        }
    }

    /**
     * Apaga a conta no banco e zera o aluno na tela.
     * aoTerminar(true) = deu certo; aoTerminar(false) = falhou (ex.: erro do banco).
     */
    fun excluirConta(aoTerminar: (sucesso: Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                progressoRepository.excluirConta()
                usuario.carregarHistorico(emptyList())
                usuario.nome = "Dev"
                _proximaLicao.value = motor.recomendarProxima(usuario, _catalogoLicoes.value)
                aoTerminar(true)
            } catch (e: Exception) {
                Log.w("AppViewModel", "Não foi possível excluir a conta", e)
                aoTerminar(false)
            }
        }
    }

    fun atualizarFiltro(novo: FiltroLicoes) {
        _filtro.value = novo
    }
}