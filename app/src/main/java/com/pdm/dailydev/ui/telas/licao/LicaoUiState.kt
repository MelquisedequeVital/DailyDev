package com.pdm.dailydev.ui.telas.licao

import com.pdm.dailydev.model.Questao

sealed interface LicaoUiState {
    val etapaAtual: Int
    val totalEtapas: Int
    val textoBotao: String
    val podeAvancar: Boolean

    data class Explicacao(
        val texto: String,
        override val totalEtapas: Int
    ) : LicaoUiState {
        override val etapaAtual = 1
        override val textoBotao = "Começar questões"
        override val podeAvancar = true
    }

    data class QuestaoAtual(
        val indice: Int,
        val questao: Questao,
        val alternativaSelecionada: Int? = null,
        override val totalEtapas: Int,
        val isUltimaQuestao: Boolean
    ) : LicaoUiState {
        override val etapaAtual = indice + 2
        override val textoBotao = if (isUltimaQuestao) "Finalizar" else "Próxima"
        override val podeAvancar = alternativaSelecionada != null
    }

    data class Resultado(
        val acertos: Int,
        val totalQuestoes: Int,
        override val totalEtapas: Int
    ) : LicaoUiState {
        override val etapaAtual = totalEtapas
        override val textoBotao = "Próxima lição"
        override val podeAvancar = true
    }
}