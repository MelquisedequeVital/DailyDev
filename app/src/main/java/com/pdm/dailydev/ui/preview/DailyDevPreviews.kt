package com.pdm.dailydev.ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.pdm.dailydev.model.Assunto
import com.pdm.dailydev.model.Licao
import com.pdm.dailydev.model.NivelUsuario
import com.pdm.dailydev.model.Questao
import com.pdm.dailydev.model.Usuario
import com.pdm.dailydev.ui.telas.LicaoScreen
import com.pdm.dailydev.ui.telas.TelaPrincipal
import com.pdm.dailydev.ui.telas.licao.LicaoViewModel

private object DadosPreview {
    val assuntoKotlin = Assunto(nome = "Variáveis e Tipos")
    val assuntoGit = Assunto(nome = "Git")

    val licaoVal = Licao(
        titulo = "Variáveis Imutáveis vs Mutáveis",
        textoExplicacao = "Em Kotlin, 'val' cria uma referência imutável e 'var' cria uma variável mutável.",
        assunto = assuntoKotlin,
        dificuldade = 1,
        questoes = listOf(
            Questao(
                enunciado = "Qual palavra-chave cria uma variável imutável em Kotlin?",
                alternativas = listOf("var", "val", "let", "const"),
                indiceCorreto = 1
            ),
            Questao(
                enunciado = "O que ocorre ao reatribuir uma variável 'val'?",
                alternativas = listOf("Atualiza normalmente", "Erro de compilação"),
                indiceCorreto = 1
            )
        )
    )

    val licaoGit = Licao(
        titulo = "Branches e Merge no Git",
        textoExplicacao = "Branches isolam o desenvolvimento de novas funcionalidades.",
        assunto = assuntoGit,
        dificuldade = 3,
        questoes = listOf(
            Questao(
                enunciado = "Qual comando cria e troca para a branch 'feature'?",
                alternativas = listOf("git branch feature", "git checkout -b feature"),
                indiceCorreto = 1
            )
        )
    )

    val catalogo = listOf(licaoVal, licaoGit)

    fun usuarioNovo() = Usuario(nome = "Dev", email = "preview@dailydev.com", senhaHash = "")

    fun usuarioComHistorico() = usuarioNovo().apply {
        nivel = NivelUsuario.INTERMEDIARIO
        registrarTentativa(licaoVal, listOf(1, 1))
        registrarTentativa(licaoGit, listOf(0))
    }
}

@Preview(name = "Principal - vazio", showBackground = true, showSystemUi = true)
@Composable
private fun TelaPrincipalVaziaPreview() {
    TelaPrincipal(
        usuario = DadosPreview.usuarioNovo(),
        catalogoLicoes = DadosPreview.catalogo,
        proximaLicao = DadosPreview.licaoVal,
        onIniciarClick = {}
    )
}

@Preview(name = "Principal - com histórico", showBackground = true, showSystemUi = true)
@Composable
private fun TelaPrincipalHistoricoPreview() {
    TelaPrincipal(
        usuario = DadosPreview.usuarioComHistorico(),
        catalogoLicoes = DadosPreview.catalogo,
        proximaLicao = null,
        onIniciarClick = {}
    )
}

@Preview(name = "Lição - explicação", showBackground = true, showSystemUi = true)
@Composable
private fun LicaoExplicacaoPreview() {
    val licao = DadosPreview.licaoVal
    LicaoScreen(
        licao = licao,
        viewModel = remember { LicaoViewModel(licao) }
    )
}

@Preview(name = "Lição - questão", showBackground = true, showSystemUi = true)
@Composable
private fun LicaoQuestaoPreview() {
    val licao = DadosPreview.licaoVal
    LicaoScreen(
        licao = licao,
        viewModel = remember {
            LicaoViewModel(licao).apply {
                avancar {}
                selecionarAlternativa(1)
            }
        }
    )
}

@Preview(name = "Lição - resultado", showBackground = true, showSystemUi = true)
@Composable
private fun LicaoResultadoPreview() {
    val licao = DadosPreview.licaoGit
    LicaoScreen(
        licao = licao,
        viewModel = remember {
            LicaoViewModel(licao).apply {
                avancar {}
                selecionarAlternativa(1)
                avancar {}
            }
        }
    )
}
