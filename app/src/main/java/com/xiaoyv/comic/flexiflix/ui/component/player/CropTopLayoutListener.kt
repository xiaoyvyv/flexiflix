package com.xiaoyv.comic.flexiflix.ui.component.player

import android.graphics.Matrix
import android.view.TextureView
import android.view.View
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi

/**
 * [CropTopLayoutListener]
 *
 * @author why
 * @since 5/23/24
 */
class CropTopLayoutListener(private val textureView: TextureView) : View.OnLayoutChangeListener {
    @OptIn(UnstableApi::class)
    override fun onLayoutChange(
        v: View?,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
        oldLeft: Int,
        oldTop: Int,
        oldRight: Int,
        oldBottom: Int,
    ) {
        val transformMatrix = Matrix()
        val textureViewWidth = textureView.width.toFloat()
        val textureViewHeight = textureView.height.toFloat()

        // 顶部裁剪功能
        if (textureViewWidth != 0f && textureViewHeight != 0f) {
            val cropTop = 0.1f
            val pivotX = textureViewWidth / 2
            transformMatrix.postScale(1f + cropTop, 1f + cropTop, pivotX, textureViewHeight)
        }

        textureView.setTransform(transformMatrix)
    }
}