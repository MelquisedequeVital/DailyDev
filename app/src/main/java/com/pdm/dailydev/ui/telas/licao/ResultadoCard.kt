package com.pdm.dailydev.ui.telas.licao


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pdm.dailydev.ui.theme.DailyDevColors


@Composable
fun ResultadoCard(
    acertos: Int,
    totalQuestoes: Int,
    modifier: Modifier = Modifier
) {
    val foiBem = acertos >= (totalQuestoes / 2.0)
    val corDestaque = if (foiBem) DailyDevColors.SuccessGreen else DailyDevColors.PrimaryBlue
    val corFundo = if (foiBem) DailyDevColors.SuccessGreenContainer else DailyDevColors.SurfaceCard

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = corFundo,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, corDestaque)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Lição Concluída!",
                color = corDestaque,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Você acertou $acertos de $totalQuestoes questões",
                color = DailyDevColors.TextPrimary,
                fontSize = 16.sp
            )
        }
    }
}