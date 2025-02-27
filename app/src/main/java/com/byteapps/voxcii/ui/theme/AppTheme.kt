package com.byteapps.voxcii.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

data class AppColorScheme(
    val background:Color,
    val onBackground:Color,
    val primary:Color,
    val onPrimary:Color,
    val secondary:Color,
    val onSecondary:Color,
    val tertiary:Color,
    val onTertiary:Color,
)

data class AppTypography(
    val headingLarge:TextStyle,
    val titleLarge:TextStyle,
    val titleMedium:TextStyle,
    val titleSmall:TextStyle,
)

data class AppShape(
    val container:Shape,
    val button:Shape
)

val LocalAppColorScheme = staticCompositionLocalOf {
    AppColorScheme(
        background = Color.Unspecified,
        onBackground = Color.Unspecified,
        primary = Color.Unspecified,
        onPrimary = Color.Unspecified,
        secondary = Color.Unspecified,
        onSecondary = Color.Unspecified,
        tertiary = Color.Unspecified,
        onTertiary = Color.Unspecified,
    )
}

val LocalAppTypography = staticCompositionLocalOf {
    AppTypography(
        headingLarge = TextStyle.Default,
        titleLarge = TextStyle.Default,
        titleMedium = TextStyle.Default,
        titleSmall = TextStyle.Default
    )
}

val LocalAppShape = staticCompositionLocalOf {
    AppShape(
        container = RectangleShape,
        button = RectangleShape
    )
}

private val darkColorScheme = AppColorScheme(
    background = DarkGray,
    onBackground = LightDarkGray,
    primary = Primary,
    onPrimary = DarkOnSecondary,
    secondary = Yellow,
    onSecondary = DarkTextColor,
    tertiary = Sky,
    onTertiary = Color.White,
)

private val lightColorScheme = AppColorScheme(
    background = Background,
    onBackground = Color.White,
    primary = Primary,
    onPrimary =  BorderColor,
    secondary = Secondary,
    onSecondary = DarkTextColor,
    tertiary = Tertiary,
    onTertiary = DarkGray,
)

private val typography = AppTypography(
    headingLarge =  TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    ),
    titleLarge = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    ),
    titleMedium = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    ),
    titleSmall = TextStyle(
        fontSize = 14.sp
    )
)

private val shape = AppShape(
    container = RoundedCornerShape(12.dp),
    button = RoundedCornerShape(12.dp)
)

@Composable
fun AppTheme(
    isDarkTheme:Boolean = false,
    content:@Composable ()->Unit
) {

    val colorScheme = if (isDarkTheme) darkColorScheme else lightColorScheme

    CompositionLocalProvider(
        LocalAppColorScheme provides colorScheme,
        LocalAppTypography provides typography,
        LocalAppShape provides shape,
        content = content
    )
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = if (isDarkTheme) colorScheme.background.toArgb() else colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }
}
object AppTheme{
    val colorScheme:AppColorScheme
        @Composable get() = LocalAppColorScheme.current

    val typography:AppTypography
        @Composable get() = LocalAppTypography.current

    val shape:AppShape
        @Composable get() = LocalAppShape.current
}
