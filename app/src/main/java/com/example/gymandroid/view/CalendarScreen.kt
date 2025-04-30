package com.example.gymandroid.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.gymandroid.model.dummypayday
import com.example.gymandroid.model.payday
import com.example.gymandroid.ui.theme.AppTheme

import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import kotlin.collections.filter
import kotlin.collections.forEach
import kotlin.collections.isNotEmpty
import kotlin.let
import kotlin.ranges.until

@Composable
fun CalendarView(

    onDateSelected: (LocalDate) -> Unit,
    selectedDate: LocalDate = LocalDate.now()
) {
    // Estado para el mes actual mostrado (puede ser diferente al mes de la fecha seleccionada)
    var currentMonth by remember { mutableStateOf(YearMonth.from(selectedDate)) }
    var appointments = remember { dummypayday }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Encabezado con controles de navegación
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón para mes anterior
            Box(
                modifier = Modifier
                    .clickable {
                        currentMonth = currentMonth.minusMonths(1)
                    }
                    .padding(8.dp)
            ) {
                Text("◀", fontSize = 20.sp)
            }

            // Mes y año con selector
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    // Aquí podrías implementar un selector de mes/año más avanzado
                }
            ) {
                Text(
                    text = currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + currentMonth.year,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Text("▼", fontSize = 14.sp, color = Color.Gray)
            }

            // Botón para mes siguiente
            Box(
                modifier = Modifier
                    .clickable {
                        currentMonth = currentMonth.plusMonths(1)
                    }
                    .padding(8.dp)
            ) {
                Text("▶", fontSize = 20.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Días de la semana
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            listOf("LUN", "MAR", "MIÉ", "JUE", "VIE", "SÁB", "DOM").forEach { day ->
                Text(
                    text = day,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.width(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Días del mes actual
        MonthCalendar(
            yearMonth = currentMonth,
            appointments = appointments,
            selectedDate = selectedDate,
            onDateSelected = onDateSelected
        )

        // Evento destacado
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Calendario Cliente",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Lista de citas

        Spacer(modifier = Modifier.height(16.dp))
        AppointmentList(
            appointments = appointments, selectedDate = selectedDate,

        )


    }
}

@Composable
fun MonthCalendar(
    yearMonth: YearMonth,
    appointments: List<payday>,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val daysInMonth = remember(yearMonth) { yearMonth.lengthOfMonth() }
    val firstDayOfMonth = remember(yearMonth) { yearMonth.atDay(1) }
    val firstDayOfWeek = remember { firstDayOfMonth.dayOfWeek.value % 7 } // Ajuste para que LUN=0

    Column {
        // Filas de semanas
        val totalCells = daysInMonth + firstDayOfWeek
        val weeks = (totalCells + 6) / 7

        for (week in 0 until weeks) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (day in 0 until 7) {
                    val cellIndex = week * 7 + day
                    if (cellIndex < firstDayOfWeek || cellIndex >= firstDayOfWeek + daysInMonth) {
                        // Días fuera del mes actual
                        Box(modifier = Modifier.size(32.dp))
                    } else {
                        val dayOfMonth = cellIndex - firstDayOfWeek + 1
                        val date = yearMonth.atDay(dayOfMonth)
                        val isSelected = date == selectedDate
                        val dayAppointments = appointments.filter { it.date == date }



                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { onDateSelected(date) }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(
                                        if (isSelected) Color.Blue else Color.Transparent,
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = dayOfMonth.toString(),
                                    color = if (isSelected) Color.White else Color.Gray,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }


                            // Indicador de citas
                            if (dayAppointments.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Box(
                                    modifier = Modifier
                                        .size(4.dp)
                                        .clip(CircleShape)
                                        .background(Color.Red)
                                )
                            }




                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Composable
fun AppointmentList(
    appointments: List<payday>,
    selectedDate: LocalDate,

    ) {
    val filteredAppointments = appointments.filter { it.date == selectedDate }
    var selectedAppointment by remember { mutableStateOf<payday?>(null) }

    // Diálogo de detalle
    selectedAppointment?.let { appointment ->
        AppointmentDetailDialog(
            appointment = appointment,
            onDismiss = { selectedAppointment = null },

        )
    }

    if (filteredAppointments.isEmpty()) {
        Text(
            text = "Sin eventos para la fecha seleccionada.",
            color = Color.Gray,
            modifier = Modifier.padding(16.dp)
        )
    } else {
        LazyColumn {
            items(filteredAppointments) { appointment ->
                AppointmentCard(
                    appointment = appointment,
                    onClick = { selectedAppointment = appointment }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentDetailDialog(
    appointment: payday,
    onDismiss: () -> Unit,

) {
    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Proxima fecha de pago:",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Información detallada
                DetailItem("Fecha:", "${appointment.date.dayOfMonth}/${appointment.date.monthValue}/${appointment.date.year}")


                Spacer(modifier = Modifier.height(24.dp))

                // Botones de acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp)

                    ) {
                        Text("Enviar mensaje")
                    }

                }
            }
        }
    }
}

@Composable
private fun DetailItem(label: String, value: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(120.dp)
        )
        Text(text = value)
    }
}

@Composable
fun AppointmentCard(
    appointment: payday,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Pago de suscripción", fontWeight = FontWeight.Bold)
            Text("Mas informacion...")

        }
    }
}

