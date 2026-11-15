package com.pdm.dailydev.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.pdm.dailydev.model.Tentativa
import java.util.UUID

/**
 * Guarda as tentativas em: usuarios/{uid}/tentativas/{idDaTentativa}
 *
 * O {uid} vem do login anônimo do Firebase: cada instalação do app ganha um ID,
 * sem precisar de tela de login. As regras do Firestore (firestore.rules) só
 * deixam cada aluno ler e gravar a própria pasta.
 */
class ProgressoRepositoryFirestore(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ProgressoRepository {

    override suspend fun carregarHistorico(usuarioId: UUID): List<Tentativa> {
        val snapshot = tentativasDoAluno()
            .orderBy("dataHoraMillis")
            .get()
            .awaitResult()

        return snapshot.documents.mapNotNull { doc ->
            documentoParaTentativa(doc, usuarioId)
        }
    }

    override suspend fun salvarTentativa(tentativa: Tentativa) {
        val dados = mapOf(
            "licaoId" to tentativa.licaoId.toString(),
            "acertos" to tentativa.acertos,
            "totalQuestoes" to tentativa.totalQuestoes,
            "dataHoraMillis" to tentativa.dataHoraMillis
        )
        // O ID da tentativa vira o ID do documento: salvar duas vezes não duplica
        tentativasDoAluno()
            .document(tentativa.id.toString())
            .set(dados)
            .awaitResult()
    }

    /** Faz o login anônimo (só na primeira vez) e devolve a coleção de tentativas do aluno. */
    private suspend fun tentativasDoAluno(): CollectionReference {
        val usuarioFirebase = auth.currentUser
            ?: auth.signInAnonymously().awaitResult().user
            ?: error("Não foi possível entrar no Firebase")

        return db.collection("usuarios")
            .document(usuarioFirebase.uid)
            .collection("tentativas")
    }

    private fun documentoParaTentativa(doc: DocumentSnapshot, usuarioId: UUID): Tentativa? {
        return try {
            Tentativa(
                id = UUID.fromString(doc.id),
                usuarioId = usuarioId,
                licaoId = UUID.fromString(doc.getString("licaoId")),
                acertos = doc.getLong("acertos")?.toInt() ?: return null,
                totalQuestoes = doc.getLong("totalQuestoes")?.toInt() ?: return null,
                dataHoraMillis = doc.getLong("dataHoraMillis") ?: return null
            )
        } catch (e: Exception) {
            null // documento inválido (ID quebrado ou valores fora do permitido): ignora
        }
    }
}
