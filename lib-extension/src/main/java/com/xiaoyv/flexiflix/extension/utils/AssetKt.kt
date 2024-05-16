package com.xiaoyv.flexiflix.extension.utils

import android.content.Context
import java.io.File
import java.io.FileOutputStream

/**
 * [copyAssetsFolder]
 *
 * @author why
 * @since 5/14/24
 */
fun Context.copyAssetsFolder(assetFolder: String, destinationFolder: String) {
    val context = this
    val assetManager = context.assets
    val files = assetManager.list(assetFolder)

    if (files != null) {
        for (filename in files) {
            val assetPath = "$assetFolder/$filename"
            val destinationPath = "$destinationFolder/$filename"

            if (assetManager.list(assetPath)?.isNotEmpty() == true) {
                // Recursively copy subfolder
                File(destinationPath).mkdirs()
                copyAssetsFolder(assetPath, destinationPath)
            } else {
                copyAssetFile(assetPath, destinationPath)
            }
        }
    }
}

fun Context.copyAssetFile(assetPath: String, destinationPath: String) {
    val assetManager = assets
    val inputStream = assetManager.open(assetPath)
    val outputStream = FileOutputStream(File(destinationPath))
    inputStream.use { input ->
        outputStream.use { output ->
            input.copyTo(output)
        }
    }
}