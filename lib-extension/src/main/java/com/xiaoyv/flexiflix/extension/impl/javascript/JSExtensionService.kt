@file:Suppress("SpellCheckingInspection")

package com.xiaoyv.flexiflix.extension.impl.javascript

import android.app.Application
import android.util.Log
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.xiaoyv.flexiflix.extension.ExtensionProvider
import com.xiaoyv.flexiflix.extension.MediaSourceFactory
import com.xiaoyv.flexiflix.extension.utils.copyAssetFile
import com.xiaoyv.flexiflix.extension.utils.copyAssetsFolder
import com.xiaoyv.flexiflix.extension.utils.runCatchingPrint
import com.xiaoyv.flexiflix.extension.utils.sharePreference
import com.xiaoyv.flexiflix.extension.utils.versionCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import kotlin.concurrent.thread

/**
 * [JSExtensionService]
 *
 * @author why
 * @since 5/13/24
 */
class JSExtensionService private constructor() {
    private val context get() = ExtensionProvider.application

    private val setupVersion by sharePreference("setupVersion", 0)

    /**
     * js 相关数据根目录
     */
    private val nodeRootDir by lazy {
        val nodeDir = File(context.filesDir.absolutePath + "/node")
        nodeDir.mkdirs()
        nodeDir
    }

    /**
     * 初始化启动脚本
     */
    private val setupJs by lazy {
        File(nodeRootDir.absolutePath, "index.js")
    }

    /**
     * js 插件安装目录
     */
    private val moduleDir by lazy {
        File(nodeRootDir.absolutePath, "extension").apply {
            if (!exists()) mkdirs()
        }
    }

    /**
     * 当前正在运行的 nodeJs Job
     */
    private var currentProcess: Process? = null

    /**
     * 是否正在运行 nodejs
     */
    val isRunning: Boolean
        get() = currentProcess != null

    /**
     * JS 插件实现的数据请求本地服务
     */
    val jsApi: JSExtensionApi by lazy {
        MediaSourceFactory.retrofitBuilder
            .baseUrl("http://localhost:3000")
            .build()
            .create(JSExtensionApi::class.java)
    }

    /**
     * 检查复制初始化资源，仅配置初始化脚本文件，不会执行
     */
    fun setup(application: Application, debug: Boolean) {
        if (application.versionCode > setupVersion || debug) {
            application.copyAssetFile("node/index.js", setupJs.absolutePath)
        }

        if (debug) {
            application.copyAssetsFolder("node/extension", moduleDir.absolutePath)
        }
    }

    /**
     * 启动 js 插件服务，安装或卸载插件后需要重新启动该服务
     */
    fun startServer() {
        if (isRunning) {
            Log.i(javaClass.simpleName, "服务正在运行中")
            return
        }

        // 启动服务
        thread {
            runCatchingPrint {
                val libraryDir = context.applicationInfo.nativeLibraryDir
                val jsBin = "$libraryDir/libjs.so"

                // 启动服务
                val code = Runtime.getRuntime().exec(
                    arrayOf(jsBin, setupJs.absolutePath, "-d=${moduleDir}"),
                    arrayOf("LD_LIBRARY_PATH=$libraryDir")
                ).let {
                    currentProcess = it

                    // 等待服务进程结束
                    it.waitFor()
                }
                currentProcess = null

                Log.e(javaClass.simpleName, "进程结束，退出码：$code")
                code
            }
        }
    }

    /**
     * 重启服务
     */
    fun restart() {
        stop()
        startServer()
    }

    /**
     * 结束服务
     */
    fun stop() {
        if (currentProcess?.isAliveCompat() == true) {
            currentProcess?.destroy()
        }

        currentProcess = null
    }

    /**
     * 是否活着
     */
    private fun Process.isAliveCompat(): Boolean {
        try {
            exitValue()
            return false
        } catch (e: IllegalThreadStateException) {
            return true
        }
    }

    companion object {
        val instacne by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { JSExtensionService() }
    }
}