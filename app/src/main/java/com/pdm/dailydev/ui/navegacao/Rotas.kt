package com.pdm.dailydev.ui.navegacao

import java.util.UUID

object Rotas {
    const val PRINCIPAL = "principal"
    const val LICOES = "licoes"
    const val MENU = "menu"

    const val ARG_LICAO_ID = "licaoId"
    const val LICAO = "licao/{$ARG_LICAO_ID}"

    fun licao(licaoId: UUID) = "licao/$licaoId"
}
