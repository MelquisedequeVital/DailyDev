package com.pdm.dailydev.ui.telas.licao.etapas

import com.pdm.dailydev.ui.telas.licao.LicaoUiState
import com.pdm.dailydev.ui.telas.licao.LicaoViewModel

class EtapaQuestao(private val indice: Int) : EtapaLicao {
    override fun toUiState(vm: LicaoViewModel): LicaoUiState {
        return LicaoUiState.QuestaoAtual(
            indice = indice,
            questao = vm.licao.questoes[indice],
            alternativaSelecionada = vm.obterResposta(indice),
            totalEtapas = vm.totalEtapas,
            isUltimaQuestao = indice == vm.licao.questoes.lastIndex
        )
    }

    override fun avancar(vm: LicaoViewModel, onConcluir: (List<Int>) -> Unit) {
        val proximoIndice = indice + 1
        if (proximoIndice < vm.licao.questoes.size) {
            vm.mudarEtapa(EtapaQuestao(proximoIndice))
        } else {
            vm.mudarEtapa(EtapaResultado())
        }
    }

    override fun voltar(vm: LicaoViewModel, onSair: () -> Unit) {
        if (indice == 0) {
            vm.mudarEtapa(EtapaExplicacao())
        } else {
            vm.mudarEtapa(EtapaQuestao(indice - 1))
        }
    }
}