package com.pdm.dailydev.ui.telas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pdm.dailydev.model.NivelUsuario
import com.pdm.dailydev.model.Usuario
import com.pdm.dailydev.ui.theme.DailyDevColors
import androidx.compose.foundation.clickable

// Itens do protótipo que ainda não existem no app
private val itensMenu = listOf("Personagem", "Projetos no GitHub", "Comunidade", "Amigos", "Tema", "Sair")

@Composable
fun TelaMenu(
    usuario: Usuario,
    onGithubClick: () -> Unit,
    onSalvarApelido: (String) -> Unit = {},
    onExcluirConta: () -> Unit = {}
) {
    // Guarda se a janela de editar apelido está aberta
    var editandoApelido by remember { mutableStateOf(false) }
    // Guarda se a janela de confirmar a exclusão está aberta
    var confirmandoExclusao by remember { mutableStateOf(false) }

    if (confirmandoExclusao) {
        ExcluirContaDialog(
            onConfirmar = {
                confirmandoExclusao = false
                onExcluirConta()
            },
            onCancelar = { confirmandoExclusao = false }
        )
    }

    if (editandoApelido) {
        EditarApelidoDialog(
            apelidoAtual = usuario.nome,
            onSalvar = { novo ->
                onSalvarApelido(novo)
                editandoApelido = false
            },
            onCancelar = { editandoApelido = false }
        )
    }

    val licoesFeitas = usuario.historico.map { it.licaoId }.distinct().size

    val licoesDominadas = usuario.historico
        .groupBy { it.licaoId }
        .count { (_, tentativas) -> tentativas.maxOf { it.aproveitamento } >= 0.7 }

    val nomeNivel = when (usuario.nivel) {
        NivelUsuario.INICIANTE -> "Iniciante"
        NivelUsuario.INTERMEDIARIO -> "Intermediário"
        NivelUsuario.AVANCADO -> "Avançado"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DailyDevColors.Background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Perfil: avatar com a inicial, nome, e-mail e nível
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                modifier = Modifier.size(64.dp),
                shape = CircleShape,
                color = DailyDevColors.SurfaceCard,
                border = BorderStroke(2.dp, DailyDevColors.PrimaryBlue)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = usuario.nome.take(1).uppercase(),
                        color = DailyDevColors.PrimaryBlue,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = usuario.nome,
                    color = DailyDevColors.TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = usuario.email,
                    color = DailyDevColors.TextSecondary,
                    fontSize = 14.sp
                )
                Text(
                    text = "Nível: $nomeNivel",
                    color = DailyDevColors.PrimaryBlue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Resumo do progresso
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ResumoCard(titulo = "Lições feitas", valor = "$licoesFeitas", modifier = Modifier.weight(1f))
            ResumoCard(titulo = "Dominadas", valor = "$licoesDominadas", modifier = Modifier.weight(1f))
        }

        // Itens do menu (ainda não implementados)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = DailyDevColors.SurfaceCard,
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.5.dp, DailyDevColors.BorderDefault)
        ) {
            Column {
                itensMenu.forEachIndexed { indice, item ->

                    if (indice > 0) {
                        HorizontalDivider(color = DailyDevColors.BorderDefault)
                    }

                    // O que acontece ao tocar no item (null = ainda não implementado)
                    val acao: (() -> Unit)? = when (item) {
                        "Personagem" -> { { editandoApelido = true } }
                        "Projetos no GitHub" -> onGithubClick
                        else -> null
                    }
                    val textoDireita = when (item) {
                        "Personagem" -> "Editar apelido"
                        "Projetos no GitHub" -> "Explorar"
                        else -> "Em breve"
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                enabled = acao != null,
                                onClick = { acao?.invoke() }
                            )
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = item,
                            color = DailyDevColors.TextPrimary,
                            fontSize = 15.sp
                        )

                        Text(
                            text = textoDireita,
                            color = DailyDevColors.TextSecondary,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // Ação perigosa fica separada dos outros itens e pede confirmação
        TextButton(
            onClick = { confirmandoExclusao = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Excluir conta", color = DailyDevColors.AccentOrange)
        }
    }
}

// Janela de confirmação: excluir a conta não tem volta
@Composable
private fun ExcluirContaDialog(
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Excluir conta?") },
        text = { Text("Isso apaga seu apelido e todo o seu progresso. Não dá para desfazer.") },
        confirmButton = {
            TextButton(onClick = onConfirmar) {
                Text("Excluir", color = DailyDevColors.AccentOrange)
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) {
                Text("Cancelar")
            }
        }
    )
}

// Janela (Material 3) com uma caixa de texto para trocar o apelido
@Composable
private fun EditarApelidoDialog(
    apelidoAtual: String,
    onSalvar: (String) -> Unit,
    onCancelar: () -> Unit
) {
    // Texto que o aluno está digitando, começa com o apelido atual
    var texto by remember { mutableStateOf(apelidoAtual) }

    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Editar apelido") },
        text = {
            OutlinedTextField(
                value = texto,
                onValueChange = { texto = it.take(20) }, // limite de 20 caracteres
                label = { Text("Apelido") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onSalvar(texto) },
                enabled = texto.isNotBlank()
            ) {
                Text("Salvar")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun ResumoCard(titulo: String, valor: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = DailyDevColors.SurfaceCard,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.5.dp, DailyDevColors.BorderDefault)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = valor,
                color = DailyDevColors.TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(text = titulo, color = DailyDevColors.TextSecondary, fontSize = 13.sp)
        }
    }
}
