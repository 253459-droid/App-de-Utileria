package com.upchiapas.kt_template.presupuestovm.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PresupuestoScreen(viewModel: PresupuestoViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    PresupuestoContent(
        state = state,
        onIngresoChange = viewModel::onIngresoChange,
        onMetaAhorroChange = viewModel::onMetaAhorroChange,
        onGastosBasicosChange = viewModel::onGastosBasicosChange,
        onGastosEstiloVidaChange = viewModel::onGastosEstiloVidaChange
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PresupuestoContent(
    state: PresupuestoUiState,
    onIngresoChange: (String) -> Unit,
    onMetaAhorroChange: (Float) -> Unit,
    onGastosBasicosChange: (String) -> Unit,
    onGastosEstiloVidaChange: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Simulador de Presupuesto", style = MaterialTheme.typography.headlineMedium)
        }

        item {
            OutlinedTextField(
                value = state.ingresoMensual,
                onValueChange = onIngresoChange,
                label = { Text("Ingreso Mensual Total ($)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        item {
            Text("Meta de Ahorro: ${state.metaAhorroPorcentaje.toInt()}%", style = MaterialTheme.typography.labelLarge)
            Slider(
                value = state.metaAhorroPorcentaje,
                onValueChange = onMetaAhorroChange,
                valueRange = 0f..50f,
                steps = 9
            )
        }

        if (state.estaCalculado) {
            item {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                        Text("Ahorro Objetivo: $${String.format("%.2f", state.objetivoAhorro)}")
                        Text("Presupuesto Libre: $${String.format("%.2f", state.saldoRestante)}")
                    }
                }
            }

            item {
                Text("Ingresa tus gastos reales:", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = state.gastosBasicos,
                    onValueChange = onGastosBasicosChange,
                    label = { Text("Gastos Básicos (Renta, Luz, etc.)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                BarraGasto(
                    etiqueta = "Límite: $${String.format("%.2f", state.limiteBasicos)}",
                    progreso = state.porcentajeBasicosConsumido
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = state.gastosEstiloVida,
                    onValueChange = onGastosEstiloVidaChange,
                    label = { Text("Estilo de Vida (Salidas, Ocio)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                BarraGasto(
                    etiqueta = "Límite: $${String.format("%.2f", state.limiteEstiloVida)}",
                    progreso = state.porcentajeEstiloVidaConsumido
                )
            }
        }
    }
}

@Composable
fun BarraGasto(etiqueta: String, progreso: Float) {
    val progresoAnimado by animateFloatAsState(targetValue = progreso.coerceIn(0f, 1f), label = "progreso")

    val colorBarra = when {
        progreso > 1f -> Color.Red
        progreso > 0.85f -> Color.Yellow
        else -> Color.Green
    }

    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(etiqueta, style = MaterialTheme.typography.bodySmall)
        LinearProgressIndicator(
            progress = { progresoAnimado },
            modifier = Modifier.fillMaxWidth().height(8.dp),
            color = colorBarra,
            trackColor = colorBarra.copy(alpha = 0.2f)
        )
    }
}