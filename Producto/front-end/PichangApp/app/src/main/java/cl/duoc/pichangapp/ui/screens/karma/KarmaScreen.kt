package cl.duoc.pichangapp.ui.screens.karma

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import cl.duoc.pichangapp.ui.components.PichangCard
import cl.duoc.pichangapp.ui.components.LoadingScreen
import cl.duoc.pichangapp.ui.components.EmptyState
import cl.duoc.pichangapp.ui.theme.KarmaExcellent
import cl.duoc.pichangapp.ui.theme.KarmaGood
import cl.duoc.pichangapp.ui.theme.KarmaLow
import cl.duoc.pichangapp.ui.theme.KarmaRegular
import kotlin.math.roundToInt

@Composable
fun KarmaScreen(
    navController: NavController? = null,
    viewModel: KarmaViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (state.isLoading) {
            LoadingScreen()
        } else if (state.error != null) {
            EmptyState(emoji = "⚠️", title = "Error", message = state.error!!)
        } else {
            val karma = state.karma

            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    "Tu Nivel de Karma", 
                    style = MaterialTheme.typography.headlineMedium, 
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(32.dp))

                val badgeColor = when (karma?.categoria?.lowercase()) {
                    "excelente", "oro" -> KarmaExcellent
                    "bueno", "plata" -> KarmaGood
                    "regular", "bronce" -> KarmaRegular
                    else -> KarmaLow
                }

                val scoreTarget = karma?.puntaje ?: 0
                val animatedScore = remember { Animatable(0f) }
                LaunchedEffect(scoreTarget) {
                    animatedScore.animateTo(
                        targetValue = scoreTarget.toFloat(),
                        animationSpec = tween(1500, easing = androidx.compose.animation.core.FastOutSlowInEasing)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .background(badgeColor.copy(alpha = 0.1f), CircleShape)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { animatedScore.value / 1000f }, // Assume 1000 is max
                        modifier = Modifier.fillMaxSize(),
                        color = badgeColor,
                        strokeWidth = 8.dp,
                        strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Filled.Star, contentDescription = null, tint = badgeColor, modifier = Modifier.size(48.dp))
                        Text(
                            text = "${animatedScore.value.roundToInt()}",
                            style = MaterialTheme.typography.displayLarge,
                            fontWeight = FontWeight.Bold,
                            color = badgeColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .background(badgeColor, RoundedCornerShape(16.dp))
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = karma?.categoria?.uppercase() ?: "SIN CATEGORÍA",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    "Historial Reciente", 
                    style = MaterialTheme.typography.titleLarge, 
                    fontWeight = FontWeight.Bold, 
                    modifier = Modifier.align(Alignment.Start)
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                val history = karma?.history ?: emptyList()

                if (history.isEmpty()) {
                    EmptyState(emoji = "📈", title = "Sin movimientos", message = "No tienes movimientos de karma aún.")
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(history, key = { _, item -> item.createdAt }) { index, item ->
                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn(tween(300, delayMillis = index * 50)) + slideInHorizontally(tween(300, delayMillis = index * 50))
                            ) {
                                PichangCard(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        val isPositive = item.amount > 0
                                        val amountColor = if (isPositive) KarmaExcellent else MaterialTheme.colorScheme.error
                                        val icon = if (isPositive) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward
                                        val sign = if (isPositive) "+" else ""

                                        Box(
                                            modifier = Modifier
                                                .size(48.dp)
                                                .background(amountColor.copy(alpha = 0.1f), CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(icon, contentDescription = null, tint = amountColor)
                                        }

                                        Spacer(modifier = Modifier.width(16.dp))

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(item.reason, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                            Text(item.createdAt.substringBefore("T"), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }

                                        Text(
                                            text = "$sign${item.amount}", 
                                            color = amountColor, 
                                            fontWeight = FontWeight.Bold, 
                                            fontSize = 20.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
