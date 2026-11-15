package com.pdm.dailydev.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdm.dailydev.data.LicaoRepository
import com.pdm.dailydev.data.LicaoRepositoryFirestore
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
                _catalogoLicoes.value = licoes
                _proximaLicao.value = motor.recomendarProxima(usuario, licoes)
                if (licoes.isEmpty()) {
                    _erro.value = "Não foi possível carregar as lições. Verifique sua conexão."
                }
            } catch (e: Exception) {
                // sem o try/catch, um erro do banco (ex.: sem permissão) fecharia o app
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
        usuario.registrarTentativa(licaoConcluida, respostasEscolhidas)

        val recomendada = motor.recomendarProxima(usuario, _catalogoLicoes.value)
        _proximaLicao.value = recomendada
        return recomendada
    }

    fun atualizarFiltro(novo: FiltroLicoes) {
        _filtro.value = novo
    }
}