package com.pdm.dailydev.ui.components

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
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

    LaunchedEffect(tentativa) {
        carregando.value = true
        mensagemErro.value = null

        try {
            val resultado = GithubRepository().searchRepositories("language:kotlin")

            resultado.onSuccess {
                repositorios.value = it
            }

            resultado.onFailure { erro ->
                mensagemErro.value = when (erro) {
                    is IOException -> "Verifique sua conexão com a internet"

                    is HttpException ->
                        "Erro HTTP: ${erro.code()}"

                    else ->
                        "Não foi possível carregar os repositórios."
                }
            }
        } catch (e: CancellationException) {
            throw e
        }

        carregando.value = false
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        Text (
            text = "Repositórios Kotlin",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer (modifier = Modifier.height(12.dp))

        if (carregando.value) {
            CircularProgressIndicator()
        } else if (mensagemErro.value != null) {
            Text(text = mensagemErro.value!!)

            Button (onClick = {tentativa++}) {
                Text("Tentar novamente")
            }
        } else {
            LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
                items (
                    items = repositorios.value,
                    key = {it.fullName}
                ) { repo ->
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = repo.fullName,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = repo.description ?: "Sem descrição"
                        )

                        Text(
                            text = "⭐ ${repo.stars} estrelas"
                        )

                        HorizontalDivider()
                    }
                }
            }
        }
    }
}