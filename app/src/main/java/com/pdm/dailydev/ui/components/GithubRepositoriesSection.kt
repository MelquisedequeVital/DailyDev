
package com.pdm.dailydev.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.pdm.dailydev.data.repository.GithubRepository
import com.pdm.dailydev.model.GithubRepo
import com.pdm.dailydev.ui.theme.DailyDevColors
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException

@Composable
fun GithubRepositoriesSection(
    modifier: Modifier = Modifier
) {

    val repositorios = remember {
        mutableStateOf<List<GithubRepo>>(emptyList())
    }

    val carregando = remember {
        mutableStateOf(true)
    }

    val mensagemErro = remember {
        mutableStateOf<String?>(null)
    }

    var tentativa by remember {
        mutableIntStateOf(0)
    }

    val repository = remember {
        GithubRepository()
    }

    val uriHandler = LocalUriHandler.current

    LaunchedEffect(tentativa) {
        carregando.value = true
        mensagemErro.value = null

        try {
            val resultado = repository.searchRepositories("language:java")

            resultado.onSuccess {
                repositorios.value = it
            }

            resultado.onFailure { erro ->
                mensagemErro.value = when (erro) {
                    is IOException ->
                        "Verifique sua conexão com a internet."

                    is HttpException ->
                        "Erro HTTP: ${erro.code()}"

                    else ->
                        "Não foi possível carregar os repositórios."
                }
            }

        } catch (e: CancellationException) {
            throw e

        } catch (e: Exception) {
            mensagemErro.value =
                "Ocorreu um erro inesperado ao carregar os repositórios."

        } finally {
            carregando.value = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(DailyDevColors.Background)
            .padding(horizontal = 16.dp)
    ) {

        Text(
            text = "Repositórios Java",
            style = MaterialTheme.typography.headlineSmall,
            color = DailyDevColors.TextPrimary
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (carregando.value) {

            CircularProgressIndicator(
                color = DailyDevColors.PrimaryBlue
            )

        } else if (mensagemErro.value != null) {

            Text(
                text = mensagemErro.value!!,
                color = DailyDevColors.TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { tentativa++ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = DailyDevColors.PrimaryBlue
                )
            ) {
                Text("Tentar novamente")
            }

        } else if (repositorios.value.isEmpty()) {

            Text(
                text = "Nenhum repositório encontrado.",
                color = DailyDevColors.TextSecondary
            )

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {

                items(
                    items = repositorios.value,
                    key = { it.fullName }
                ) { repo ->

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                uriHandler.openUri(repo.htmlUrl)
                            },
                        color = DailyDevColors.SurfaceCard,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(
                            1.dp,
                            DailyDevColors.BorderDefault
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = repo.fullName,
                                style = MaterialTheme.typography.titleMedium,
                                color = DailyDevColors.TextPrimary
                            )

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )

                            Text(
                                text = repo.description ?: "Sem descrição",
                                style = MaterialTheme.typography.bodyMedium,
                                color = DailyDevColors.TextSecondary
                            )

                            Spacer(
                                modifier = Modifier.height(10.dp)
                            )

                            Text(
                                text = "⭐ ${repo.stars} estrelas",
                                color = DailyDevColors.AccentGold,
                                style = MaterialTheme.typography.labelLarge
                            )

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )

                            Text(
                                text = "Linguagem: ${repo.language ?: "Não informada"}",
                                color = DailyDevColors.TextSecondary,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}
