package com.pdm.dailydev.filtro

import com.pdm.dailydev.model.Licao
import com.pdm.dailydev.model.Tentativa

enum class FaixaDificuldade(val titulo: String, val niveis: IntRange) {
    FACIL("Fácil", 1..3),
    MEDIO("Médio", 4..6),
    DIFICIL("Difícil", 7..10)
}

enum class StatusLicao(val titulo: String) {
    INEDITA("Inédita"),
    REVISAR("Revisar"),
    DOMINADA("Dominada")
}

enum class OrdemLicoes(val titulo: String) {
    PADRAO("Padrão"),
    MAIS_FACEIS("Mais fáceis"),
    MAIS_DIFICEIS("Mais difíceis"),
    ALFABETICA("A–Z")
}

// null = "Todos" (não filtra por esse campo)
data class FiltroLicoes(
    val assunto: String = "", // texto digitado na busca; vazio = todos os assuntos
    val faixa: FaixaDificuldade? = null,
    val status: StatusLicao? = null,
    val ordem: OrdemLicoes = OrdemLicoes.PADRAO
)

fun statusDaLicao(licao: Licao, historico: List<Tentativa>): StatusLicao {
    val tentativas = historico.filter { it.licaoId == licao.id }
    return when {
        tentativas.isEmpty() -> StatusLicao.INEDITA
        tentativas.maxOf { it.aproveitamento } >= 0.7 -> StatusLicao.DOMINADA
        else -> StatusLicao.REVISAR
    }
}

fun aplicarFiltro(
    catalogo: List<Licao>,
    historico: List<Tentativa>,
    filtro: FiltroLicoes
): List<Licao> {
    val filtradas = catalogo.filter { licao ->
        // Busca por texto: o nome do assunto contém o que foi digitado (sem diferenciar maiúsculas)
        (filtro.assunto.isBlank() ||
            licao.assunto.nome.contains(filtro.assunto.trim(), ignoreCase = true)) &&
            (filtro.faixa == null || licao.dificuldade in filtro.faixa.niveis) &&
            (filtro.status == null || statusDaLicao(licao, historico) == filtro.status)
    }
    return when (filtro.ordem) {
        OrdemLicoes.PADRAO -> filtradas
        OrdemLicoes.MAIS_FACEIS -> filtradas.sortedBy { it.dificuldade }
        OrdemLicoes.MAIS_DIFICEIS -> filtradas.sortedByDescending { it.dificuldade }
        OrdemLicoes.ALFABETICA -> filtradas.sortedBy { it.titulo }
    }
}
