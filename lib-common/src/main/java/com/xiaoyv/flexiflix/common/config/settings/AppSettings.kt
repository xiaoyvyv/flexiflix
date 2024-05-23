@file:Suppress("MemberVisibilityCanBePrivate")

package com.xiaoyv.flexiflix.common.config.settings

import androidx.annotation.IntDef
import com.xiaoyv.flexiflix.extension.utils.sharePreference

/**
 * [AppSettings]
 *
 * @author why
 * @since 5/23/24
 */
object AppSettings {
    /**
     * 深色模式
     */
    const val THEME_DARK_MODE_KEY = "theme_dark_mode"
    const val THEME_DARK_MODE_VALUE_SYSTEM = 0
    const val THEME_DARK_MODE_VALUE_ON = 1
    const val THEME_DARK_MODE_VALUE_OFF = 2
    var darkModeSpValue by sharePreference(THEME_DARK_MODE_KEY, THEME_DARK_MODE_VALUE_SYSTEM)

    @IntDef(THEME_DARK_MODE_VALUE_SYSTEM, THEME_DARK_MODE_VALUE_ON, THEME_DARK_MODE_VALUE_OFF)
    @Retention(AnnotationRetention.SOURCE)
    annotation class DarkMode

    /**
     * 深色模型使用纯黑
     */
    const val THEME_DARK_PURE_KEY = "theme_dark_pure"
    const val THEME_DARK_PURE_VALUE_DEFAULT = false
    var darkForceBlackSpValue by sharePreference(THEME_DARK_PURE_KEY, THEME_DARK_PURE_VALUE_DEFAULT)

    /**
     * 颜色外观
     */
    const val THEME_COLOR_KEY = "theme_color"
    const val THEME_COLOR_VALUE_DEFAULT = 0
    var themeSpValue by sharePreference(THEME_COLOR_KEY, THEME_COLOR_VALUE_DEFAULT)

    @IntDef(THEME_COLOR_VALUE_DEFAULT)
    @Retention(AnnotationRetention.SOURCE)
    annotation class ThemeColor

}