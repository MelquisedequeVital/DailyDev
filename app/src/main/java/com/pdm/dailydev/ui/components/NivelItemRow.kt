package com.pdm.dailydev.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.pdm.dailydev.ui.theme.DailyDevColors

@Composable
fun NivelItemRow(
    nome: String,
    desbloqueado: Boolean,
    atual: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
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