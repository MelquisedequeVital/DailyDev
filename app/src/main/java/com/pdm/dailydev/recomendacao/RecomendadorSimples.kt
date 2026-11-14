package com.pdm.dailydev.recomendacao

import com.pdm.dailydev.model.Licao
import com.pdm.dailydev.model.Tentativa
import com.pdm.dailydev.model.Usuario
import java.util.UUID

class RecomendadorSimples : MotorRecomendacao {

    override fun recomendarProxima(
        usuario: Usuario,
        licoesDisponiveis: List<Licao>
    ): Licao? {
        val idsRespondidos = usuario.historico.map { it.licaoId }.toSet()
        val dificuldadeMaxima = usuario.nivel.valor * 3

        return licoesDisponiveis.firstOrNull { licao ->
            podeSerRecomendada(licao, idsRespondidos, dificuldadeMaxima)
        }
    }

    private fun podeSerRecomendada(
        licao: Licao,
        idsRespondidos: Set<UUID>,
        dificuldadeMaxima: Int
    ): Boolean = licao.id !in idsRespondidos && licao.dificuldade <= dificuldadeMaxima
}