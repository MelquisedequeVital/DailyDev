package com.pdm.dailydev.model

import java.util.UUID

data class Tentativa(
    val id: UUID = UUID.randomUUID(),
    val usuarioId: UUID,
    val licaoId: UUID,
    val acertos: Int,
    val totalQuestoes: Int,
    val dataHoraMillis: Long = System.currentTimeMillis()
) {
    init {
        require(totalQuestoes > 0) {
            "O total de questões deve ser maior que zero."
        }
        require(acertos in 0..totalQuestoes) {
            "Os acertos devem estar entre 0 e $totalQuestoes."
        }
    }

    // Devolve um valor de 0.0 (0%) a 1.0 (100%) — ouro para cálculos matemáticos e IA
    val aproveitamento: Double
        get() = acertos.toDouble() / totalQuestoes

    // Mantém compatibilidade fácil se você quiser saber se ele gabaritou ou atingiu a média
    val aprovado: Boolean
        get() = aproveitamento >= 0.5 // Ex: acertou pelo menos 1 de 2 (50%)
}