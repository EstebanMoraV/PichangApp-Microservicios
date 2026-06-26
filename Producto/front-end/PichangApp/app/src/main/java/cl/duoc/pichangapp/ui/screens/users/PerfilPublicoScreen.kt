package cl.duoc.pichangapp.ui.screens.users

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import cl.duoc.pichangapp.data.model.KarmaHistoryDto
import cl.duoc.pichangapp.ui.components.Avatar
import cl.duoc.pichangapp.ui.components.CategoryChip
import cl.duoc.pichangapp.ui.components.EmptyState
import cl.duoc.pichangapp.ui.components.LoadingScreen
import cl.duoc.pichangapp.ui.components.PichangTopBar
import cl.duoc.pichangapp.ui.components.karmaColor

@Composable
fun PerfilPublicoScreen(
    navController: NavController,
    nombre: String,
    apellido: String,
    viewModel: PerfilPublicoViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(nombre, apellido) { viewModel.load(nombre, apellido) }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        PichangTopBar(title = "Perfil", onNavigateBack = { navController.popBackStack() })

        when {
            state.isLoading -> LoadingScreen()
            state.error != null || state.perfil == null ->
                EmptyState(emoji = "🤷", title = "Perfil no disponible", message = state.error ?: "No se encontró el perfil")
            else -> {
                val perfil = state.perfil!!
                val fullName = "${perfil.nombre.orEmpty()} ${perfil.apellido.orEmpty()}".trim()
                val categoria = perfil.categoriaKarma.orEmpty().ifBlank { "Sin categoría" }
                val color = karmaColor(categoria)

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(24.dp))
                    Avatar(name = fullName, size = 120.dp)
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = fullName,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(12.dp))

                    // ── Tarjeta de karma ──────────────────────────────────────
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        Column(
                            Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${perfil.karmaScore}",
                                style = MaterialTheme.typography.displayMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = color
                            )
                            Text(
                                text = "puntos de karma",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(12.dp))
                            CategoryChip(label = categoria, color = color, filled = true)
                        }
                    }

                    // ── Historial de karma ────────────────────────────────────
                    // Solo se muestra (título incluido) si el usuario tiene el historial visible.
                    if (perfil.historialVisible) {
                        Spacer(Modifier.height(24.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Filled.Schedule,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "Historial de eventos",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        Spacer(Modifier.height(12.dp))

                        if (perfil.history.isEmpty()) {
                            Text(
                                text = "Este jugador aún no tiene movimientos de karma.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp)
                            )
                        } else {
                            // Lista acotada dentro de un Column con scroll → forEach (sin LazyColumn anidado)
                            perfil.history.forEach { item ->
                                HistorialItemRow(
                                    item = item,
                                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Fila de un movimiento de karma en el perfil público (mismo diseño que el historial propio).
@Composable
private fun HistorialItemRow(item: KarmaHistoryDto, modifier: Modifier = Modifier) {
    val positive = item.amount >= 0
    val tint = if (positive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(tint.copy(alpha = 0.16f))
            ) {
                Icon(
                    if (positive) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = item.reason.ifBlank { "Movimiento de karma" },
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = item.createdAt.substringBefore("T"),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = if (positive) "+${item.amount}" else "${item.amount}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = tint
            )
        }
    }
}
