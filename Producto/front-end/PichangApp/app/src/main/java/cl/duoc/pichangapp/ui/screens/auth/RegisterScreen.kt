package cl.duoc.pichangapp.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cl.duoc.pichangapp.ui.components.PichangButton
import cl.duoc.pichangapp.ui.components.PichangCard
import cl.duoc.pichangapp.ui.components.PichangSnackbar
import cl.duoc.pichangapp.ui.components.PichangTextField
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    onNavigateBack: () -> Unit,
    onRegisterSuccess: (String) -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var nombreError by remember { mutableStateOf(false) }
    var apellidoError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(state) {
        if (state is RegisterState.Success) {
            onRegisterSuccess(correo)
        } else if (state is RegisterState.Error) {
            snackbarHostState.showSnackbar((state as RegisterState.Error).message)
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                PichangSnackbar(data)
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            PichangCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Crear Cuenta",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Progress Indicator (Step 1 -> Register)
                    LinearProgressIndicator(
                        progress = 0.5f,
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                    Text(
                        text = "Paso 1: Datos",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.align(Alignment.End).padding(top = 4.dp, bottom = 16.dp)
                    )

                    PichangTextField(
                        value = nombre,
                        onValueChange = { 
                            nombre = it
                            nombreError = false
                        },
                        label = "Nombre",
                        isError = nombreError,
                        errorMessage = "Mínimo 2 caracteres",
                        leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PichangTextField(
                        value = apellido,
                        onValueChange = { 
                            apellido = it
                            apellidoError = false
                        },
                        label = "Apellido",
                        isError = apellidoError,
                        errorMessage = "Mínimo 2 caracteres",
                        leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PichangTextField(
                        value = correo,
                        onValueChange = { 
                            correo = it
                            emailError = false
                        },
                        label = "Correo electrónico",
                        isError = emailError,
                        errorMessage = "Ingresa un correo válido",
                        leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PichangTextField(
                        value = password,
                        onValueChange = { 
                            password = it
                            passwordError = false
                        },
                        label = "Contraseña",
                        isError = passwordError,
                        errorMessage = "Mínimo 6 caracteres",
                        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    val isLoading = state is RegisterState.Loading
                    PichangButton(
                        onClick = {
                            var valid = true
                            if (nombre.length < 2) { nombreError = true; valid = false }
                            if (apellido.length < 2) { apellidoError = true; valid = false }
                            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) { emailError = true; valid = false }
                            if (password.length < 6) { passwordError = true; valid = false }

                            if (valid) {
                                viewModel.register(correo, password, nombre, apellido)
                            }
                        },
                        text = "Registrarse",
                        isLoading = isLoading
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    TextButton(onClick = onNavigateBack) {
                        Text(
                            text = "Ya tengo cuenta. Iniciar Sesión",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
