package com.pdm.dailydev.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pdm.dailydev.ui.navegacao.Rotas
import com.pdm.dailydev.ui.telas.LicaoScreen
import com.pdm.dailydev.ui.telas.TelaLicoes
import com.pdm.dailydev.ui.telas.TelaMenu
import com.pdm.dailydev.ui.telas.TelaPrincipal
import com.pdm.dailydev.ui.theme.DailyDevColors
import java.util.UUID
import com.pdm.dailydev.ui.telas.GithubScreen

private data class Aba(val rota: String, val titulo: String, val icone: ImageVector)

private val abas = listOf(
    Aba(Rotas.PRINCIPAL, "Início", Icons.Filled.Home),
    Aba(Rotas.LICOES, "Lições", Icons.AutoMirrored.Filled.MenuBook),
    Aba(Rotas.MENU, "Menu", Icons.Filled.Person)
)

@Composable
fun DailyDevApp(
    appViewModel: AppViewModel = viewModel()
) {
    val navController = rememberNavController()

    val proximaLicao by appViewModel.proximaLicao.collectAsState()
    val catalogoLicoes by appViewModel.catalogoLicoes.collectAsState()
    val carregando by appViewModel.carregando.collectAsState()
    val erro by appViewModel.erro.collectAsState()
    val filtro by appViewModel.filtro.collectAsState()

    // Rota no topo da pilha: marca a aba selecionada e esconde a barra durante a lição
    val rotaAtual = navController.currentBackStackEntryAsState().value?.destination?.route
    val mostrarBarra = abas.any { it.rota == rotaAtual }

    Scaffold(
        containerColor = DailyDevColors.Background,
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            if (mostrarBarra) {
                NavigationBar(containerColor = DailyDevColors.SurfaceCard) {
                    abas.forEach { aba ->
                        NavigationBarItem(
                            selected = rotaAtual == aba.rota,
                            onClick = {
                                navController.navigate(aba.rota) {
                                    popUpTo(Rotas.PRINCIPAL) // pilha não cresce a cada troca de aba
                                    launchSingleTop = true   // não empilha a mesma aba duas vezes
                                }
                            },
                            icon = { Icon(aba.icone, contentDescription = aba.titulo) },
                            label = { Text(aba.titulo) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Rotas.PRINCIPAL,
            // consumeWindowInsets: telas internas não somam de novo a altura da barra do sistema
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            composable(Rotas.PRINCIPAL) {
                TelaPrincipal(
                    usuario = appViewModel.usuario,
                    catalogoLicoes = catalogoLicoes,
                    proximaLicao = proximaLicao,
                    onIniciarClick = {
                        proximaLicao?.let { navController.navigate(Rotas.licao(it.id)) }
                    },
                    carregando = carregando,
                    erro = erro,
                    onTentarNovamenteClick = { appViewModel.carregarLicoes() }
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

            composable(Rotas.LICOES) {
                TelaLicoes(
                    catalogoLicoes = catalogoLicoes,
                    historico = appViewModel.usuario.historico,
                    filtro = filtro,
                    onFiltroChange = { appViewModel.atualizarFiltro(it) },
                    onLicaoClick = { licao -> navController.navigate(Rotas.licao(licao.id)) }
                )
            }

            composable(Rotas.MENU) {
                TelaMenu(
                    usuario = appViewModel.usuario,
                    onGithubClick = {
                        navController.navigate(Rotas.GITHUB)
                    }
                )
            }

            composable(route = Rotas.GITHUB) {
                GithubScreen()
            }
        }
    }
}
