package com.example.deliveryapp.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


private val LightColorScheme = lightColorScheme(
    primary = Red,
    background = White,
    surface = White,
    onPrimary = White,
    onBackground = Black,
    onSurface = Black,
    surfaceVariant = Backlight
)

@Composable
fun DeliveryAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}