package com.pdm.dailydev.ui.telas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pdm.dailydev.model.Licao
import com.pdm.dailydev.model.NivelUsuario
import com.pdm.dailydev.model.Tentativa
import com.pdm.dailydev.model.Usuario
import com.pdm.dailydev.ui.components.DailyDevTopBar
import com.pdm.dailydev.ui.theme.DailyDevColors

@Composable
fun TelaPrincipal(
    usuario: Usuario,
    catalogoLicoes: List<Licao>,
    proximaLicao: Licao?,
    onIniciarClick: () -> Unit
) {
    val temLicaoDisponivel = proximaLicao != null
    val licoesPorId = remember(catalogoLicoes) { catalogoLicoes.associateBy { it.id } }
    val historicoRecente = usuario.historico.asReversed()

    Scaffold(
        containerColor = DailyDevColors.Background,
        topBar = { DailyDevTopBar(usuario = usuario) },
        bottomBar = {
            Button(
                onClick = onIniciarClick,
                enabled = temLicaoDisponivel,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DailyDevColors.PrimaryBlue,
                    disabledContainerColor = DailyDevColors.SurfaceCard
                )
            ) {
                Text(
                    text = if (temLicaoDisponivel) "Iniciar Lição Recomendada" else "Todas as lições concluídas",
                    color = if (temLicaoDisponivel) DailyDevColors.TextPrimary else DailyDevColors.TextSecondary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Saudação + Ofensiva
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Olá, ${usuario.nome}!",
                    color = DailyDevColors.TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    shape = RoundedCornerShape(50),
                    color = DailyDevColors.SurfaceCard,
                    border = BorderStroke(1.5.dp, DailyDevColors.AccentOrange)
                ) {
                    Text(
                        text = "7 dias",
                        color = DailyDevColors.AccentOrange,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }
            }

            // Seção: Seu nível
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Seu nível",
                    color = DailyDevColors.TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = DailyDevColors.SurfaceCard,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.5.dp, DailyDevColors.BorderDefault)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        NivelItemRow(
                            nome = "Iniciante",
                            desbloqueado = true,
                            atual = usuario.nivel == NivelUsuario.INICIANTE
                        )
                        NivelItemRow(
                            nome = "Intermediário",
                            desbloqueado = usuario.nivel.valor >= NivelUsuario.INTERMEDIARIO.valor,
                            atual = usuario.nivel == NivelUsuario.INTERMEDIARIO
                        )
                        NivelItemRow(
                            nome = "Avançado",
                            desbloqueado = usuario.nivel.valor >= NivelUsuario.AVANCADO.valor,
                            atual = usuario.nivel == NivelUsuario.AVANCADO
                        )
                    }
                }
            }

            // Seção: Histórico de Lições Resolvidas
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Lições resolvidas",
                        color = DailyDevColors.TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${historicoRecente.size}/${catalogoLicoes.size}",
                        color = DailyDevColors.PrimaryBlue,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                if (historicoRecente.isEmpty()) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = DailyDevColors.SurfaceCard,
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.5.dp, DailyDevColors.BorderDefault)
                    ) {
                        Text(
                            text = "Nenhuma lição resolvida ainda. Toque no botão abaixo para começar.",
                            color = DailyDevColors.TextSecondary,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(18.dp)
                        )
                    }
                } else {
                    historicoRecente.forEach { tentativa ->
                        val licaoResolvida = licoesPorId[tentativa.licaoId]
                        HistoricoLicaoCard(
                            tentativa = tentativa,
                            titulo = licaoResolvida?.titulo ?: "Lição concluída",
                            assunto = licaoResolvida?.assunto?.nome?: ""
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoricoLicaoCard(
    tentativa: Tentativa,
    titulo: String,
    assunto: String
) {
    val erros = tentativa.totalQuestoes - tentativa.acertos

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = DailyDevColors.SurfaceCard,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.5.dp, DailyDevColors.BorderDefault)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = titulo,
                    color = DailyDevColors.TextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${tentativa.acertos}/${tentativa.totalQuestoes}",
                    color = if (tentativa.aprovado) DailyDevColors.SuccessGreen else DailyDevColors.AccentOrange,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = assunto,
                    color = DailyDevColors.TextSecondary,
                    fontSize = 13.sp
                )
                Text(
                    text = "${tentativa.acertos} acerto(s) • $erros erro(s)",
                    color = DailyDevColors.TextSecondary,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            LinearProgressIndicator(
                progress = { tentativa.aproveitamento.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(50)),
                color = if (tentativa.aprovado) DailyDevColors.SuccessGreen else DailyDevColors.AccentOrange,
                trackColor = DailyDevColors.Background
            )
        }
    }
}

@Composable
private fun NivelItemRow(
    nome: String,
    desbloqueado: Boolean,
    atual: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = nome,
            color = if (desbloqueado) DailyDevColors.TextPrimary else DailyDevColors.TextSecondary,
            fontSize = 16.sp,
            fontWeight = if (atual) FontWeight.Bold else FontWeight.Normal
        )

        val indicador = when {
            atual -> "✓"
            desbloqueado -> ">"
            else -> "Bloqueado"
        }

        val corIndicador = when {
            atual -> DailyDevColors.SuccessGreen
            desbloqueado -> DailyDevColors.PrimaryBlue
            else -> DailyDevColors.TextSecondary
        }

        Text(
            text = indicador,
            color = corIndicador,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}