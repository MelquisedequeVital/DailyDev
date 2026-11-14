package com.pdm.dailydev.model

import java.util.UUID

data class Assunto(
    val id: UUID = UUID.randomUUID(),
    val nome: String
)