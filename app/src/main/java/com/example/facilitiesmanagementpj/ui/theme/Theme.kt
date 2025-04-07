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
val IndigoPrimary = Color(0xFF002d5e)
val IndigoDark = Color(0xFF303F9F)
val IndigoLight = Color(0xFFC5CAE9)

val AccentBlue = Color(0xFF03A9F4)

val BackgroundWhite = Color(0xFFFFFFFF)
val DividerGray = Color(0xFFBDBDBD)


val PrimaryText = Color(0xFF212121)
val SecondaryText = Color(0xFF757575)
val MainBlue = Color(0xFF18447C) // Màu chủ đạo mới
val MainBlueDark = Color(0xFF0F2D5A) // Cho onPrimaryContainer
val MainBlueLight = Color(0xFFD7E4F5) // Cho primaryContainer


//val LightColorScheme = lightColorScheme(
//    primary = MainBlue,
//    onPrimary = Color.White,
//    primaryContainer = MainBlueLight,
//    onPrimaryContainer = MainBlueDark,
//
//    secondary = AccentBlue,
//    onSecondary = Color.White,
//    secondaryContainer = AccentBlue.copy(alpha = 0.1f),
//    onSecondaryContainer = Color.Black,
//
//    background = BackgroundWhite,
//    onBackground = PrimaryText,
//    surface = BackgroundWhite,
//    onSurface = PrimaryText,
//    surfaceVariant = Color(0xFFE3EAF5),
//    onSurfaceVariant = Color(0xFF444444),
//
//    outline = DividerGray,
//    error = Color(0xFFB00020),
//    onError = Color.White,
//)
private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)


//val DarkColorScheme = darkColorScheme(
//    primary = MainBlueLight,
//    onPrimary = MainBlueDark,
//    primaryContainer = MainBlue,
//    onPrimaryContainer = Color.White,
//
//    secondary = AccentBlue,
//    onSecondary = Color.Black,
//    secondaryContainer = AccentBlue.copy(alpha = 0.2f),
//    onSecondaryContainer = Color.White,
//
//    background = Color(0xFF121212),
//    onBackground = Color.White,
//    surface = Color(0xFF1E1E1E),
//    onSurface = Color.White,
//    surfaceVariant = Color(0xFF2A2A2A),
//    onSurfaceVariant = Color(0xFFDDDDDD),
//
//    outline = DividerGray,
//    error = Color(0xFFCF6679),
//    onError = Color.Black,
//)
private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
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

        darkTheme -> darkScheme
        else -> lightScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
