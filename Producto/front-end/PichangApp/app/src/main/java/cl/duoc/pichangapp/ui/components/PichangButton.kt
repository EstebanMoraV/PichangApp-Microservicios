package cl.duoc.pichangapp.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun PichangButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    text: String,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    isOutlined: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "button_scale"
    )

    val buttonModifier = modifier
        .fillMaxWidth()
        .height(56.dp)
        .graphicsLayer(scaleX = scale, scaleY = scale)

    if (isOutlined) {
        OutlinedButton(
            onClick = onClick,
            modifier = buttonModifier,
            enabled = enabled && !isLoading,
            interactionSource = interactionSource,
            shape = MaterialTheme.shapes.large
        ) {
            ButtonContent(isLoading, text)
        }
    } else {
        Button(
            onClick = onClick,
            modifier = buttonModifier,
            enabled = enabled && !isLoading,
            interactionSource = interactionSource,
            shape = MaterialTheme.shapes.large,
            colors = colors
        ) {
            ButtonContent(isLoading, text)
        }
    }
}

@Composable
private fun ButtonContent(isLoading: Boolean, text: String) {
    if (isLoading) {
        CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            strokeWidth = 2.dp
        )
    } else {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium
        )
    }
}
