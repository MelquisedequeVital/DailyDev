package com.pdm.dailydev.model

import java.util.UUID

class Usuario(
    val id: UUID = UUID.randomUUID(),
    val nome: String,
    val email: String,
    val senhaHash: String,
    var nivel: NivelUsuario = NivelUsuario.INICIANTE
) {
    private val _historico = mutableListOf<Tentativa>()
    val historico: List<Tentativa> get() = _historico.toList()

    fun registrarTentativa(licao: Licao, respostas: List<Int>): Tentativa {
        val acertos = licao.calcularAcertos(respostas)
        val tentativa = Tentativa(
            usuarioId = id,
            licaoId = licao.id,
            acertos = acertos,
            totalQuestoes = licao.questoes.size
        )
        _historico.add(tentativa)
        return tentativa
    }

    fun aproveitamentoPorAssunto(assunto: Assunto, catalogo: List<Licao>): Double? {
        val licoesDoAssuntoIds = catalogo
            .filter { it.assunto == assunto }
            .map { it.id }
            .toSet()

        val tentativasDoAssunto = _historico.filter { it.licaoId in licoesDoAssuntoIds }
        if (tentativasDoAssunto.isEmpty()) return null

        return tentativasDoAssunto.map { it.aproveitamento }.average()
    }
}