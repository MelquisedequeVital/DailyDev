package com.pdm.dailydev.model
import java.util.UUID

data class Licao(
    val id: UUID = UUID.randomUUID(),
    val titulo: String,
    val textoExplicacao: String,
    val questoes: List<Questao>,
    val assunto: Assunto,
    val dificuldade: Int // 1 a 10
) {
    init {
        require(dificuldade in 1..10) {
            "A dificuldade deve estar entre 1 e 10."
        }
        require(questoes.isNotEmpty()) {
            "A lição deve ter pelo menos uma questão."
        }
    }

    fun calcularAcertos(respostasEscolhidas: List<Int>): Int {
        return questoes.zip(respostasEscolhidas).count { (questao, resposta) ->
            questao.verificarResposta(resposta)
        }
    }
}