package cl.duoc.pichangapp.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cl.duoc.pichangapp.ui.components.PichangCard
import cl.duoc.pichangapp.ui.components.PichangButton
import cl.duoc.pichangapp.ui.components.LoadingScreen
import cl.duoc.pichangapp.ui.components.EmptyState
import kotlin.math.absoluteValue

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.isLoading) {
            LoadingScreen()
        } else if (state.error != null) {
            EmptyState(emoji = "⚠️", title = "Error", message = state.error!!)
        } else {
            val user = state.user

            Spacer(modifier = Modifier.height(32.dp))

            // Avatar 
            val avatarColor = remember(user?.nombre) {
                val hash = user?.nombre?.hashCode()?.absoluteValue ?: 0
                val colors = listOf(Color(0xFF1565C0), Color(0xFF2E7D32), Color(0xFFE65100), Color(0xFF6A1B9A), Color(0xFF00838F))
                colors[hash % colors.size]
            }

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(avatarColor),
                contentAlignment = Alignment.Center
            ) {
                if (user?.nombre?.isNotEmpty() == true) {
                    val initials = "${user.nombre.first().uppercase()}${user.apellido.firstOrNull()?.uppercase() ?: ""}"
                    Text(
                        text = initials,
                        style = MaterialTheme.typography.displayMedium,
                        color = Color.White
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "${user?.nombre} ${user?.apellido}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (user?.correo != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = user.correo,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            PichangCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    ProfileRow(icon = Icons.Filled.Badge, label = "ID de Usuario", value = user?.id?.toString() ?: "N/A")
                    Divider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 1.dp)
                    ProfileRow(icon = Icons.Filled.Email, label = "Correo", value = user?.correo ?: "N/A")
                    Divider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 1.dp)
                    ProfileRow(icon = Icons.Filled.Person, label = "Nombre", value = "${user?.nombre} ${user?.apellido}")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            PichangButton(
                onClick = { showLogoutDialog = true },
                text = "Cerrar Sesión",
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            if (showLogoutDialog) {
                AlertDialog(
                    onDismissRequest = { showLogoutDialog = false },
                    title = { Text("Cerrar Sesión") },
                    text = { Text("¿Estás seguro que deseas cerrar sesión?") },
                    confirmButton = {
                        TextButton(onClick = {
                            showLogoutDialog = false
                            viewModel.logout(onLogoutSuccess = onLogout)
                        }) {
                            Text("Confirmar", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showLogoutDialog = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ProfileRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon, 
            contentDescription = null, 
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}
