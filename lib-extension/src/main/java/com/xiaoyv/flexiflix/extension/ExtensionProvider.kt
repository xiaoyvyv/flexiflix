package com.xiaoyv.flexiflix.extension

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.xiaoyv.flexiflix.extension.impl.javascript.JSExtensionService
import com.xiaoyv.flexiflix.extension.utils.copyAssetsFolder
import com.xiaoyv.flexiflix.extension.utils.workDir
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * [ExtensionProvider]
 *
 * @author why
 * @since 5/8/24
 */
class ExtensionProvider : ContentProvider() {

    companion object {
        internal lateinit var application: Application
    }

    override fun onCreate(): Boolean {
        application = requireNotNull(context?.applicationContext) as Application

        // Python 脚本环境初始化
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(application))
        }

        // NodeJS 初始化脚本资源复制
        ProcessLifecycleOwner.get().lifecycleScope.launch(Dispatchers.IO) {
            JSExtensionService.instacne.setup(application, BuildConfig.DEBUG)
        }

        // Jvm 初始化插件复制
        if (BuildConfig.DEBUG) {
            application.copyAssetsFolder("extension", workDir("extension/jvm"))
        }
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?,
    ): Cursor? {
        return null
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?,
    ): Int {
        return 0
    }
}