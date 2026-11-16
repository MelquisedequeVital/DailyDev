package com.pdm.dailydev.filtro

import com.pdm.dailydev.model.Assunto
import com.pdm.dailydev.model.Licao
import com.pdm.dailydev.model.Questao
import com.pdm.dailydev.model.Tentativa
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.UUID

class FiltroLicoesTest {

    private val kotlin = Assunto(nome = "Kotlin")
    private val git = Assunto(nome = "Git")

    private val questao = Questao(enunciado = "?", alternativas = listOf("A", "B"), indiceCorreto = 0)

    private val facilKotlin = Licao(titulo = "B fácil", textoExplicacao = "", questoes = listOf(questao), assunto = kotlin, dificuldade = 2)
    private val dificilKotlin = Licao(titulo = "A difícil", textoExplicacao = "", questoes = listOf(questao), assunto = kotlin, dificuldade = 8)
    private val medioGit = Licao(titulo = "C médio", textoExplicacao = "", questoes = listOf(questao), assunto = git, dificuldade = 5)

    private val catalogo = listOf(facilKotlin, dificilKotlin, medioGit)
    private val usuarioId = UUID.randomUUID()

    private fun tentativa(licao: Licao, acertos: Int) =
        Tentativa(usuarioId = usuarioId, licaoId = licao.id, acertos = acertos, totalQuestoes = 1)

    @Test
    fun semFiltro_retornaCatalogoNaOrdemOriginal() {
        val resultado = aplicarFiltro(catalogo, emptyList(), FiltroLicoes())
        assertEquals(catalogo, resultado)
    }

    @Test
    fun buscaPorAssunto_retornaSoLicoesDoAssunto() {
        val resultado = aplicarFiltro(catalogo, emptyList(), FiltroLicoes(assunto = "git"))
        assertEquals(listOf(medioGit), resultado)
    }

    @Test
    fun buscaPorAssunto_aceitaTextoParcialEMaiusculas() {
        val resultado = aplicarFiltro(catalogo, emptyList(), FiltroLicoes(assunto = "KOT"))
        assertEquals(listOf(facilKotlin, dificilKotlin), resultado)
    }

    @Test
    fun buscaSoComEspacos_retornaTudo() {
        val resultado = aplicarFiltro(catalogo, emptyList(), FiltroLicoes(assunto = "   "))
        assertEquals(catalogo, resultado)
    }

    @Test
    fun filtroPorFaixa_retornaSoLicoesDaFaixa() {
        val resultado = aplicarFiltro(catalogo, emptyList(), FiltroLicoes(faixa = FaixaDificuldade.DIFICIL))
        assertEquals(listOf(dificilKotlin), resultado)
    }

    @Test
    fun filtroPorStatus_separaIneditaRevisarDominada() {
        val historico = listOf(
            tentativa(facilKotlin, acertos = 1), // 100% -> dominada
            tentativa(medioGit, acertos = 0)     // 0%   -> revisar
        )

        assertEquals(listOf(facilKotlin), aplicarFiltro(catalogo, historico, FiltroLicoes(status = StatusLicao.DOMINADA)))
        assertEquals(listOf(medioGit), aplicarFiltro(catalogo, historico, FiltroLicoes(status = StatusLicao.REVISAR)))
        assertEquals(listOf(dificilKotlin), aplicarFiltro(catalogo, historico, FiltroLicoes(status = StatusLicao.INEDITA)))
    }

    @Test
    fun ordenacao_maisFaceisEAlfabetica() {
        val porDificuldade = aplicarFiltro(catalogo, emptyList(), FiltroLicoes(ordem = OrdemLicoes.MAIS_FACEIS))
        assertEquals(listOf(facilKotlin, medioGit, dificilKotlin), porDificuldade)

        val alfabetica = aplicarFiltro(catalogo, emptyList(), FiltroLicoes(ordem = OrdemLicoes.ALFABETICA))
        assertEquals(listOf(dificilKotlin, facilKotlin, medioGit), alfabetica)
    }
}
