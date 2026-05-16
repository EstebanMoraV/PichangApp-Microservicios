package cl.duoc.pichangapp.ui.screens.events

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    navController: NavController,
    eventId: Int,
    viewModel: EventsViewModel = hiltViewModel()
) {
    val registrations by viewModel.registrations.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(eventId) {
        viewModel.loadRegistrations(eventId)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Asistencia") })
        }
    ) { paddingValues ->
        if (isLoading && registrations.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(registrations) { reg ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Usuario ID: ${reg.userId}", fontWeight = FontWeight.Bold)
                                    Text("Estado: ${reg.status}")
                                }
                                IconButton(
                                    onClick = { viewModel.markAttendance(eventId, reg.userId, true) },
                                    colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                                ) {
                                    Icon(Icons.Filled.Check, contentDescription = "Asistió")
                                }
                                IconButton(
                                    onClick = { viewModel.markAttendance(eventId, reg.userId, false) },
                                    colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.error)
                                ) {
                                    Icon(Icons.Filled.Close, contentDescription = "No Asistió")
                                }
                            }
                        }
                    }
                }
                
                Button(
                    onClick = { 
                        viewModel.finishEvent(eventId)
                        navController.popBackStack("events", inclusive = false)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Finalizar Evento")
                }
            }
        }
    }
}
