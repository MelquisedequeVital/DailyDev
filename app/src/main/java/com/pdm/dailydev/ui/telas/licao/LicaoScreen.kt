package com.pdm.dailydev.ui.telas.licao

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pdm.dailydev.model.Licao
import com.pdm.dailydev.ui.components.AlternativaCard
import com.pdm.dailydev.ui.components.ExplicacaoCard
import com.pdm.dailydev.ui.components.LicaoHeader
import com.pdm.dailydev.ui.theme.DailyDevColors

@Composable
fun LicaoScreen(
    licao: Licao,
    onVoltarClick: () -> Unit = {},
    onConcluirLicao: (respostas: List<Int>, irParaProxima: Boolean) -> Unit = { _, _ -> },
    viewModel: LicaoViewModel = viewModel(key = licao.id.toString()) { LicaoViewModel(licao) }
) {
    val uiState by viewModel.uiState.collectAsState()

    val concluirEVoltar = {
        viewModel.avancar { respostas ->
            onConcluirLicao(respostas, false)
        }
    }

    val concluirEAvancar = {
        viewModel.avancar { respostas ->
            onConcluirLicao(respostas, true)
        }
    }

    Scaffold(
        containerColor = DailyDevColors.Background,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                LicaoHeader(
                    titulo = licao.titulo,
                    etapaAtual = uiState.etapaAtual,
                    totalEtapas = uiState.totalEtapas,
                    onVoltarClick = {
                        if (uiState is LicaoUiState.Resultado) {
                            concluirEVoltar()
                        } else {
                            viewModel.voltar(onVoltarClick)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = licao.assunto.nome,
                    color = DailyDevColors.TextSecondary,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(start = 36.dp)
                )
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = concluirEAvancar,
                    enabled = uiState.podeAvancar,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DailyDevColors.PrimaryBlue,
                        disabledContainerColor = DailyDevColors.SurfaceCard
                    )
                ) {
                    Text(
                        text = uiState.textoBotao,
                        color = DailyDevColors.TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (uiState is LicaoUiState.Resultado) {
                    OutlinedButton(
                        onClick = concluirEVoltar,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.5.dp, DailyDevColors.BorderDefault),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = DailyDevColors.TextPrimary
                        )
                    ) {
                        Text(
                            text = "Voltar ao início",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            when (val s = uiState) {
                is LicaoUiState.Explicacao -> ExplicacaoCard(texto = s.texto)

                is LicaoUiState.QuestaoAtual -> {
                    Text(
                        text = s.questao.enunciado,
                        color = DailyDevColors.TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    s.questao.alternativas.forEachIndexed { indiceAlt, textoAlt ->
                        AlternativaCard(
                            indice = indiceAlt,
                            texto = textoAlt,
                            selecionada = s.alternativaSelecionada == indiceAlt,
                            onClick = { viewModel.selecionarAlternativa(indiceAlt) }
                        )
                    }
                }

                is LicaoUiState.Resultado -> ResultadoCard(
                    acertos = s.acertos,
                    totalQuestoes = s.totalQuestoes,
                    onVoltarInicioClick = concluirEVoltar
                )
            }
        }
    }
}