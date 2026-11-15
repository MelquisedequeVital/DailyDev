package com.pdm.dailydev.ui.telas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pdm.dailydev.filtro.FaixaDificuldade
import com.pdm.dailydev.filtro.FiltroLicoes
import com.pdm.dailydev.filtro.OrdemLicoes
import com.pdm.dailydev.filtro.StatusLicao
import com.pdm.dailydev.filtro.aplicarFiltro
import com.pdm.dailydev.model.Licao
import com.pdm.dailydev.model.Tentativa
import com.pdm.dailydev.ui.theme.DailyDevColors

@Composable
fun TelaLicoes(
    catalogoLicoes: List<Licao>,
    historico: List<Tentativa>,
    filtro: FiltroLicoes,
    onFiltroChange: (FiltroLicoes) -> Unit,
    onLicaoClick: (Licao) -> Unit
) {
    // Chips de assunto vêm do catálogo inteiro, para não sumirem ao filtrar
    val assuntos = catalogoLicoes.map { it.assunto }.distinct()

    // Aplica o filtro e agrupa o resultado pelo assunto: Map<Assunto, List<Licao>>
    val licoesPorAssunto = aplicarFiltro(catalogoLicoes, historico, filtro).groupBy { it.assunto }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DailyDevColors.Background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Lições",
            color = DailyDevColors.TextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        // null = "Todos" (primeira opção de cada filtro)
        LinhaDeChips(
            titulo = "Assunto",
            opcoes = listOf(null) + assuntos,
            selecionada = assuntos.find { it.id == filtro.assuntoId },
            texto = { it?.nome ?: "Todos" },
            onSelecionar = { onFiltroChange(filtro.copy(assuntoId = it?.id)) }
        )
        LinhaDeChips(
            titulo = "Dificuldade",
            opcoes = listOf(null) + FaixaDificuldade.entries,
            selecionada = filtro.faixa,
            texto = { faixa ->
                if (faixa == null) "Todas" else "${faixa.titulo} ${faixa.niveis.first}–${faixa.niveis.last}"
            },
            onSelecionar = { onFiltroChange(filtro.copy(faixa = it)) }
        )
        LinhaDeChips(
            titulo = "Status",
            opcoes = listOf(null) + StatusLicao.entries,
            selecionada = filtro.status,
            texto = { it?.titulo ?: "Todos" },
            onSelecionar = { onFiltroChange(filtro.copy(status = it)) }
        )
        LinhaDeChips(
            titulo = "Ordenar",
            opcoes = OrdemLicoes.entries,
            selecionada = filtro.ordem,
            texto = { it.titulo },
            onSelecionar = { onFiltroChange(filtro.copy(ordem = it)) }
        )

        TextButton(
            onClick = { onFiltroChange(FiltroLicoes()) },
            enabled = filtro != FiltroLicoes()
        ) {
            Text("Limpar filtros")
        }

        if (licoesPorAssunto.isEmpty()) {
            Text(
                text = "Nenhuma lição encontrada com esses filtros.",
                color = DailyDevColors.TextSecondary,
                fontSize = 14.sp
            )
        }

        licoesPorAssunto.forEach { (assunto, licoes) ->
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = assunto.nome,
                    color = DailyDevColors.TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                licoes.forEach { licao ->
                    LicaoItem(licao = licao, onClick = { onLicaoClick(licao) })
                }
            }
        }
    }
}

// Uma linha de chips com rolagem lateral, usada pelos 4 filtros.
// <T> = "qualquer tipo": serve para Assunto, FaixaDificuldade, StatusLicao e OrdemLicoes.
@Composable
private fun <T> LinhaDeChips(
    titulo: String,
    opcoes: List<T>,
    selecionada: T,
    texto: (T) -> String,
    onSelecionar: (T) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(text = titulo, color = DailyDevColors.TextSecondary, fontSize = 13.sp)
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            opcoes.forEach { opcao ->
                val marcado = opcao == selecionada
                FilterChip(
                    selected = marcado,
                    onClick = { onSelecionar(opcao) },
                    label = { Text(texto(opcao)) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = DailyDevColors.SurfaceCard,
                        labelColor = DailyDevColors.TextSecondary,
                        selectedContainerColor = DailyDevColors.PrimaryBlueContainer,
                        selectedLabelColor = DailyDevColors.TextPrimary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = marcado,
                        borderColor = DailyDevColors.BorderDefault,
                        selectedBorderColor = DailyDevColors.PrimaryBlue
                    )
                )
            }
        }
    }
}

@Composable
private fun LicaoItem(licao: Licao, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        color = DailyDevColors.SurfaceCard,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.5.dp, DailyDevColors.BorderDefault)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = licao.titulo,
                color = DailyDevColors.TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Nível ${licao.dificuldade}/10",
                color = DailyDevColors.TextSecondary,
                fontSize = 13.sp
            )
        }
    }
}
