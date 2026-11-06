package com.example.myappcontroldenotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myappcontroldenotas.ui.theme.MyAppControlDeNotasTheme
import java.util.Locale

data class PercentageScheme(
    val name: String,
    val p1: Double,
    val p2: Double,
    val p3: Double,
    val p4: Double
)

data class StudentRecord(
    val name: String,
    val finalGrade: Double,
    val passed: Boolean
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppControlDeNotasTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ControlDeNotasScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ControlDeNotasScreen(modifier: Modifier = Modifier) {
    // Definición de esquemas de porcentaje para RadioButtons
    val schemes = listOf(
        PercentageScheme("Esquema 1 (20%, 20%, 30%, 30%)", 0.20, 0.20, 0.30, 0.30),
        PercentageScheme("Esquema 2 (10%, 20%, 30%, 40%)", 0.10, 0.20, 0.30, 0.40),
        PercentageScheme("Esquema 3 (25%, 25%, 25%, 25%)", 0.25, 0.25, 0.25, 0.25)
    )

    // Estados de la aplicación (EditText / Inputs)
    var nombreEstudiante by remember { mutableStateOf("") }
    var nota1 by remember { mutableStateOf("") }
    var nota2 by remember { mutableStateOf("") }
    var nota3 by remember { mutableStateOf("") }
    var nota4 by remember { mutableStateOf("") }

    // Estado RadioButton
    var selectedScheme by remember { mutableStateOf(schemes[0]) }

    // Estados CheckBox
    var applyBonus by remember { mutableStateOf(false) }
    var showBreakdown by remember { mutableStateOf(true) }

    // Estados de mensajes y resultados
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var lastResultText by remember { mutableStateOf<String?>(null) }
    var breakdownText by remember { mutableStateOf<String?>(null) }

    // Lista acumulada de estudiantes del grupo
    val studentGroup = remember { mutableStateListOf<StudentRecord>() }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Título Principal (TextView)
        Text(
            text = "Control de Notas",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Text(
            text = "Registro y cálculo de notas definitivas del grupo",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Divider()

        // Card de Ingreso de Datos (EditText)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "1. Ingreso de Estudiante y Notas (0.0 a 5.0)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                // EditText: Nombre del estudiante
                OutlinedTextField(
                    value = nombreEstudiante,
                    onValueChange = { nombreEstudiante = it },
                    label = { Text("Nombre del Estudiante (Opcional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // EditText: Nota 1
                    OutlinedTextField(
                        value = nota1,
                        onValueChange = { nota1 = it },
                        label = { Text("Nota 1") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )

                    // EditText: Nota 2
                    OutlinedTextField(
                        value = nota2,
                        onValueChange = { nota2 = it },
                        label = { Text("Nota 2") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // EditText: Nota 3
                    OutlinedTextField(
                        value = nota3,
                        onValueChange = { nota3 = it },
                        label = { Text("Nota 3") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )

                    // EditText: Nota 4
                    OutlinedTextField(
                        value = nota4,
                        onValueChange = { nota4 = it },
                        label = { Text("Nota 4") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Card de Configuración de Porcentajes ()
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "2. Selección de Porcentajes ",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Column(Modifier.selectableGroup()) {
                    schemes.forEach { scheme ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .selectable(
                                    selected = (scheme == selectedScheme),
                                    onClick = { selectedScheme = scheme },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (scheme == selectedScheme),
                                onClick = null // Manejado por la fila
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = scheme.name, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }

        // Card de Opciones Adicionales (CheckBox)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "3. Opciones Adicionales " +
                            "",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = applyBonus,
                        onCheckedChange = { applyBonus = it }
                    )
                    Text(
                        text = "Aplicar bonificación por asistencia (+0.2 a nota final)",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = showBreakdown,
                        onCheckedChange = { showBreakdown = it }
                    )
                    Text(
                        text = "Mostrar desglose del porcentaje obtenido por cada nota",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // Mensaje de Error (si existe)
        errorMessage?.let { error ->
            Surface(
                color = MaterialTheme.colorScheme.errorContainer,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // Botones de Acción (Button)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Button: Calcular y Registrar
            Button(
                onClick = {
                    errorMessage = null

                    val n1 = nota1.replace(',', '.').toDoubleOrNull()
                    val n2 = nota2.replace(',', '.').toDoubleOrNull()
                    val n3 = nota3.replace(',', '.').toDoubleOrNull()
                    val n4 = nota4.replace(',', '.').toDoubleOrNull()

                    if (n1 == null || n2 == null || n3 == null || n4 == null) {
                        errorMessage = "Por favor ingrese las 4 notas con formato numérico válido."
                        return@Button
                    }

                    if (n1 !in 0.0..5.0 || n2 !in 0.0..5.0 || n3 !in 0.0..5.0 || n4 !in 0.0..5.0) {
                        errorMessage = "Cada nota debe estar comprendida estrictamente en el rango de 0.0 a 5.0."
                        return@Button
                    }

                    // Cálculo del porcentaje correspondiente de cada nota
                    val pNota1 = n1 * selectedScheme.p1
                    val pNota2 = n2 * selectedScheme.p2
                    val pNota3 = n3 * selectedScheme.p3
                    val pNota4 = n4 * selectedScheme.p4

                    // Acumulación de porcentajes para nota definitiva
                    var notaDefinitiva = pNota1 + pNota2 + pNota3 + pNota4

                    if (applyBonus) {
                        notaDefinitiva = (notaDefinitiva + 0.2).coerceAtMost(5.0)
                    }

                    val aprueba = notaDefinitiva >= 3.0
                    val nombreFormat = if (nombreEstudiante.isBlank()) "Estudiante #${studentGroup.size + 1}" else nombreEstudiante.trim()

                    val record = StudentRecord(nombreFormat, notaDefinitiva, aprueba)
                    studentGroup.add(record)

                    val estadoStr = if (aprueba) "APROBÓ LA MATERIA" else "PERDIÓ LA MATERIA"

                    lastResultText = String.format(
                        Locale.getDefault(),
                        "Resultado de %s:\nNota Definitiva: %.2f - Estado: %s",
                        nombreFormat,
                        notaDefinitiva,
                        estadoStr
                    )

                    breakdownText = String.format(
                        Locale.getDefault(),
                        "Acumulados por Porcentaje:\n• Nota 1 (%.0f%%): %.2f pts\n• Nota 2 (%.0f%%): %.2f pts\n• Nota 3 (%.0f%%): %.2f pts\n• Nota 4 (%.0f%%): %.2f pts%s",
                        selectedScheme.p1 * 100, pNota1,
                        selectedScheme.p2 * 100, pNota2,
                        selectedScheme.p3 * 100, pNota3,
                        selectedScheme.p4 * 100, pNota4,
                        if (applyBonus) "\n• Bonificación asistencia: +0.20 pts" else ""
                    )

                    // Limpiar entradas después de registrar
                    nombreEstudiante = ""
                    nota1 = ""
                    nota2 = ""
                    nota3 = ""
                    nota4 = ""
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Calcular Nota")
            }

            // Button: Limpiar Campos
            OutlinedButton(
                onClick = {
                    nombreEstudiante = ""
                    nota1 = ""
                    nota2 = ""
                    nota3 = ""
                    nota4 = ""
                    errorMessage = null
                }
            ) {
                Text("Limpiar")
            }
        }

        // Resultados del Estudiante Actual (TextView)
        lastResultText?.let { result ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = result,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    if (showBreakdown && breakdownText != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = breakdownText!!,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }

        Divider()

        // Resumen Final del Grupo de Estudiantes (TextView)
        val totalEstudiantes = studentGroup.size
        val perdieronMateria = studentGroup.count { !it.passed }
        val aprobaronMateria = studentGroup.count { it.passed }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (perdieronMateria > 0) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f) else MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Resumen del Grupo Evaluado",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "• Total estudiantes registrados: $totalEstudiantes",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "• Estudiantes que aprobaron: $aprobaronMateria",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.SemiBold
                )

                // Requisito clave: Indicar cuántos estudiantes perdieron la materia
                Text(
                    text = "• Estudiantes que PERDIERON LA MATERIA: $perdieronMateria",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold
                )

                if (totalEstudiantes > 0) {
                    val promedioGrupo = studentGroup.map { it.finalGrade }.average()
                    Text(
                        text = String.format(Locale.getDefault(), "• Promedio del grupo: %.2f", promedioGrupo),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Historial de registros:",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )

                    studentGroup.forEachIndexed { index, student ->
                        val estado = if (student.passed) "Aprobó" else "REPROBÓ"
                        Text(
                            text = "${index + 1}. ${student.name} - Nota: ${String.format(Locale.getDefault(), "%.2f", student.finalGrade)} ($estado)",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Button: Reiniciar Estadísticas del Grupo
                OutlinedButton(
                    onClick = {
                        studentGroup.clear()
                        lastResultText = null
                        breakdownText = null
                        errorMessage = null
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Reiniciar Grupo")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ControlDeNotasScreenPreview() {
    MyAppControlDeNotasTheme {
        ControlDeNotasScreen()
    }
}
