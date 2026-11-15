package com.pdm.dailydev.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pdm.dailydev.ui.navegacao.Rotas
import com.pdm.dailydev.ui.telas.LicaoScreen
import com.pdm.dailydev.ui.telas.TelaPrincipal
import java.util.UUID

@Composable
fun DailyDevApp(
    appViewModel: AppViewModel = viewModel()
) {
    val navController = rememberNavController()

    val proximaLicao by appViewModel.proximaLicao.collectAsState()
    val catalogoLicoes by appViewModel.catalogoLicoes.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Rotas.PRINCIPAL
    ) {
        composable(Rotas.PRINCIPAL) {
            TelaPrincipal(
                usuario = appViewModel.usuario,
                catalogoLicoes = catalogoLicoes,
                proximaLicao = proximaLicao,
                onIniciarClick = {
                    proximaLicao?.let { navController.navigate(Rotas.licao(it.id)) }
                }
            )
        }

        composable(
            route = Rotas.LICAO,
            arguments = listOf(navArgument(Rotas.ARG_LICAO_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val licaoId = backStackEntry.arguments
                ?.getString(Rotas.ARG_LICAO_ID)
                ?.let(UUID::fromString)
            val licao = catalogoLicoes.find { it.id == licaoId }

            if (licao == null) {
                // ID não existe mais no catálogo: volta para a principal
                LaunchedEffect(Unit) { navController.popBackStack(Rotas.PRINCIPAL, inclusive = false) }
                return@composable
            }

            LicaoScreen(
                licao = licao,
                onVoltarClick = { navController.popBackStack() },
                onConcluirLicao = { respostas, irParaProxima ->
                    // Ignora toques enquanto a tela está saindo (animação), evitando registro duplicado
                    if (!backStackEntry.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                        return@LicaoScreen
                    }

                    val recomendada = appViewModel.concluirLicao(licao, respostas)

                    if (irParaProxima && recomendada != null) {
                        navController.navigate(Rotas.licao(recomendada.id)) {
                            popUpTo(Rotas.PRINCIPAL)
                        }
                    } else {
                        navController.popBackStack(Rotas.PRINCIPAL, inclusive = false)
                    }
                }
            )
        }
    }
}
