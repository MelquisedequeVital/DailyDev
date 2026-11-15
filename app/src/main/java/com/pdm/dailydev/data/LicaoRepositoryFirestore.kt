package com.pdm.dailydev.data

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.pdm.dailydev.model.Assunto
import com.pdm.dailydev.model.Licao
import com.pdm.dailydev.model.Questao
import java.util.UUID

class LicaoRepositoryFirestore(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : LicaoRepository {

    private val assuntosCollection = db.collection("assuntos")
    private val licoesCollection = db.collection("licoes")

    override suspend fun obterTodosAssuntos(): List<Assunto> {
        val snapshot = assuntosCollection.get().awaitResult()
        return snapshot.documents.mapNotNull { doc ->
            documentoParaAssunto(doc)
        }
    }

    override suspend fun obterTodasLicoes(): List<Licao> {
        val snapshot = licoesCollection.get().awaitResult()
        return snapshot.documents.mapNotNull { doc ->
            documentoParaLicao(doc)
        }
    }

    override suspend fun obterLicoesPorAssunto(assuntoId: UUID): List<Licao> {
        val snapshot = licoesCollection
            .whereEqualTo("assuntoId", assuntoId.toString())
            .get()
            .awaitResult()

        return snapshot.documents.mapNotNull { doc ->
            documentoParaLicao(doc)
        }
    }

    override suspend fun obterLicaoPorId(id: UUID): Licao? {
        val doc = licoesCollection.document(id.toString()).get().awaitResult()
        return if (doc.exists()) documentoParaLicao(doc) else null
    }

    private fun documentoParaAssunto(doc: DocumentSnapshot): Assunto? {
        val idStr = doc.getString("id") ?: doc.id
        val nome = doc.getString("nome") ?: return null
        return try {
            Assunto(
                id = UUID.fromString(idStr),
                nome = nome
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun documentoParaLicao(doc: DocumentSnapshot): Licao? {
        val idStr = doc.getString("id") ?: doc.id
        val titulo = doc.getString("titulo") ?: return null
        val textoExplicacao = doc.getString("textoExplicacao") ?: ""
        val dificuldade = (doc.getLong("dificuldade") ?: 1L).toInt().coerceIn(1, 10)
        val assuntoIdStr = doc.getString("assuntoId") ?: return null
        val assuntoNome = doc.getString("assuntoNome") ?: "Geral"

        val questoesRaw = doc.get("questoes") as? List<*> ?: return null
        val questoes = questoesRaw.mapNotNull { item ->
            val map = item as? Map<*, *> ?: return@mapNotNull null
            val enunciado = map["enunciado"] as? String ?: return@mapNotNull null
            val alternativas = (map["alternativas"] as? List<*>)
                ?.mapNotNull { it as? String }
                ?: return@mapNotNull null
            val indiceCorreto = (map["indiceCorreto"] as? Number)?.toInt() ?: return@mapNotNull null

            if (alternativas.size < 2 || indiceCorreto !in alternativas.indices) {
                return@mapNotNull null
            }

            Questao(
                enunciado = enunciado,
                alternativas = alternativas,
                indiceCorreto = indiceCorreto
            )
        }

        if (questoes.isEmpty()) return null

        return try {
            Licao(
                id = UUID.fromString(idStr),
                titulo = titulo,
                textoExplicacao = textoExplicacao,
                questoes = questoes,
                assunto = Assunto(
                    id = UUID.fromString(assuntoIdStr),
                    nome = assuntoNome
                ),
                dificuldade = dificuldade
            )
        } catch (e: Exception) {
            null
        }
    }
}
