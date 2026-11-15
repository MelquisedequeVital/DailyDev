package com.pdm.dailydev

import android.net.http.HttpException
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pdm.dailydev.ui.DailyDevApp
import android.util.Log
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.pdm.dailydev.repository.GithubRepository
import java.io.IOException



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DailyDevApp()
        }

        val githubRepository = GithubRepository()

        lifecycleScope.launch {
            val resultado = githubRepository.searchRepositories("language:kotlin")

            resultado.onSuccess { repositorios ->
                Log.d("DailyDevAPI", "Total de repositórios recebidos: ${repositorios.size}")
                repositorios.take(5).forEach { repo -> Log.d("DailyDevAPI", "Nome: ${repo.fullName} | Estrelas: ${repo.stars}") }
            }

            resultado.onFailure { erro ->
                val mensagem = when (erro) {
                    is IOException -> "Verifique sua conexão com a internet"

                    is retrofit2.HttpException -> {
                        when (erro.code()) {
                            403 -> "Limite de requisições atingido ou acesso proibido."
                            404 -> "Recurso não encontrado."
                            500 -> "O servidor está com problemas."
                            else -> "Erro HTTP: ${erro.code()}"
                        }
                    }

                    else -> "Ocorreu um erro inesperado"
                }

                Log.e("DailyDevAPI", mensagem, erro)
            }
        }
    }
}