package com.pdm.dailydev.model

data class Questao(
    val enunciado: String,
    val alternativas: List<String>,
    val indiceCorreto: Int
) {
    init {
        require(alternativas.size >= 2) {
            "A questão deve ter pelo menos 2 alternativas."
        }
        require(indiceCorreto in alternativas.indices) {
            "O índice correto deve corresponder a uma alternativa válida."
        }
    }

    fun verificarResposta(indiceEscolhido: Int): Boolean {
        return indiceEscolhido == indiceCorreto
    }
}