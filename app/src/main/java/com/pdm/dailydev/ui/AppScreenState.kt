package com.pdm.dailydev.ui

import com.pdm.dailydev.model.Licao

sealed interface AppScreenState {
    data object Principal : AppScreenState
    data class EmLicao(val licao: Licao) : AppScreenState
}