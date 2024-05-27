package com.xiaoyv.flexiflix.extension.utils

import com.xiaoyv.flexiflix.extension.ExtensionProvider.Companion.application
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest

/**
 * [md5]
 *
 * @author why
 * @since 5/11/24
 */
fun File.md5(): String {
    val digest = MessageDigest.getInstance("MD5")
    FileInputStream(this).use { fileInputStream ->
        val buffer = ByteArray(1024)
        var read: Int = fileInputStream.read(buffer)
        while (read != -1) {
            digest.update(buffer, 0, read)
            read = fileInputStream.read(buffer)
        }
    }
    val md5Bytes = digest.digest()
    return md5Bytes.joinToString("") { "%02x".format(it) }.lowercase()
}


/**
 * 创建目录
 */
fun cacheDir(relative: String): File {
    return File(application.cacheDir.absolutePath, relative).let {
        if (it.exists().not()) it.mkdirs()
        it
    }
}

/**
 * 创建目录
 */
fun workDir(relative: String): String {
    return File(application.filesDir.absolutePath, relative).let {
        if (it.exists().not()) it.mkdirs()
        it.absolutePath
    }
}