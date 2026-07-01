package com.project.hjcperformance.model

sealed class PremiumStatus {
    object Premium : PremiumStatus()
    object Free : PremiumStatus()
    object NotAuthenticated : PremiumStatus()
    data class Error(val theException: Exception) : PremiumStatus()
}

data class User(
    val uid: String = "",
    val nome: String = "",
    val email: String = "",
    @field:JvmField 
    val isPremium: Boolean = false,
    val idiomaPref: String = "pt"
)

data class Cliente(
    val id: String = "",
    val idMecanico: String = "",
    val nome: String = "",
    val telefone: String = "",
    val email: String = "",
    val fotoUrl: String = ""
)

data class Pedido(
    val id: String = "",
    val idMecanico: String = "",
    val matriculaAutomovel: String = "",
    val marcaAutomovel: String = "",
    val modeloAutomovel: String = "",
    val idCliente: String = "",
    val descricaoProblema: String = "",
    val orcamentoEstimado: Double = 0.0,
    val estado: String = "Em Aberto"
)