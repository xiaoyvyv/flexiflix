package com.xiaoyv.comic.flexiflix.ui.component

import androidx.compose.runtime.staticCompositionLocalOf
import com.xiaoyv.flexiflix.common.config.settings.AppSettings
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
    private val _darkMode = mutableStateFlowOf(AppSettings.darkModeSpValue)
    val darkMode get() = _darkMode.asStateFlow()

    /**
     * 主题颜色
     */
    private val _theme = mutableStateFlowOf(AppSettings.themeSpValue)
    val theme get() = _theme.asStateFlow()

    /**
     * 暗色模式强制黑色主题
     */
    private val _darkForceBlack = mutableStateFlowOf(AppSettings.darkForceBlackSpValue)
    val darkForceBlack get() = _darkForceBlack.asStateFlow()

    /**
     * 深色模式切换
     *
     * - 跟随系统
     * - 暗色
     * - 亮色
     */
    fun changeDarkMode(@AppSettings.DarkMode value: Int) {
        _darkMode.update { value }
    }

    /**
     * 主题颜色切换
     */
    fun changeTheme(@AppSettings.ThemeColor value: Int) {
        _theme.update { value }
    }

    /**
     * 暗色模式强制黑色主题开关
     */
    fun changeDarkForceBlack(value: Boolean) {
        _darkForceBlack.update { value }
    }
}

val LocalThemeConfigState = staticCompositionLocalOf<AppThemeState> {
    error("")
}