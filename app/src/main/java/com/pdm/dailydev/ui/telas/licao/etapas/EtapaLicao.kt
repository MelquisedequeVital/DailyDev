package com.pdm.dailydev.ui.telas.licao.etapas

import com.pdm.dailydev.ui.telas.licao.LicaoUiState
import com.pdm.dailydev.ui.telas.licao.LicaoViewModel


interface EtapaLicao {
    fun toUiState(vm: LicaoViewModel): LicaoUiState
    fun avancar(vm: LicaoViewModel, onConcluir: (List<Int>) -> Unit)
    fun voltar(vm: LicaoViewModel, onSair: () -> Unit)
}