package com.pdm.dailydev.ui.telas.licao

import androidx.lifecycle.ViewModel
import com.pdm.dailydev.model.Licao
import com.pdm.dailydev.ui.telas.licao.etapas.EtapaExplicacao
import com.pdm.dailydev.ui.telas.licao.etapas.EtapaLicao

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LicaoViewModel(val licao: Licao) : ViewModel() {

    val totalEtapas: Int = licao.questoes.size + 2

    private var etapaAtual: EtapaLicao = EtapaExplicacao()
    private val respostasMap = mutableMapOf<Int, Int>()

    private val _uiState = MutableStateFlow(etapaAtual.toUiState(this))
    val uiState: StateFlow<LicaoUiState> = _uiState.asStateFlow()

    fun mudarEtapa(novaEtapa: EtapaLicao) {
        etapaAtual = novaEtapa
        _uiState.value = etapaAtual.toUiState(this)
    }

    fun selecionarAlternativa(indiceAlternativa: Int) {
        val atual = _uiState.value as? LicaoUiState.QuestaoAtual ?: return
        respostasMap[atual.indice] = indiceAlternativa
        _uiState.value = etapaAtual.toUiState(this)
    }

    fun avancar(onConcluirLicao: (List<Int>) -> Unit) {
        etapaAtual.avancar(this, onConcluirLicao)
    }

    fun voltar(onSairDaLicao: () -> Unit) {
        etapaAtual.voltar(this, onSairDaLicao)
    }

    fun obterResposta(indiceQuestao: Int): Int? = respostasMap[indiceQuestao]

    fun obterRespostasOrdenadas(): List<Int> =
        licao.questoes.indices.map { respostasMap[it] ?: -1 }
}