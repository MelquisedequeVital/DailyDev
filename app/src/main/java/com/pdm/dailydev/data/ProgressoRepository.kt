package com.pdm.dailydev.data

import com.pdm.dailydev.model.Tentativa
import java.util.UUID

/**
 * Salva e recupera o progresso (histórico de tentativas) do aluno.
 * É uma interface, igual ao LicaoRepository, para dar para trocar o banco sem mexer na UI.
 */
interface ProgressoRepository {
    /** Tentativas já salvas do aluno, da mais antiga para a mais nova. */
    suspend fun carregarHistorico(usuarioId: UUID): List<Tentativa>

    suspend fun salvarTentativa(tentativa: Tentativa)
}
