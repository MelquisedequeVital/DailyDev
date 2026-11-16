package com.pdm.dailydev.ui.telas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pdm.dailydev.ui.components.GithubRepositoriesSection

@Composable
fun GithubScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Materiais complementares")

        GithubRepositoriesSection()
    }
}