package com.pdm.dailydev.ui

import com.pdm.dailydev.model.Licao
import java.util.UUID

sealed interface AppScreenState {
    data object Principal : AppScreenState
    data class EmLicao(
        val licao: Licao,
        val sessaoId: UUID = UUID.randomUUID()
    ) : AppScreenState
}