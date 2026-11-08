package com.upchiapas.kt_template.presupuestovm.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PresupuestoViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PresupuestoUiState())
    val uiState: StateFlow<PresupuestoUiState> = _uiState.asStateFlow()

    fun onIngresoChange(nuevoIngreso: String) {
        _uiState.update { recalcular(it.copy(ingresoMensual = nuevoIngreso)) }
    }

    fun onMetaAhorroChange(nuevoPorcentaje: Float) {
        _uiState.update { recalcular(it.copy(metaAhorroPorcentaje = nuevoPorcentaje)) }
    }

    fun onGastosBasicosChange(nuevosBasicos: String) {
        _uiState.update { recalcular(it.copy(gastosBasicos = nuevosBasicos)) }
    }

    fun onGastosEstiloVidaChange(nuevosEstiloVida: String) {
        _uiState.update { recalcular(it.copy(gastosEstiloVida = nuevosEstiloVida)) }
    }

    private fun recalcular(state: PresupuestoUiState): PresupuestoUiState {
        val ingreso = state.ingresoMensual.toDoubleOrNull() ?: 0.0
        val basicos = state.gastosBasicos.toDoubleOrNull() ?: 0.0
        val estiloVida = state.gastosEstiloVida.toDoubleOrNull() ?: 0.0

        if (ingreso > 0) {
            val ratioAhorro = state.metaAhorroPorcentaje / 100.0
            val ratioBasicos = 0.50
            val ratioEstiloVida = (1.0 - ratioAhorro - ratioBasicos).coerceAtLeast(0.0)

            val objAhorro = ingreso * ratioAhorro
            val limBasicos = ingreso * ratioBasicos
            val limEstiloVida = ingreso * ratioEstiloVida

            val porcBasicos = if (limBasicos > 0) (basicos / limBasicos).toFloat() else 0f
            val porcEstiloVida = if (limEstiloVida > 0) (estiloVida / limEstiloVida).toFloat() else 0f

            val saldo = ingreso - basicos - estiloVida - objAhorro

            return state.copy(
                objetivoAhorro = objAhorro,
                limiteBasicos = limBasicos,
                limiteEstiloVida = limEstiloVida,
                porcentajeBasicosConsumido = porcBasicos,
                porcentajeEstiloVidaConsumido = porcEstiloVida,
                saldoRestante = saldo,
                estaCalculado = true
            )
        }
        return state.copy(estaCalculado = false)
    }
}