@file:Suppress("DEPRECATION")

package com.xiaoyv.flexiflix.common.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.graphics.Point
import android.os.Vibrator
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

/**
 * [findActivity]
 *
 * @author why
 * @since 5/9/24
 */
fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun Context.setScreenOrientation(orientation: Int) {
    val activity = this.findActivity() ?: return
    activity.requestedOrientation = orientation
//    if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//        hideSystemUi()
//    } else {
//        showSystemUi()
//    }
}

fun Context.hideSystemUi() {
    val activity = this.findActivity() ?: return
    val window = activity.window ?: return
    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(window, window.decorView).let { controller ->
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

fun Context.showSystemUi() {
    val activity = this.findActivity() ?: return
    val window = activity.window ?: return
    WindowCompat.setDecorFitsSystemWindows(window, true)
    WindowInsetsControllerCompat(window, window.decorView)
        .show(WindowInsetsCompat.Type.systemBars())
}

val Context.isLandscape: Boolean
    get() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

val Context.isPortrait: Boolean
    get() = resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE

/**
 * 屏幕宽度和高度
 */
val Context.screenInfo: Pair<Int, Int>
    get() {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val point = Point()
        display.getSize(point)
        return Pair(point.x, point.y)
    }

val Context.screenAspectRatio: Float
    get() = screenInfo.first / screenInfo.second.toFloat()

/**
 * 震动
 */
fun Context.vibrate(mills: Long) {
    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    vibrator.vibrate(mills)
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
