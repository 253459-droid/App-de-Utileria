package com.upchiapas.kt_template.presupuestovm.presentation

data class PresupuestoUiState(
    val ingresoMensual: String = "",
    val montoAhorro: String = "",
    val gastosBasicos: String = "",
    val gastosPersonales: String = "",

    val porcentajeAhorro: Float = 0f,
    val porcentajeBasicos: Float = 0f,
    val porcentajePersonales: Float = 0f,
    val porcentajeSobrante: Float = 0f,

    val montoSobrante: Double = 0.0,
    val estaCalculado: Boolean = false
)