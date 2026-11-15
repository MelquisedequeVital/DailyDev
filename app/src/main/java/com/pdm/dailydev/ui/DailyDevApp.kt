package com.pdm.dailydev.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pdm.dailydev.ui.telas.TelaPrincipal
import com.pdm.dailydev.ui.telas.LicaoScreen

@Composable
fun DailyDevApp(
    appViewModel: AppViewModel = viewModel()
) {
    val screenState by appViewModel.screenState.collectAsState()
    val proximaLicao by appViewModel.proximaLicao.collectAsState()
    val catalogoLicoes by appViewModel.catalogoLicoes.collectAsState()
    val carregando by appViewModel.carregando.collectAsState()
    val erro by appViewModel.erro.collectAsState()

    when (val estado = screenState) {
        is AppScreenState.Principal -> {
            TelaPrincipal(
                usuario = appViewModel.usuario,
                catalogoLicoes = catalogoLicoes,
                proximaLicao = proximaLicao,
                onIniciarClick = { appViewModel.iniciarLicao() },
                carregando = carregando,
                erro = erro,
                onTentarNovamenteClick = { appViewModel.carregarLicoes() }
            )
        }

        is AppScreenState.EmLicao -> {
            LicaoScreen(
                licao = estado.licao,
                sessaoId = estado.sessaoId,
                onVoltarClick = { appViewModel.voltarParaPrincipal() },
                onConcluirLicao = { respostas, irParaProxima ->
                    appViewModel.concluirLicao(estado.licao, respostas, irParaProxima)
                }
            )
        }
    }
}