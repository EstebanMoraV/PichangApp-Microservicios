package cl.duoc.pichangapp.ui.screens.auth

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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
fun LoginScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit,
    mensaje: String? = null,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val shakeAnim = remember { Animatable(0f) }

    LaunchedEffect(mensaje) {
        if (!mensaje.isNullOrBlank()) {
            snackbarHostState.showSnackbar(mensaje)
        }
    }

    LaunchedEffect(state) {
        if (state is LoginState.Success) {
            onNavigateToHome()
        } else if (state is LoginState.Error) {
            snackbarHostState.showSnackbar((state as LoginState.Error).message)
            // Shake animation on error
            scope.launch {
                for (i in 0..5) {
                    shakeAnim.animateTo(if (i % 2 == 0) 10f else -10f, animationSpec = tween(50))
                }
                shakeAnim.animateTo(0f)
            }
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
                    .graphicsLayer(translationX = shakeAnim.value)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Bienvenido de nuevo",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Inicia sesión para continuar",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
                    )

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

                    Spacer(modifier = Modifier.height(16.dp))

                    PichangTextField(
                        value = password,
                        onValueChange = { 
                            password = it
                            passwordError = false
                        },
                        label = "Contraseña",
                        isError = passwordError,
                        errorMessage = "Debe tener al menos 6 caracteres",
                        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    val isLoading = state is LoginState.Loading
                    PichangButton(
                        onClick = {
                            var valid = true
                            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                                emailError = true
                                valid = false
                            }
                            if (password.length < 6) {
                                passwordError = true
                                valid = false
                            }
                            
                            if (valid) {
                                viewModel.login(correo, password)
                            } else {
                                scope.launch {
                                    for (i in 0..5) {
                                        shakeAnim.animateTo(if (i % 2 == 0) 10f else -10f, animationSpec = tween(50))
                                    }
                                    shakeAnim.animateTo(0f)
                                }
                            }
                        },
                        text = "Iniciar Sesión",
                        isLoading = isLoading
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(onClick = onNavigateToRegister) {
                        Text(
                            text = "¿No tienes cuenta? Regístrate aquí",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
