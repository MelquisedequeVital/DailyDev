package com.pdm.dailydev.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdm.dailydev.data.LicaoRepository
import com.pdm.dailydev.data.LicaoRepositoryLocal
import com.pdm.dailydev.model.Licao
import com.pdm.dailydev.model.Usuario
import com.pdm.dailydev.recomendacao.MotorRecomendacao
import com.pdm.dailydev.recomendacao.RecomendadorSimples
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel(
    private val repository: LicaoRepository = LicaoRepositoryLocal(),
    private val motor: MotorRecomendacao = RecomendadorSimples()
) : ViewModel() {

    val usuario = Usuario(
        nome = "Dev",
        email = "aluno@dailydev.com",
        senhaHash = "123"
    )

    private val _catalogoLicoes = MutableStateFlow<List<Licao>>(emptyList())
    val catalogoLicoes: StateFlow<List<Licao>> = _catalogoLicoes.asStateFlow()

    private val _screenState = MutableStateFlow<AppScreenState>(AppScreenState.Principal)
    val screenState: StateFlow<AppScreenState> = _screenState.asStateFlow()

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

    fun iniciarLicao() {
        val licao = _proximaLicao.value ?: return
        _screenState.value = AppScreenState.EmLicao(licao)
    }

    fun voltarParaPrincipal() {
        _screenState.value = AppScreenState.Principal
    }

    fun concluirLicao(
        licaoConcluida: Licao,
        respostasEscolhidas: List<Int>,
        irParaProxima: Boolean = true
    ) {
        usuario.registrarTentativa(licaoConcluida, respostasEscolhidas)

        val recomendada = motor.recomendarProxima(usuario, _catalogoLicoes.value)
        _proximaLicao.value = recomendada

        _screenState.value = if (irParaProxima && recomendada != null) {
            AppScreenState.EmLicao(recomendada)
        } else {
            AppScreenState.Principal
        }
    }
}