package com.pdm.dailydev.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pdm.dailydev.model.Tentativa
import com.pdm.dailydev.ui.theme.DailyDevColors

@Composable
fun HistoricoLicaoCard(
    tentativa: Tentativa,
    titulo: String,
    assunto: String,
    modifier: Modifier = Modifier
) {
    val erros = tentativa.totalQuestoes - tentativa.acertos

    Surface(
        modifier = modifier.fillMaxWidth(),
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