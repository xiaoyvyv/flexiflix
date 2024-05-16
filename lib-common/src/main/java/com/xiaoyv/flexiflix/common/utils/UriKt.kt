package com.xiaoyv.flexiflix.common.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import java.io.InputStream

/**
 * 获取 Uri 文件名
 */
fun Uri.displayName(context: Context): String {
    var fileName: String? = null
    context.contentResolver.query(this, null, null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
            val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (displayNameIndex != -1) {
                fileName = cursor.getString(displayNameIndex)
            }
        }
    }

    if (fileName == null) {
        // 从 URI 的路径中提取文件名
        val pathSegments = this.pathSegments
        if (pathSegments != null && pathSegments.isNotEmpty()) {
            fileName = pathSegments.last()
        }
    }

    return fileName.orEmpty().ifBlank { System.currentTimeMillis().toString() }
}

fun Uri.inputStream(context: Context): InputStream {
    return requireNotNull(context.contentResolver.openInputStream(this))
}

fun Uri.realFilePath(context: Context): String {
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    return context.contentResolver.query(this, projection, null, null, null).use {
        it?.let {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            it.getString(columnIndex)
        }.orEmpty()
    }
}