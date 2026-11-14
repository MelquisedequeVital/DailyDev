package com.pdm.dailydev.ui.components



import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pdm.dailydev.ui.theme.DailyDevColors


@Composable
fun ExplicacaoCard(
    texto: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = DailyDevColors.SurfaceCard,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.5.dp, DailyDevColors.BorderDefault)
    ) {
        Text(
            text = texto,
            color = DailyDevColors.TextPrimary,
            fontSize = 15.sp,
            lineHeight = 22.sp,
            modifier = Modifier.padding(16.dp)
        )
    }
}