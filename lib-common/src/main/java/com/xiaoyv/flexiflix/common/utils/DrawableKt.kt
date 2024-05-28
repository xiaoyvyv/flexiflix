package com.xiaoyv.flexiflix.common.utils

import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap

/**
 * 将 Drawable 的中心区域放大到指定倍数，并返回一个新的 Drawable。
 *
 * @param factor 放大的倍数
 * @return 放大后的 Drawable
 */
fun Drawable.zoomCenter(factor: Float): Bitmap {
    // 将 Drawable 转换为 Bitmap
    val bitmap = toBitmap()

    // 获取 Bitmap 的宽高
    val width = bitmap.width
    val height = bitmap.height

    // 计算中心区域的矩形
    val centerX = width / 2
    val centerY = height / 2
    val halfWidth = (width / (2 * factor)).toInt()
    val halfHeight = (height / (2 * factor)).toInt()
    val centerRect = Rect(
        centerX - halfWidth,
        centerY - halfHeight,
        centerX + halfWidth,
        centerY + halfHeight
    )

    // 裁剪中心区域
    val centerBitmap = Bitmap.createBitmap(
        bitmap,
        centerRect.left,
        centerRect.top,
        centerRect.width(),
        centerRect.height()
    )

    // 放大裁剪区域到原始 Bitmap 大小的指定倍数
    return Bitmap.createScaledBitmap(
        centerBitmap,
        (centerRect.width() * factor).toInt(),
        (centerRect.height() * factor).toInt(),
        true
    )
}