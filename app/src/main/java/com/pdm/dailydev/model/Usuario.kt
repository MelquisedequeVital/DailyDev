package com.pdm.dailydev.model
import java.util.UUID

enum class NivelUsuario(val valor: Int) {
    INICIANTE(1),
    INTERMEDIARIO(2),
    AVANCADO(3)
}

data class Usuario(
    val id: UUID = UUID.randomUUID(),
    val email: String,
    val senhaHash: String,
    var nivel: NivelUsuario = NivelUsuario.INICIANTE
) {
    private val _historico = mutableListOf<Tentativa>()

    // Expõe apenas para leitura fora da classe
    val historico: List<Tentativa>
        get() = _historico.toList()

    // Método de domínio: o próprio usuário registra o resultado da lição
    fun registrarTentativa(licao: Licao, respostasEscolhidas: List<Int>): Tentativa {
        val acertos = licao.calcularAcertos(respostasEscolhidas)
        val tentativa = Tentativa(
            usuarioId = this.id,
            licaoId = licao.id,
            acertos = acertos,
            totalQuestoes = licao.questoes.size
        )
        _historico.add(tentativa)
        return tentativa
    }
}