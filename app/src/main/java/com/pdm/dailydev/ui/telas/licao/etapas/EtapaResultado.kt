package com.pdm.dailydev.ui.telas.licao.etapas

import com.pdm.dailydev.ui.telas.licao.LicaoUiState
import com.pdm.dailydev.ui.telas.licao.LicaoViewModel


class EtapaResultado : EtapaLicao {
    override fun toUiState(vm: LicaoViewModel): LicaoUiState {
        val acertos = vm.licao.calcularAcertos(vm.obterRespostasOrdenadas())
        return LicaoUiState.Resultado(
            acertos = acertos,
            totalQuestoes = vm.licao.questoes.size,
            totalEtapas = vm.totalEtapas
        )
    }

    override fun avancar(vm: LicaoViewModel, onConcluir: (List<Int>) -> Unit) {
        onConcluir(vm.obterRespostasOrdenadas())
    }

    override fun voltar(vm: LicaoViewModel, onSair: () -> Unit) {
        onSair()
    }
}