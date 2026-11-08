package com.upchiapas.kt_template.presupuestovm.presentation

data class PresupuestoUiState(
    val ingresoMensual: String = "",
    val metaAhorroPorcentaje: Float = 20f,
    val gastosBasicos: String = "",
    val gastosEstiloVida: String = "",

    val objetivoAhorro: Double = 0.0,
    val limiteBasicos: Double = 0.0,
    val limiteEstiloVida: Double = 0.0,

    val porcentajeBasicosConsumido: Float = 0f,
    val porcentajeEstiloVidaConsumido: Float = 0f,

    val saldoRestante: Double = 0.0,
    val estaCalculado: Boolean = false
)