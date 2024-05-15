@file:Suppress("unused")

package com.xiaoyv.comic.flexiflix.ui.animation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith

private const val ProgressThreshold = 0.35f

private val Int.ForOutgoing: Int
    get() = (this * ProgressThreshold).toInt()

private val Int.ForIncoming: Int
    get() = this - this.ForOutgoing

/**
 * [materialFadeThrough] allows to switch a layout with a fade through animation.
 *
 * @param durationMillis the duration of transition.
 */
fun materialFadeThrough(
    durationMillis: Int = MotionConstants.DefaultMotionDuration,
): ContentTransform = materialFadeThroughIn(
    durationMillis = durationMillis
) togetherWith materialFadeThroughOut(
    durationMillis = durationMillis
)

/**
 * [materialFadeThroughIn] allows to switch a layout with fade through enter transition.
 *
 * @param initialScale the starting scale of the enter transition.
 * @param durationMillis the duration of the enter transition.
 */
fun materialFadeThroughIn(
    initialScale: Float = 0.92f,
    durationMillis: Int = MotionConstants.DefaultMotionDuration,
): EnterTransition = fadeIn(
    animationSpec = tween(
        durationMillis = durationMillis.ForIncoming,
        delayMillis = durationMillis.ForOutgoing,
        easing = LinearOutSlowInEasing
    )
) + scaleIn(
    animationSpec = tween(
        durationMillis = durationMillis.ForIncoming,
        delayMillis = durationMillis.ForOutgoing,
        easing = LinearOutSlowInEasing
    ),
    initialScale = initialScale
)

/**
 * [materialFadeThroughOut] allows to switch a layout with fade through exit transition.
 *
 * @param durationMillis the duration of the exit transition.
 */
fun materialFadeThroughOut(
    durationMillis: Int = MotionConstants.DefaultMotionDuration,
): ExitTransition = fadeOut(
    animationSpec = tween(
        durationMillis = durationMillis.ForOutgoing,
        delayMillis = 0,
        easing = FastOutLinearInEasing
    )
)



