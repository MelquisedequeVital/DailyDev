package com.pdm.dailydev.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pdm.dailydev.ui.telas.TelaPrincipal
import com.pdm.dailydev.ui.telas.licao.LicaoScreen

@Composable
fun DailyDevApp(
    appViewModel: AppViewModel = viewModel()
) {
    val screenState by appViewModel.screenState.collectAsState()
    val proximaLicao by appViewModel.proximaLicao.collectAsState()
    val catalogoLicoes by appViewModel.catalogoLicoes.collectAsState()

    when (val estado = screenState) {
        is AppScreenState.Principal -> {
            TelaPrincipal(
                usuario = appViewModel.usuario,
                catalogoLicoes = catalogoLicoes,
                proximaLicao = proximaLicao,
                onIniciarClick = { appViewModel.iniciarLicao() }
            )
        }

        is AppScreenState.EmLicao -> {
            LicaoScreen(
                licao = estado.licao,
                onVoltarClick = { appViewModel.voltarParaPrincipal() },
                onConcluirLicao = { respostas, irParaProxima ->
                    appViewModel.concluirLicao(estado.licao, respostas, irParaProxima)
                }
            )
        }
    }
}