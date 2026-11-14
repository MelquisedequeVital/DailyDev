package com.pdm.dailydev.recomendacao

import com.pdm.dailydev.model.Licao
import com.pdm.dailydev.model.Tentativa
import com.pdm.dailydev.model.Usuario

fun interface MotorRecomendacao {
    fun recomendarProxima(
        usuario: Usuario,
        licoesDisponiveis: List<Licao>
    ): Licao?
}