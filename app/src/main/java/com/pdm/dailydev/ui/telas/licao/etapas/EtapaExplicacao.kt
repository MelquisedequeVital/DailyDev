package com.pdm.dailydev.ui.telas.licao.etapas

import com.pdm.dailydev.ui.telas.licao.LicaoUiState
import com.pdm.dailydev.ui.telas.licao.LicaoViewModel

class EtapaExplicacao : EtapaLicao {
    override fun toUiState(vm: LicaoViewModel): LicaoUiState {
        return LicaoUiState.Explicacao(
            texto = vm.licao.textoExplicacao,
            totalEtapas = vm.totalEtapas
        )
    }

    override fun avancar(vm: LicaoViewModel, onConcluir: (List<Int>) -> Unit) {
        vm.mudarEtapa(EtapaQuestao(indice = 0))
    }

    override fun voltar(vm: LicaoViewModel, onSair: () -> Unit) {
        onSair()
    }
}