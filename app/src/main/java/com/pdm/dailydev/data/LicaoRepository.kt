package com.pdm.dailydev.data

import com.pdm.dailydev.model.Assunto
import com.pdm.dailydev.model.Licao
import java.util.UUID

interface LicaoRepository {
    suspend fun obterTodosAssuntos(): List<Assunto>
    suspend fun obterTodasLicoes(): List<Licao>
    suspend fun obterLicoesPorAssunto(assuntoId: UUID): List<Licao>
    suspend fun obterLicaoPorId(id: UUID): Licao?
}