package com.pdm.dailydev.ui.telas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
    // Opções de assunto vêm do catálogo inteiro, para não sumirem ao filtrar
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Lições",
                color = DailyDevColors.TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            TextButton(
                onClick = { onFiltroChange(FiltroLicoes()) },
                enabled = filtro != FiltroLicoes()
            ) {
                Text("Limpar filtros")
            }
        }

        // null = "Todos" (primeira opção de cada filtro)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FiltroDropdown(
                titulo = "Assunto",
                opcoes = listOf(null) + assuntos,
                selecionada = assuntos.find { it.id == filtro.assuntoId },
                texto = { it?.nome ?: "Todos" },
                onSelecionar = { onFiltroChange(filtro.copy(assuntoId = it?.id)) },
                modifier = Modifier.weight(1f)
            )
            FiltroDropdown(
                titulo = "Dificuldade",
                opcoes = listOf(null) + FaixaDificuldade.entries,
                selecionada = filtro.faixa,
                texto = { faixa ->
                    if (faixa == null) "Todas" else "${faixa.titulo} ${faixa.niveis.first}–${faixa.niveis.last}"
                },
                onSelecionar = { onFiltroChange(filtro.copy(faixa = it)) },
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FiltroDropdown(
                titulo = "Status",
                opcoes = listOf(null) + StatusLicao.entries,
                selecionada = filtro.status,
                texto = { it?.titulo ?: "Todos" },
                onSelecionar = { onFiltroChange(filtro.copy(status = it)) },
                modifier = Modifier.weight(1f)
            )
            FiltroDropdown(
                titulo = "Ordenar",
                opcoes = OrdemLicoes.entries,
                selecionada = filtro.ordem,
                texto = { it.titulo },
                onSelecionar = { onFiltroChange(filtro.copy(ordem = it)) },
                modifier = Modifier.weight(1f)
            )
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

// Um filtro em formato de dropdown, usado pelos 4 filtros.
// <T> = "qualquer tipo": serve para Assunto, FaixaDificuldade, StatusLicao e OrdemLicoes.
@Composable
private fun <T> FiltroDropdown(
    titulo: String,
    opcoes: List<T>,
    selecionada: T,
    texto: (T) -> String,
    onSelecionar: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    // Guarda se o menu está aberto ou fechado
    var aberto by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Surface(
            onClick = { aberto = true },
            modifier = Modifier.fillMaxWidth(),
            color = DailyDevColors.SurfaceCard,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, DailyDevColors.BorderDefault)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = titulo, color = DailyDevColors.TextSecondary, fontSize = 11.sp)
                    Text(
                        text = texto(selecionada),
                        color = DailyDevColors.TextPrimary,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    tint = DailyDevColors.TextSecondary
                )
            }
        }

        DropdownMenu(expanded = aberto, onDismissRequest = { aberto = false }) {
            opcoes.forEach { opcao ->
                DropdownMenuItem(
                    text = { Text(texto(opcao)) },
                    onClick = {
                        onSelecionar(opcao)
                        aberto = false
                    }
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
