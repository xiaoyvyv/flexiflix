package com.xiaoyv.comic.flexiflix.ui.component

import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.staticCompositionLocalOf
import com.xiaoyv.flexiflix.extension.config.settings.AppSettings
import com.xiaoyv.flexiflix.common.utils.mutableStateFlowOf
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * [AppThemeState]
 *
 * @author why
 * @since 5/23/24
 */
class AppThemeState {

    /**
     * 深色模式
     */
    private val _darkMode by lazy { mutableStateFlowOf(AppSettings.Theme.darkMode) }
    val darkMode get() = _darkMode.asStateFlow()

    /**
     * 主题颜色
     */
    private val _themeColor by lazy { mutableStateFlowOf(AppSettings.Theme.themeColor) }
    val themeColor get() = _themeColor.asStateFlow()

    /**
     * 暗色模式强制黑色主题
     */
    private val _darkForceBlack by lazy { mutableStateFlowOf(AppSettings.Theme.darkForceBlack) }
    val darkForceBlack get() = _darkForceBlack.asStateFlow()

    /**
     * 深色模式切换
     *
     * - 跟随系统
     * - 暗色
     * - 亮色
     */
    fun changeDarkMode(@AppSettings.Theme.DarkMode value: Int) {
        _darkMode.update { value }
    }

    /**
     * 主题颜色切换
     */
    fun changeTheme(color: String) {
        _themeColor.update { color }
    }

    /**
     * 暗色模式强制黑色主题开关
     */
    fun changeDarkForceBlack(value: Boolean) {
        if (!AppSettings.editMode) {
            _darkForceBlack.update { value }
        }
    }

    /**
     * App 内当前主题是否为暗色
     */
    fun isDarkMode(context: Context): Boolean {
        val darkMode = darkMode.value
        val systemDarkTheme =
            (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        return when (darkMode) {
            // 跟随系统
            AppSettings.Theme.THEME_DARK_MODE_VALUE_SYSTEM -> systemDarkTheme
            // 暗色模式
            AppSettings.Theme.THEME_DARK_MODE_VALUE_ON -> true
            // 亮色模式
            AppSettings.Theme.THEME_DARK_MODE_VALUE_OFF -> false
            else -> error("error")
        }
    }
}

val LocalThemeConfigState = staticCompositionLocalOf<AppThemeState> {
    error("Please provide <AppThemeState> for LocalThemeConfigState")
}