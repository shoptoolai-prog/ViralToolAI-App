package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldPrimary,
    secondary = EmeraldLight,
    tertiary = ElectricPurple,
    background = AmoledBlack,
    surface = DarkSurface,
    onPrimary = AmoledBlack,
    onSecondary = AmoledBlack,
    onBackground = TextWhite,
    onSurface = TextWhite
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force AMOLED Theme
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
