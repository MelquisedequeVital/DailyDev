package com.pdm.dailydev.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pdm.dailydev.model.Usuario
import com.pdm.dailydev.ui.theme.DailyDevColors

@Composable
fun DailyDevTopBar(
    usuario: Usuario,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "</>",
                color = DailyDevColors.PrimaryBlue,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "DailyDev",
                color = DailyDevColors.TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = usuario.nome,
                color = DailyDevColors.TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.width(10.dp))
            Surface(
                modifier = Modifier.size(34.dp),
                shape = CircleShape,
                color = DailyDevColors.SurfaceCard,
                border = BorderStroke(1.5.dp, DailyDevColors.PrimaryBlue)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = usuario.nome.take(1).uppercase(),
                        color = DailyDevColors.PrimaryBlue,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}