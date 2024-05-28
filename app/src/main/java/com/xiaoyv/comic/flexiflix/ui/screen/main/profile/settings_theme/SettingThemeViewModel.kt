@file:Suppress("SpellCheckingInspection")

package com.xiaoyv.comic.flexiflix.ui.screen.main.profile.settings_theme

import androidx.lifecycle.ViewModel
import com.google.android.material.color.DynamicColors
import com.xiaoyv.flexiflix.common.utils.mutableStateFlowOf
import com.xiaoyv.flexiflix.extension.config.settings.AppSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * [SettingThemeViewModel]
 *
 * @author why
 * @since 5/28/24
 */
@HiltViewModel
class SettingThemeViewModel @Inject constructor() : ViewModel() {
    private val _themeColors = mutableStateFlowOf(
        listOf(
            "#FF0000", "#FF5500", "#FFAA00", "#FFFF00",
            "#AA0000", "#AA5500", "#AAAA00", "#AAFF00",
            "#550000", "#555500", "#55AA00", "#55FF00",
            "#000000", "#005500", "#00AA00", "#00FF00",
            "#000055", "#005555", "#00AA55", "#00FF55",
            "#0000AA", "#0055AA", "#00AAAA", "#00FFAA",
            "#0000FF", "#0055FF", "#00AAFF", "#00FFFF",
            "#5500FF", "#5555FF", "#55AAFF", "#55FFFF",
            "#AA00FF", "#AA55FF", "#AAAAFF", "#AAFFFF",
            "#FF00FF", "#FF55FF", "#FFAAFF", "#FFFFFF"
        )
    )

    val themeColors get() = _themeColors.asStateFlow()

    init {
        checkCanSyncSystemDynamicColor()
    }

    /**
     * 如果支持同步系统整体动态色，则主题设置第一个多一个系统色
     */
    private fun checkCanSyncSystemDynamicColor() {
        if (DynamicColors.isDynamicColorAvailable()) {
            _themeColors.update {
                it.toMutableList().apply { add(0, AppSettings.Theme.THEME_COLOR_VALUE_SYSTEM) }
            }
        }
    }
}