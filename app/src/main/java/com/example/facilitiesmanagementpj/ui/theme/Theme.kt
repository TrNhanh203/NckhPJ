package com.example.facilitiesmanagementpj.ui.theme

import android.app.Activity
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

// Định nghĩa màu sắc theo bộ bạn đã chọn
val IndigoPrimary = Color(0xFF152693)
val IndigoDark = Color(0xFF303F9F)
val IndigoLight = Color(0xFFC5CAE9)

val AccentBlue = Color(0xFF03A9F4)

val BackgroundWhite = Color(0xFFFFFFFF)
val DividerGray = Color(0xFFBDBDBD)

val PrimaryText = Color(0xFF212121)
val SecondaryText = Color(0xFF757575)

val LightColorScheme = lightColorScheme(
    primary = IndigoPrimary,
    onPrimary = Color.White,
    primaryContainer = IndigoLight,
    onPrimaryContainer = IndigoDark,

    secondary = AccentBlue,
    onSecondary = Color.White,
    secondaryContainer = AccentBlue.copy(alpha = 0.1f),
    onSecondaryContainer = Color.Black,

    background = BackgroundWhite,
    onBackground = PrimaryText,
    surface = BackgroundWhite,
    onSurface = PrimaryText,

    outline = DividerGray,
    error = Color(0xFFB00020),
    onError = Color.White,
)

val DarkColorScheme = darkColorScheme(
    primary = IndigoLight,
    onPrimary = IndigoDark,
    secondary = AccentBlue,
    onSecondary = Color.Black,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,
    outline = DividerGray,
    error = Color(0xFFCF6679),
    onError = Color.Black,

)

@Composable
fun FacilitiesManagementPJTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
