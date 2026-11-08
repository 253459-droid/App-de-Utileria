package com.upchiapas.kt_template.presupuestovm.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PresupuestoViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PresupuestoUiState())
    val uiState: StateFlow<PresupuestoUiState> = _uiState.asStateFlow()

    fun onIngresoChange(nuevoValor: String) { _uiState.update { recalcular(it.copy(ingresoMensual = nuevoValor)) } }
    fun onAhorroChange(nuevoValor: String) { _uiState.update { recalcular(it.copy(montoAhorro = nuevoValor)) } }
    fun onGastosBasicosChange(nuevoValor: String) { _uiState.update { recalcular(it.copy(gastosBasicos = nuevoValor)) } }
    fun onGastosPersonalesChange(nuevoValor: String) { _uiState.update { recalcular(it.copy(gastosPersonales = nuevoValor)) } }

    private fun recalcular(state: PresupuestoUiState): PresupuestoUiState {
        val ingreso = state.ingresoMensual.toDoubleOrNull() ?: 0.0
        val ahorro = state.montoAhorro.toDoubleOrNull() ?: 0.0
        val basicos = state.gastosBasicos.toDoubleOrNull() ?: 0.0
        val personales = state.gastosPersonales.toDoubleOrNull() ?: 0.0

        if (ingreso > 0) {
            val sobrante = ingreso - ahorro - basicos - personales
            val sobranteSeguro = sobrante.coerceAtLeast(0.0)

            val pAhorro = ((ahorro / ingreso) * 100).toFloat()
            val pBasicos = ((basicos / ingreso) * 100).toFloat()
            val pPersonales = ((personales / ingreso) * 100).toFloat()
            val pSobrante = ((sobranteSeguro / ingreso) * 100).toFloat()

            return state.copy(
                porcentajeAhorro = pAhorro,
                porcentajeBasicos = pBasicos,
                porcentajePersonales = pPersonales,
                porcentajeSobrante = pSobrante,
                montoSobrante = sobrante,
                estaCalculado = true
            )
        }
        return state.copy(estaCalculado = false)
    }
}