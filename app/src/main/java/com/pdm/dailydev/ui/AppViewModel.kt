package com.pdm.dailydev.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdm.dailydev.data.LicaoRepository
import com.pdm.dailydev.data.LicaoRepositoryLocal
import com.pdm.dailydev.model.Licao
import com.pdm.dailydev.model.Usuario
import com.pdm.dailydev.recomendacao.MotorRecomendacao
import com.pdm.dailydev.recomendacao.RecomendadorAdaptativo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel(
    private val repository: LicaoRepository = LicaoRepositoryLocal(),
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

    init {
        carregarLicoes()
    }

    private fun carregarLicoes() {
        viewModelScope.launch {
            val licoes = repository.obterTodasLicoes()
            _catalogoLicoes.value = licoes
            _proximaLicao.value = motor.recomendarProxima(usuario, licoes)
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
}