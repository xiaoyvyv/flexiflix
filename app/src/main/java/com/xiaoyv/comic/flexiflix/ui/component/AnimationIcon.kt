package com.xiaoyv.comic.flexiflix.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xiaoyv.comic.flexiflix.ui.theme.AppTheme
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlin.math.roundToLong

/**
 * [PlayingAnimationBar]
 *
 * @author why
 * @since 5/27/24
 */
@Composable
fun PlayingAnimationBar(
    sizeDp: Float,
    duration: Int = 300,
    color: Color? = null,
) {
    val bars = listOf(
        remember { Animatable(sizeDp / 2f) },
        remember { Animatable(sizeDp / 2f) },
        remember { Animatable(sizeDp / 2f) }
    )

    LaunchedEffect(Unit) {
        awaitAll(*bars.mapIndexed { index, animate ->
            async {
                while (true) {
                    delay(((duration / bars.size.toFloat()) * index).roundToLong())
                    animate.animateTo(
                        targetValue = sizeDp * 0.2f,
                        animationSpec = tween(durationMillis = duration, easing = LinearEasing)
                    )
                    animate.animateTo(
                        targetValue = sizeDp * 0.8f,
                        animationSpec = tween(durationMillis = duration, easing = LinearEasing)
                    )
                }
            }
        }.toTypedArray())
    }

    Row(
        modifier = Modifier.size(sizeDp.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        bars.forEach {
            Box(
                modifier = Modifier
                    .width((sizeDp / 6f).dp)
                    .height(it.value.dp)
                    .background(
                        color = color ?: MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.extraLarge
                    )
            )
        }
    }
}


@Preview(widthDp = 100, heightDp = 100)
@Composable
fun PreviewPlayingAnimationBar() {
    AppTheme {
        PlayingAnimationBar(sizeDp = 48f)
    }
}