package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = CivicBlueContainer,
    onPrimary = Color.White,
    primaryContainer = NavyContainer,
    onPrimaryContainer = CivicBlueLight,
    secondary = CivicBlueContainer,
    onSecondary = Color.White,
    tertiary = EmeraldSafe,
    background = NavyDeep,
    surface = NavyDeep,
    onBackground = SurfaceWhite,
    onSurface = SurfaceWhite,
    surfaceVariant = NavyContainer,
    onSurfaceVariant = OutlineVariant,
    error = EmergencyRed,
    errorContainer = EmergencyRedContainer,
    onError = OnEmergencyRed,
  )

private val LightColorScheme =
  lightColorScheme(
    primary = NavyDeep,
    onPrimary = Color.White,
    primaryContainer = NavyContainer,
    onPrimaryContainer = Color.White,
    secondary = CivicBlue,
    onSecondary = Color.White,
    secondaryContainer = CivicBlueContainer,
    onSecondaryContainer = Color.White,
    tertiary = EmeraldSafe,
    onTertiary = Color.White,
    background = SurfaceWhite,
    surface = SurfaceWhite,
    surfaceVariant = SurfaceContainer,
    onSurfaceVariant = TextSecondary,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = EmergencyRed,
    errorContainer = EmergencyRedContainer,
    onError = OnEmergencyRed,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = false,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

