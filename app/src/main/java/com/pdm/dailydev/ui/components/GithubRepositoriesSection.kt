
package com.pdm.dailydev.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.pdm.dailydev.data.repository.GithubRepository
import com.pdm.dailydev.model.GithubRepo
import kotlinx.coroutines.CancellationException
import java.io.IOException
import retrofit2.HttpException

@Composable
fun GithubRepositoriesSection() {

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
            val resultado = repository.searchRepositories("language:kotlin")

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
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Repositórios Kotlin",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (carregando.value) {

            CircularProgressIndicator()

        } else if (mensagemErro.value != null) {

            Text(text = mensagemErro.value!!)

            Button(
                onClick = { tentativa++ }
            ) {
                Text("Tentar novamente")
            }

        } else if (repositorios.value.isEmpty()) {

            Text("Nenhum repositório encontrado.")

        } else {

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(
                    items = repositorios.value,
                    key = { it.fullName }
                ) { repo ->

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                uriHandler.openUri(repo.htmlUrl)
                            }
                            .padding(vertical = 12.dp)
                    ) {
                        Text(
                            text = repo.fullName,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = repo.description ?: "Sem descrição"
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "⭐ ${repo.stars} estrelas"
                        )

                        Text(
                            text = "Linguagem: ${repo.language ?: "Não informada"}",
                            style = MaterialTheme.typography.bodySmall
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
