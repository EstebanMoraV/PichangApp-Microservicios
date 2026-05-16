package cl.duoc.pichangapp.ui.screens.auth

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cl.duoc.pichangapp.ui.components.PichangButton
import cl.duoc.pichangapp.ui.components.PichangCard
import cl.duoc.pichangapp.ui.components.PichangSnackbar

@Composable
fun VerifyCodeScreen(
    email: String,
    onVerifySuccess: () -> Unit,
    viewModel: VerifyCodeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val timeLeft by viewModel.timeLeft.collectAsState()
    var code by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state) {
        if (state is VerifyCodeState.Success) {
            onVerifySuccess()
        } else if (state is VerifyCodeState.Error) {
            snackbarHostState.showSnackbar((state as VerifyCodeState.Error).message)
        }
    }

    val minutes = timeLeft / 60
    val seconds = timeLeft % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)
    val isExpired = timeLeft <= 0

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
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Verifica tu cuenta",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Ingresa el código de 6 dígitos enviado a $email",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    BasicTextField(
                        value = code,
                        onValueChange = {
                            if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                                code = it
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        decorationBox = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                repeat(6) { index ->
                                    val char = when {
                                        index >= code.length -> ""
                                        else -> code[index].toString()
                                    }
                                    val isFocused = code.length == index
                                    
                                    val scale by animateFloatAsState(
                                        targetValue = if (char.isNotEmpty()) 1.1f else 1f,
                                        animationSpec = tween(150),
                                        label = "scale"
                                    )
                                    val borderColor by animateColorAsState(
                                        targetValue = if (isFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                                        label = "color"
                                    )
                                    
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .scale(if (char.isNotEmpty()) 1f else scale) // Reset scale after typing
                                            .border(
                                                width = if (isFocused) 2.dp else 1.dp,
                                                color = borderColor,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .background(
                                                color = if (char.isNotEmpty()) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f) else Color.Transparent,
                                                shape = RoundedCornerShape(8.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = char,
                                            style = MaterialTheme.typography.headlineMedium,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(80.dp)) {
                        CircularProgressIndicator(
                            progress = timeLeft.toFloat() / 300f, // Assuming 5 minutes (300s)
                            modifier = Modifier.fillMaxSize(),
                            color = if (isExpired) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                            strokeWidth = 4.dp
                        )
                        Text(
                            text = timeFormatted,
                            style = MaterialTheme.typography.titleMedium,
                            color = if (isExpired) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    val isLoading = state is VerifyCodeState.Loading
                    if (isExpired) {
                        PichangButton(
                            onClick = { viewModel.resendCode(email) },
                            text = "Reenviar código",
                            isLoading = isLoading
                        )
                    } else {
                        PichangButton(
                            onClick = { viewModel.verifyCode(email, code) },
                            enabled = code.length == 6,
                            text = "Verificar",
                            isLoading = isLoading
                        )
                    }
                }
            }
        }
    }
}
