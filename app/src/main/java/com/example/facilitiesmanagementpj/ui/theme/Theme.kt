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
val lightScheme = lightColorScheme(
    // 🌿 Màu chủ đạo – dùng cho các thành phần chính như AppBar, nút chính, icon chính, chip trạng thái
    primary = primaryLight,

    // 🖋 Màu chữ hoặc icon nằm trên nền primary (nút trắng chữ xanh)
    onPrimary = onPrimaryLight,

    // 📦 Màu nền phụ chủ đạo – dùng cho nút thứ cấp, card có viền nổi bật hoặc hộp nhóm
    primaryContainer = primaryContainerLight,

    // 🖋 Màu chữ/icon nằm trên nền primaryContainer
    onPrimaryContainer = onPrimaryContainerLight,

    // 🌱 Màu phụ thứ 2 – thường dùng cho chip lọc, thông tin phụ trợ (như trạng thái phụ)
    secondary = secondaryLight,

    // 🖋 Màu chữ/icon nằm trên nền secondary
    onSecondary = onSecondaryLight,

    // 📦 Container của màu secondary – có thể dùng cho thẻ nhỏ hoặc vùng nền nhẹ
    secondaryContainer = secondaryContainerLight,

    // 🖋 Màu chữ/icon nằm trên nền secondaryContainer
    onSecondaryContainer = onSecondaryContainerLight,

    // 🌺 Màu thứ ba – thường dùng cho biểu tượng nhấn mạnh (alert nhỏ, vùng điểm nhấn nhẹ)
    tertiary = tertiaryLight,

    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,

    // ❌ Màu lỗi – dùng cho nút "Từ chối", thông báo lỗi, border cảnh báo
    error = errorLight,

    // 🖋 Màu chữ/icon nằm trên nền error
    onError = onErrorLight,

    // 📦 Container lỗi – có thể dùng cho thông báo lỗi nổi bật (background đỏ nhẹ)
    errorContainer = errorContainerLight,

    // 🖋 Màu chữ/icon trên nền errorContainer
    onErrorContainer = onErrorContainerLight,

    // 🎨 Màu nền chính cho toàn app
    background = backgroundLight,

    // 🖋 Màu chữ chính (dùng trên background)
    onBackground = onBackgroundLight,

    // 🪵 Màu mặt phẳng chính – nền của các card, surface, bottom sheet, button,...
    surface = surfaceLight,

    // 🖋 Màu chữ/icon dùng trong surface
    onSurface = onSurfaceLight,

    // 🎨 Biến thể của surface – dùng cho phần tách biệt, như item trong danh sách, card phụ
    surfaceVariant = surfaceVariantLight,

    onSurfaceVariant = onSurfaceVariantLight,

    // 🧱 Màu đường viền chính
    outline = outlineLight,

    // 🧱 Màu viền phụ nhẹ hơn (ít dùng, có thể dùng để chia block)
    outlineVariant = outlineVariantLight,

    // 🌫 Overlay – dùng cho overlay như scrim (ví dụ khi bottom sheet mở)
    scrim = scrimLight,

    // 🌙 Mặt phẳng đảo ngược (hiếm dùng, thường auto)
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,

    // 🌘 Biến thể nền tối/nhẹ hơn – dùng để chia layer nội dung như sheet, dialog, card
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

    // 🌿 Màu chủ đạo – sử dụng cho nút chính, chip trạng thái, icon nổi bật
    primary = primaryDark,

    // 🖋 Màu chữ hoặc icon nằm trên nền primary
    onPrimary = onPrimaryDark,

    // 📦 Màu nền phụ chủ đạo – dùng cho nút phụ, card có nhóm hoặc header của block
    primaryContainer = primaryContainerDark,

    // 🖋 Màu chữ/icon nằm trên primaryContainer
    onPrimaryContainer = onPrimaryContainerDark,

    // 🌱 Màu phụ thứ 2 – dùng cho chip lọc, thẻ thông tin phụ
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,

    // 🌺 Màu thứ ba – dùng cho hiệu ứng nhấn mạnh, ảnh minh hoạ nhỏ
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,

    // ❌ Màu lỗi – màu nền hoặc viền cảnh báo lỗi
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,

    // 🎨 Nền chính toàn app trong dark mode
    background = backgroundDark,

    // 🖋 Màu chữ chính trong dark mode
    onBackground = onBackgroundDark,

    // 🪵 Màu nền của các surface chính – card, sheet, bottom bar...
    surface = surfaceDark,

    // 🖋 Màu chữ/icon chính trên surface
    onSurface = onSurfaceDark,

    // 🎨 Màu nền phụ – cho vùng chia lớp như card phụ, chip phụ, list item
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,

    // 🧱 Màu border chính (Divider, outline field...)
    outline = outlineDark,

    // 🧱 Màu border phụ nhẹ hơn – ít dùng
    outlineVariant = outlineVariantDark,

    // 🌫 Overlay scrim – thường là semi-transparent black
    scrim = scrimDark,

    // 🌙 Các biến thể inverse – dùng cho dialog, banner ngược màu
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,

    // 🌘 Các lớp nền cao/thấp – tạo cảm giác nổi khối hơn trong dark mode
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
