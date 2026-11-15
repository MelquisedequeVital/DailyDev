package com.pdm.dailydev.data

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Espera uma Task do Firebase terminar dentro de uma coroutine.
 * Sucesso devolve o resultado; falha lança a exceção (tratada no ViewModel).
 * Fica num arquivo próprio porque os dois repositórios do Firebase usam.
 */
internal suspend fun <T> Task<T>.awaitResult(): T =
    suspendCancellableCoroutine { continuation ->
        addOnSuccessListener { result ->
            continuation.resume(result)
        }
        addOnFailureListener { exception ->
            continuation.resumeWithException(exception)
        }
    }
