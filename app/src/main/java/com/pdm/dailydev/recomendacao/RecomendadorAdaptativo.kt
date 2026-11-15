package com.pdm.dailydev.recomendacao

import com.pdm.dailydev.model.Licao
import com.pdm.dailydev.model.NivelUsuario
import com.pdm.dailydev.model.Usuario
import kotlin.math.abs

class RecomendadorAdaptativo(
    private val limiarDominio: Double = 0.7,
    private val maxTentativasPorLicao: Int = 3
) : MotorRecomendacao {

    override fun recomendarProxima(usuario: Usuario, licoesDisponiveis: List<Licao>): Licao? {
        if (licoesDisponiveis.isEmpty()) return null

        val tentativasPorLicao = usuario.historico.groupBy { it.licaoId }

        // Pega os IDs das últimas 3 lições feitas para evitar alternância em loop (A -> B -> A -> B)
        val recentesIds = usuario.historico
            .takeLast(3)
            .map { it.licaoId }
            .toSet()

        // Pré-calcula aproveitamento por assunto uma única vez
        val mediaPorAssunto = licoesDisponiveis
            .map { it.assunto }
            .distinct()
            .associateWith { assunto -> usuario.aproveitamentoPorAssunto(assunto, licoesDisponiveis) }

        // Candidatas: inéditas OU feitas menos de 3 vezes sem atingir 70% na última tentativa
        val candidatas = licoesDisponiveis.filter { licao ->
            val historicoLicao = tentativasPorLicao[licao.id].orEmpty()
            if (historicoLicao.isEmpty()) return@filter true

            val ultimaNota = historicoLicao.maxBy { it.dataHoraMillis }.aproveitamento
            ultimaNota < limiarDominio && historicoLicao.size < maxTentativasPorLicao
        }

        if (candidatas.isEmpty()) return null

        return candidatas.maxByOrNull { licao ->
            val historicoLicao = tentativasPorLicao[licao.id].orEmpty()
            val ultimaNota = historicoLicao.maxByOrNull { it.dataHoraMillis }?.aproveitamento

            calcularScore(
                licao = licao,
                usuario = usuario,
                ultimaNotaLicao = ultimaNota,
                mediaNoAssunto = mediaPorAssunto[licao.assunto],
                feitaRecentemente = licao.id in recentesIds
            )
        }
    }

    private fun calcularScore(
        licao: Licao,
        usuario: Usuario,
        ultimaNotaLicao: Double?,
        mediaNoAssunto: Double?,
        feitaRecentemente: Boolean
    ): Double {
        var score = 0.0

        // Fator A: Lições inéditas (+45 pts) vêm antes de revisões (até +35 pts)
        score += if (ultimaNotaLicao == null) {
            45.0
        } else {
            (1.0 - ultimaNotaLicao) * 35.0
        }

        // Fator B: Desempenho histórico no Assunto (até +25 pts)
        score += if (mediaNoAssunto != null) {
            (1.0 - mediaNoAssunto) * 25.0
        } else {
            15.0 // Incentivo para explorar assuntos novos
        }

        // Fator C: Aderência ao nível atual do usuário (até +20 pts)
        val dificuldadeAlvo = when (usuario.nivel) {
            NivelUsuario.INICIANTE -> 2
            NivelUsuario.INTERMEDIARIO -> 4
            NivelUsuario.AVANCADO -> 6
        }
        val distanciaDificuldade = abs(licao.dificuldade - dificuldadeAlvo)
        score += (20.0 - (distanciaDificuldade * 4.0)).coerceAtLeast(0.0)

        // Fator D: Penalidade forte se esteve entre as últimas 3 lições feitas
        if (feitaRecentemente) {
            score -= 60.0
        }

        return score
    }
}