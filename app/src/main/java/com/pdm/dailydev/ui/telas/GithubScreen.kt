
package com.pdm.dailydev.ui.telas

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pdm.dailydev.ui.components.GithubRepositoriesSection
import com.pdm.dailydev.ui.theme.DailyDevColors

@Composable
fun GithubScreen(
    onVoltarClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVoltarClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = DailyDevColors.TextPrimary // claro, para aparecer no fundo escuro
                )
            }

            Text(
                text = "Materiais complementares",
                color = DailyDevColors.TextPrimary,
                style = MaterialTheme.typography.titleLarge
            )
        }

        GithubRepositoriesSection()
    }
}
