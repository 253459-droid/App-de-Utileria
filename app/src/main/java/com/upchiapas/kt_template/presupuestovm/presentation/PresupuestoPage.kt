package com.upchiapas.kt_template.presupuestovm.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.upchiapas.kt_template.R
import androidx.compose.ui.text.input.ImeAction

val ColorAhorro = Color(0xFF4ADE80)
val ColorBasicos = Color(0xFFFACC15)
val ColorPersonales = Color(0xFFFB923C)
val ColorSobrante = Color(0xFF60A5FA)

@Composable
fun PresupuestoScreen(viewModel: PresupuestoViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    PresupuestoContent(
        state = state,
        onIngresoChange = viewModel::onIngresoChange,
        onAhorroChange = viewModel::onAhorroChange,
        onGastosBasicosChange = viewModel::onGastosBasicosChange,
        onGastosPersonalesChange = viewModel::onGastosPersonalesChange
    )
}

@Composable
fun PresupuestoContent(
    state: PresupuestoUiState,
    onIngresoChange: (String) -> Unit,
    onAhorroChange: (String) -> Unit,
    onGastosBasicosChange: (String) -> Unit,
    onGastosPersonalesChange: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = stringResource(id = R.string.titulo_presupuesto),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item { CustomTextField(state.ingresoMensual, onIngresoChange, R.string.ingreso_mensual) }
        item { CustomTextField(state.montoAhorro, onAhorroChange, R.string.cuanto_ahorrar) }
        item { CustomTextField(state.gastosBasicos, onGastosBasicosChange, R.string.gastos_basicos) }
        item {
            CustomTextField(
                value = state.gastosPersonales,
                onValueChange = onGastosPersonalesChange,
                labelRes = R.string.gastos_personales,
                imeAction = ImeAction.Done
            )
        }

        if (state.estaCalculado) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(stringResource(id = R.string.grafica_presupuesto), style = MaterialTheme.typography.titleMedium)
                            Text(stringResource(id = R.string.cien_por_ciento), style = MaterialTheme.typography.titleMedium)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(24.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surface)
                        ) {
                            if (state.porcentajeAhorro > 0f) Box(Modifier.weight(state.porcentajeAhorro).fillMaxHeight().background(ColorAhorro))
                            if (state.porcentajeBasicos > 0f) Box(Modifier.weight(state.porcentajeBasicos).fillMaxHeight().background(ColorBasicos))
                            if (state.porcentajePersonales > 0f) Box(Modifier.weight(state.porcentajePersonales).fillMaxHeight().background(ColorPersonales))
                            if (state.porcentajeSobrante > 0f) Box(Modifier.weight(state.porcentajeSobrante).fillMaxHeight().background(ColorSobrante))
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                LegendItem(
                                    ColorAhorro,
                                    stringResource(R.string.leyenda_ahorro, state.porcentajeAhorro.toInt(), state.montoAhorro.toDoubleOrNull() ?: 0.0)
                                )
                                LegendItem(
                                    ColorPersonales,
                                    stringResource(R.string.leyenda_personales, state.porcentajePersonales.toInt(), state.gastosPersonales.toDoubleOrNull() ?: 0.0)
                                )
                            }
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                LegendItem(
                                    ColorBasicos,
                                    stringResource(R.string.leyenda_basicos, state.porcentajeBasicos.toInt(), state.gastosBasicos.toDoubleOrNull() ?: 0.0)
                                )
                                LegendItem(
                                    ColorSobrante,
                                    stringResource(R.string.leyenda_sobrante, state.porcentajeSobrante.toInt(), state.montoSobrante)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelRes: Int,
    imeAction: ImeAction = ImeAction.Next
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(id = labelRes)) },
        prefix = { Text("$ ") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = imeAction        ),
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun LegendItem(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(color))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}