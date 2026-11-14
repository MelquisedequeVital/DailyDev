package com.pdm.dailydev.ui.components



import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pdm.dailydev.ui.theme.DailyDevColors


@Composable
fun AlternativaCard(
    indice: Int,
    texto: String,
    selecionada: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val letra = ('A' + indice).toString()

    val corFundo = if (selecionada) DailyDevColors.PrimaryBlueContainer else DailyDevColors.SurfaceCard
    val corBorda = if (selecionada) DailyDevColors.PrimaryBlue else DailyDevColors.BorderDefault
    val corBadge = if (selecionada) DailyDevColors.PrimaryBlue else DailyDevColors.BorderDefault

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        color = corFundo,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(if (selecionada) 2.dp else 1.5.dp, corBorda)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(corBadge),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = letra,
                    color = DailyDevColors.TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = texto,
                color = DailyDevColors.TextPrimary,
                fontSize = 16.sp,
                fontWeight = if (selecionada) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}