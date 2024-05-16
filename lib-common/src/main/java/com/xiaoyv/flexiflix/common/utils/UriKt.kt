package com.xiaoyv.flexiflix.common.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import androidx.media3.common.MimeTypes
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

/**
 * 获取文件类型
 */
fun Uri.fileType(context: Context): String {
    return context.contentResolver.getType(this).orEmpty()
        .let {
            if (it.isNotBlank()) MimeTypeMap.getSingleton().getExtensionFromMimeType(it)
                .orEmpty() else it
        }
        .ifBlank {
            path.orEmpty().substringAfterLast(".")
        }
}