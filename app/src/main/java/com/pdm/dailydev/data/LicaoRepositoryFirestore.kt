package com.pdm.dailydev.data

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.pdm.dailydev.model.Assunto
import com.pdm.dailydev.model.Licao
import com.pdm.dailydev.model.Questao
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LicaoRepositoryFirestore(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : LicaoRepository {

    private val assuntosCollection = db.collection("assuntos")
    private val licoesCollection = db.collection("licoes")
    private val seedMutex = Mutex()

    override suspend fun obterTodosAssuntos(): List<Assunto> {
        popularBancoSeVazio()
        val snapshot = assuntosCollection.get().awaitResult()
        return snapshot.documents.mapNotNull { doc ->
            documentoParaAssunto(doc)
        }
    }

    override suspend fun obterTodasLicoes(): List<Licao> {
        popularBancoSeVazio()
        val snapshot = licoesCollection.get().awaitResult()
        return snapshot.documents.mapNotNull { doc ->
            documentoParaLicao(doc)
        }
    }

    override suspend fun obterLicoesPorAssunto(assuntoId: UUID): List<Licao> {
        popularBancoSeVazio()
        val snapshot = licoesCollection
            .whereEqualTo("assuntoId", assuntoId.toString())
            .get()
            .awaitResult()

        return snapshot.documents.mapNotNull { doc ->
            documentoParaLicao(doc)
        }
    }

    override suspend fun obterLicaoPorId(id: UUID): Licao? {
        popularBancoSeVazio()
        val doc = licoesCollection.document(id.toString()).get().awaitResult()
        return if (doc.exists()) documentoParaLicao(doc) else null
    }

    /**
     * Envia os assuntos e lições iniciais para o Firestore caso o banco ainda esteja vazio.
     */
    private suspend fun popularBancoSeVazio() {
        seedMutex.withLock {
            val verificacao = assuntosCollection.limit(1).get().awaitResult()
            if (!verificacao.isEmpty) return

            val dadosIniciais = LicaoRepositoryLocal()
            val assuntos = dadosIniciais.obterTodosAssuntos()
            val licoes = dadosIniciais.obterTodasLicoes()

            val batch = db.batch()

            for (assunto in assuntos) {
                val docRef = assuntosCollection.document(assunto.id.toString())
                val dadosAssunto = mapOf(
                    "id" to assunto.id.toString(),
                    "nome" to assunto.nome
                )
                batch.set(docRef, dadosAssunto)
            }

            for (licao in licoes) {
                val docRef = licoesCollection.document(licao.id.toString())
                val questoesMap = licao.questoes.map { questao ->
                    mapOf(
                        "enunciado" to questao.enunciado,
                        "alternativas" to questao.alternativas,
                        "indiceCorreto" to questao.indiceCorreto
                    )
                }
                val dadosLicao = mapOf(
                    "id" to licao.id.toString(),
                    "titulo" to licao.titulo,
                    "textoExplicacao" to licao.textoExplicacao,
                    "dificuldade" to licao.dificuldade,
                    "assuntoId" to licao.assunto.id.toString(),
                    "assuntoNome" to licao.assunto.nome,
                    "questoes" to questoesMap
                )
                batch.set(docRef, dadosLicao)
            }

            batch.commit().awaitResult()
        }
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

    private suspend fun <T> Task<T>.awaitResult(): T =
        suspendCancellableCoroutine { continuation ->
            addOnSuccessListener { result ->
                continuation.resume(result)
            }
            addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
        }
}