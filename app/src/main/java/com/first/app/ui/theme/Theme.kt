package com.first.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Brand colours
val Primary     = Color(0xFF0057FF)
val PrimaryDark = Color(0xFF003DBF)
val Accent      = Color(0xFF00C9A7)
val ErrorRed    = Color(0xFFE53935)
val Discount    = Color(0xFFFF6D00)
val Surface     = Color(0xFFF8F9FD)
val TextDark    = Color(0xFF1A1A2E)
val CardBg      = Color(0xFFFFFFFF)
val BorderColor = Color(0xFFE8ECF4)
val FieldBg     = Color(0xFFF5F7FF)

private val LightColors = lightColorScheme(
    primary          = Primary,
    onPrimary        = Color.White,
    secondary        = Accent,
    onSecondary      = Color.White,
    error            = ErrorRed,
    onError          = Color.White,
    surface          = Surface,
    onSurface        = TextDark,
    background       = Surface,
    onBackground     = TextDark,
)

@Composable
fun FirstTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}








//package com.first.app.ui.theme
//
//import android.app.Activity
//import android.os.Build
//import androidx.compose.foundation.isSystemInDarkTheme
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.darkColorScheme
//import androidx.compose.material3.dynamicDarkColorScheme
//import androidx.compose.material3.dynamicLightColorScheme
//import androidx.compose.material3.lightColorScheme
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.platform.LocalContext
//
//private val DarkColorScheme = darkColorScheme(
//    primary = Purple80,
//    secondary = PurpleGrey80,
//    tertiary = Pink80
//)
//
//private val LightColorScheme = lightColorScheme(
//    primary = Purple40,
//    secondary = PurpleGrey40,
//    tertiary = Pink40
//
//    /* Other default colors to override
//    background = Color(0xFFFFFBFE),
//    surface = Color(0xFFFFFBFE),
//    onPrimary = Color.White,
//    onSecondary = Color.White,
//    onTertiary = Color.White,
//    onBackground = Color(0xFF1C1B1F),
//    onSurface = Color(0xFF1C1B1F),
//    */
//)
//
//@Composable
//fun FirstTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
//    // Dynamic color is available on Android 12+
//    dynamicColor: Boolean = true,
//    content: @Composable () -> Unit
//) {
//    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }
//
//    MaterialTheme(
//        colorScheme = colorScheme,
//        typography = Typography,
//        content = content
//    )
//}