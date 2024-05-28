@file:SuppressLint("RestrictedApi")

package com.xiaoyv.comic.flexiflix.ui.theme

import android.annotation.SuppressLint
import android.app.Activity
import android.app.UiModeManager
import android.content.Context
import android.os.Build
import android.os.Build.VERSION_CODES
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.material.color.utilities.Hct
import com.google.android.material.color.utilities.MaterialDynamicColors
import com.google.android.material.color.utilities.SchemeContent
import com.xiaoyv.comic.flexiflix.ui.component.AppThemeState
import com.xiaoyv.flexiflix.extension.config.settings.AppSettings

private val dynamicColors = MaterialDynamicColors()

private val Int.composeColor: Color
    get() = Color(this)

private fun getSystemContrast(context: Context): Float {
    val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as? UiModeManager
    return if ((uiModeManager == null || Build.VERSION.SDK_INT < VERSION_CODES.UPSIDE_DOWN_CAKE)) 0f
    else uiModeManager.contrast
}

/**
 * 主题
 */
data class ColorSchemes(
    val lightScheme: ColorScheme,
    val darkScheme: ColorScheme,
)

/**
 * 根据基色构建 ComposeUI 的亮色和暗色主题
 */
fun Context.createComposeTheme(
    themeColor: String,
    contrast: Double = getSystemContrast(this).toDouble(),
): ColorSchemes {
    val contentBasedSeedColor = android.graphics.Color.parseColor(themeColor)

    // 暗色和亮色
    val darkScheme = SchemeContent(Hct.fromInt(contentBasedSeedColor), true, contrast)
    val lightScheme = SchemeContent(Hct.fromInt(contentBasedSeedColor), false, contrast)

    return ColorSchemes(
        lightScheme = createComposeScheme(lightScheme),
        darkScheme = createComposeScheme(darkScheme)
    )
}

/**
 * 构建动态主题色
 */
private fun createComposeScheme(scheme: SchemeContent): ColorScheme {
    return ColorScheme(
        primary = dynamicColors.primary().getArgb(scheme).composeColor,
        onPrimary = dynamicColors.onPrimary().getArgb(scheme).composeColor,
        primaryContainer = dynamicColors.primaryContainer().getArgb(scheme).composeColor,
        onPrimaryContainer = dynamicColors.onPrimaryContainer().getArgb(scheme).composeColor,
        inversePrimary = dynamicColors.inversePrimary().getArgb(scheme).composeColor,
        secondary = dynamicColors.secondary().getArgb(scheme).composeColor,
        onSecondary = dynamicColors.onSecondary().getArgb(scheme).composeColor,
        secondaryContainer = dynamicColors.secondaryContainer().getArgb(scheme).composeColor,
        onSecondaryContainer = dynamicColors.onSecondaryContainer().getArgb(scheme).composeColor,
        tertiary = dynamicColors.tertiary().getArgb(scheme).composeColor,
        onTertiary = dynamicColors.onTertiary().getArgb(scheme).composeColor,
        tertiaryContainer = dynamicColors.tertiaryContainer().getArgb(scheme).composeColor,
        onTertiaryContainer = dynamicColors.onTertiaryContainer().getArgb(scheme).composeColor,
        background = dynamicColors.background().getArgb(scheme).composeColor,
        onBackground = dynamicColors.onBackground().getArgb(scheme).composeColor,
        surface = dynamicColors.surface().getArgb(scheme).composeColor,
        onSurface = dynamicColors.onSurface().getArgb(scheme).composeColor,
        surfaceVariant = dynamicColors.surfaceVariant().getArgb(scheme).composeColor,
        onSurfaceVariant = dynamicColors.onSurfaceVariant().getArgb(scheme).composeColor,
        surfaceTint = dynamicColors.surfaceTint().getArgb(scheme).composeColor,
        inverseSurface = dynamicColors.inverseSurface().getArgb(scheme).composeColor,
        inverseOnSurface = dynamicColors.inverseOnSurface().getArgb(scheme).composeColor,
        error = dynamicColors.error().getArgb(scheme).composeColor,
        onError = dynamicColors.onError().getArgb(scheme).composeColor,
        errorContainer = dynamicColors.errorContainer().getArgb(scheme).composeColor,
        onErrorContainer = dynamicColors.onErrorContainer().getArgb(scheme).composeColor,
        outline = dynamicColors.outline().getArgb(scheme).composeColor,
        outlineVariant = dynamicColors.outlineVariant().getArgb(scheme).composeColor,
        scrim = dynamicColors.scrim().getArgb(scheme).composeColor,
        surfaceBright = dynamicColors.surfaceBright().getArgb(scheme).composeColor,
        surfaceContainer = dynamicColors.surfaceContainer().getArgb(scheme).composeColor,
        surfaceContainerHigh = dynamicColors.surfaceContainerHigh()
            .getArgb(scheme).composeColor,
        surfaceContainerHighest = dynamicColors.surfaceContainerHighest()
            .getArgb(scheme).composeColor,
        surfaceContainerLow = dynamicColors.surfaceContainerLow().getArgb(scheme).composeColor,
        surfaceContainerLowest = dynamicColors.surfaceContainerLowest()
            .getArgb(scheme).composeColor,
        surfaceDim = dynamicColors.surfaceDim().getArgb(scheme).composeColor,
    )
}

/*

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
*/

private val highContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkHighContrast,
    onPrimary = onPrimaryDarkHighContrast,
    primaryContainer = primaryContainerDarkHighContrast,
    onPrimaryContainer = onPrimaryContainerDarkHighContrast,
    secondary = secondaryDarkHighContrast,
    onSecondary = onSecondaryDarkHighContrast,
    secondaryContainer = secondaryContainerDarkHighContrast,
    onSecondaryContainer = onSecondaryContainerDarkHighContrast,
    tertiary = tertiaryDarkHighContrast,
    onTertiary = onTertiaryDarkHighContrast,
    tertiaryContainer = tertiaryContainerDarkHighContrast,
    onTertiaryContainer = onTertiaryContainerDarkHighContrast,
    error = errorDarkHighContrast,
    onError = onErrorDarkHighContrast,
    errorContainer = errorContainerDarkHighContrast,
    onErrorContainer = onErrorContainerDarkHighContrast,
    background = backgroundDarkHighContrast,
    onBackground = onBackgroundDarkHighContrast,
    surface = surfaceDarkHighContrast,
    onSurface = onSurfaceDarkHighContrast,
    surfaceVariant = surfaceVariantDarkHighContrast,
    onSurfaceVariant = onSurfaceVariantDarkHighContrast,
    outline = outlineDarkHighContrast,
    outlineVariant = outlineVariantDarkHighContrast,
    scrim = scrimDarkHighContrast,
    inverseSurface = inverseSurfaceDarkHighContrast,
    inverseOnSurface = inverseOnSurfaceDarkHighContrast,
    inversePrimary = inversePrimaryDarkHighContrast,
    surfaceDim = surfaceDimDarkHighContrast,
    surfaceBright = surfaceBrightDarkHighContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkHighContrast,
    surfaceContainerLow = surfaceContainerLowDarkHighContrast,
    surfaceContainer = surfaceContainerDarkHighContrast,
    surfaceContainerHigh = surfaceContainerHighDarkHighContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkHighContrast,
)


@Composable
fun AppTheme(
    themeState: AppThemeState = remember { AppThemeState() },
    content: @Composable () -> Unit,
) {
    val view = LocalView.current
    val context = LocalContext.current

    AppSettings.editMode = view.isInEditMode

    val darkMode by themeState.darkMode.collectAsStateWithLifecycle()
    val darkModeForceBlack by themeState.darkForceBlack.collectAsStateWithLifecycle()

    // 当前主题色
    val themeColor by themeState.themeColor.collectAsStateWithLifecycle()
    val currentColorSchemes = remember(themeColor) {
        context.createComposeTheme(themeColor)
    }

    // 判断暗色模式
    val darkTheme = remember(darkMode) { themeState.isDarkMode(context) }

    val applyScheme = when {
        // 暗色强制黑色
        darkTheme && darkModeForceBlack -> highContrastDarkColorScheme
        // 暗色主题色
        darkTheme && !darkModeForceBlack -> currentColorSchemes.darkScheme
        // 亮色色主题色
        else -> currentColorSchemes.lightScheme
    }

//    val scheme =
//        SchemeContent(
//            Hct.fromInt(dynamicColorsOptions.getContentBasedSeedColor()),
//            !MaterialColors.isLightTheme(activity),
//            DynamicColors.getSystemContrast(activity).toDouble()
//        )
//    val dynamicColor = DynamicColor.fromArgb("test", Color.Red.toArgb())
//    MaterialColorUtilitiesHelper.createColorResourcesIdsToColorValues(
//        DynamicScheme()
//    )
//    DynamicColors.applyToActivityIfAvailable()
// 跟随系统（Android 12+）支持
    // && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
//                dynamicDarkColorScheme(context)
//                dynamicLightColorScheme(context)

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = applyScheme,
        typography = AppTypography,
        content = content
    )
}

/**
 * 单独配置指定颜色主题
 */
@Composable
fun MaterialColorTheme(themeColor: String, content: @Composable () -> Unit) {
    val context = LocalContext.current

    val colorSchemes = remember(themeColor) {
        context.createComposeTheme(themeColor)
    }

    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) colorSchemes.darkScheme else colorSchemes.lightScheme,
        typography = AppTypography,
        content = content
    )
}


