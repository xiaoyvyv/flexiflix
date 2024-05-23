package com.xiaoyv.comic.flexiflix.ui.component

import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.Placeholder
import com.bumptech.glide.integration.compose.RequestBuilderTransform
import com.bumptech.glide.integration.compose.Transition

/**
 * [ElevatedImage]
 *
 * @author why
 * @since 5/12/24
 */
@Composable
fun ElevatedImage(
    modifier: Modifier = Modifier,
    model: Any?,
    overlayBrush: Brush? = null,
    contentDescription: String? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Crop,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    loading: Placeholder? = null,
    failure: Placeholder? = null,
    transition: Transition.Factory? = CrossFade,
    requestBuilderTransform: RequestBuilderTransform<Drawable> = { it },
    onClick: () -> Unit = {},
) {
    ElevatedCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Box(Modifier.fillMaxSize()) {
            GlideImage(
                model = model,
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxSize(),
                alignment = alignment,
                contentScale = contentScale,
                alpha = alpha,
                colorFilter = colorFilter,
                loading = loading,
                failure = failure,
                transition = transition,
                requestBuilderTransform = requestBuilderTransform
            )

            if (overlayBrush != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(brush = overlayBrush)
                )
            }
        }
    }
}