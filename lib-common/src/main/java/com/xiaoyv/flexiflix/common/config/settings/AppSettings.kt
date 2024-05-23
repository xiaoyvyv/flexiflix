@file:Suppress("MemberVisibilityCanBePrivate")

package com.xiaoyv.flexiflix.common.config.settings

import androidx.annotation.IntDef
import com.xiaoyv.flexiflix.common.config.settings.AppSettings.Player.PLAYER_DRAG_SENSITIVITY_KEY
import com.xiaoyv.flexiflix.common.config.settings.AppSettings.Player.PLAYER_DRAG_SENSITIVITY_VALUE_MIDDLE
import com.xiaoyv.flexiflix.extension.utils.sharePreference

/**
 * [AppSettings]
 *
 * @author why
 * @since 5/23/24
 */
object AppSettings {

    /**
     * 主题相关配置
     */
    object Theme {
        /**
         * 深色模式
         */
        const val THEME_DARK_MODE_KEY = "theme_dark_mode"
        const val THEME_DARK_MODE_VALUE_SYSTEM = 0
        const val THEME_DARK_MODE_VALUE_ON = 1
        const val THEME_DARK_MODE_VALUE_OFF = 2
        var darkMode by sharePreference(THEME_DARK_MODE_KEY, THEME_DARK_MODE_VALUE_SYSTEM)

        @IntDef(THEME_DARK_MODE_VALUE_SYSTEM, THEME_DARK_MODE_VALUE_ON, THEME_DARK_MODE_VALUE_OFF)
        @Retention(AnnotationRetention.SOURCE)
        annotation class DarkMode

        /**
         * 深色模型使用纯黑
         */
        const val THEME_DARK_PURE_KEY = "theme_dark_pure"
        const val THEME_DARK_PURE_VALUE_DEFAULT = false
        var darkForceBlack by sharePreference(
            THEME_DARK_PURE_KEY,
            THEME_DARK_PURE_VALUE_DEFAULT
        )

        /**
         * 颜色外观
         */
        const val THEME_COLOR_KEY = "theme_color"
        const val THEME_COLOR_VALUE_DEFAULT = 0
        var theme by sharePreference(THEME_COLOR_KEY, THEME_COLOR_VALUE_DEFAULT)

        @IntDef(THEME_COLOR_VALUE_DEFAULT)
        @Retention(AnnotationRetention.SOURCE)
        annotation class ThemeColor
    }

    /**
     * 播放器相关配置
     */
    object Player {
        /**
         * 播放器竖屏状态宽高比
         */
        const val PLAYER_PORTRAIT_RATIO_KEY = "player_portrait_ratio"
        const val PLAYER_PORTRAIT_RATIO_VALUE_4_3 = 4 / 3f
        const val PLAYER_PORTRAIT_RATIO_VALUE_15_10 = 15 / 10f
        const val PLAYER_PORTRAIT_RATIO_VALUE_16_9 = 16 / 9f
        var portraitRatio by sharePreference(
            PLAYER_PORTRAIT_RATIO_KEY,
            PLAYER_PORTRAIT_RATIO_VALUE_4_3
        )

        /**
         * 长按倍数播放速率
         */
        const val PLAYER_PRESS_SPEED_KEY = "player_press_speed"
        const val PLAYER_PRESS_SPEED_VALUE_1_5 = 1.5f
        const val PLAYER_PRESS_SPEED_VALUE_2 = 2f
        const val PLAYER_PRESS_SPEED_VALUE_2_5 = 2.5f
        const val PLAYER_PRESS_SPEED_VALUE_3 = 3f
        const val PLAYER_PRESS_SPEED_VALUE_3_5 = 3.5f
        const val PLAYER_PRESS_SPEED_VALUE_4 = 4f
        var pressSpeed by sharePreference(PLAYER_PRESS_SPEED_KEY, PLAYER_PRESS_SPEED_VALUE_3)

        /**
         * 手势左右拖拽进度灵敏度
         */
        const val PLAYER_DRAG_SENSITIVITY_KEY = "player_drag_sensitivity"
        const val PLAYER_DRAG_SENSITIVITY_VALUE_LOWEST = 4f
        const val PLAYER_DRAG_SENSITIVITY_VALUE_LOWER = 3f
        const val PLAYER_DRAG_SENSITIVITY_VALUE_MIDDLE = 2f
        const val PLAYER_DRAG_SENSITIVITY_VALUE_HIGHER = 1f
        const val PLAYER_DRAG_SENSITIVITY_VALUE_HIGHEST = 0.5f
        var dragSensitivity by sharePreference(
            PLAYER_DRAG_SENSITIVITY_KEY,
            PLAYER_DRAG_SENSITIVITY_VALUE_MIDDLE
        )
    }

    /**
     * 网络相关配置
     */
    object Network {
        /**
         * 是否启用自定义 Host 功能
         */
        const val NETWORK_HOST_ENABLE_KEY = "network_host_enable"
        var hostEnable by sharePreference(NETWORK_HOST_ENABLE_KEY, false)

        /**
         * 是否启用插件中配置的 Host
         */
        const val NETWORK_HOST_EXTENSION_ENABLE_KEY = "network_host_extension_enable"
        var hostExtensionEnable by sharePreference(NETWORK_HOST_EXTENSION_ENABLE_KEY, false)

        /**
         * 自定义 Host 内容
         */
        const val NETWORK_HOST_CONTENT_KEY = "network_host_content"
        var hostContent by sharePreference(NETWORK_HOST_CONTENT_KEY, "")
    }

    /**
     * 实验室相关功能
     */
    object Beta {
        /**
         * 过滤 M3U8 数据源广告
         */
        const val BETA_BLOCK_M3U8_AD_KEY = "beta_block_m3u8_ad"
        var blockM3u8Ad by sharePreference(BETA_BLOCK_M3U8_AD_KEY, false)


        /**
         * 裁除顶部轮播广告
         */
        const val BETA_CROP_TOP_KEY = "beta_crop_top"
        const val BETA_CROP_TOP_VALUE_0 = 0f
        const val BETA_CROP_TOP_VALUE_0_025 = 0.025f
        const val BETA_CROP_TOP_VALUE_0_05 = 0.05f
        const val BETA_CROP_TOP_VALUE_0_075 = 0.075f
        const val BETA_CROP_TOP_VALUE_0_1 = 0.1f
        const val BETA_CROP_TOP_VALUE_0_125 = 0.125f
        const val BETA_CROP_TOP_VALUE_0_15 = 0.15f
        const val BETA_CROP_TOP_VALUE_0_175 = 0.175f
        const val BETA_CROP_TOP_VALUE_0_2 = 0.2f
        var cropTop by sharePreference(BETA_CROP_TOP_KEY, BETA_CROP_TOP_VALUE_0)
    }
}