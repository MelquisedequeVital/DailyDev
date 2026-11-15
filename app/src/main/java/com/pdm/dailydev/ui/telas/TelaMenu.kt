package com.pdm.dailydev.ui.telas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pdm.dailydev.model.NivelUsuario
import com.pdm.dailydev.model.Usuario
import com.pdm.dailydev.ui.theme.DailyDevColors

// Itens do protótipo que ainda não existem no app
private val itensMenu = listOf("Personagem", "Comunidade", "Amigos", "Tema", "Sair")

@Composable
fun TelaMenu(usuario: Usuario) {
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = item, color = DailyDevColors.TextPrimary, fontSize = 15.sp)
                        Text(text = "Em breve", color = DailyDevColors.TextSecondary, fontSize = 13.sp)
                    }
                }
            }
        }
    }
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
