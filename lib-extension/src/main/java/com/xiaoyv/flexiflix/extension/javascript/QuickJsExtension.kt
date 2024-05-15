package com.xiaoyv.flexiflix.extension.javascript

import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import com.whl.quickjs.wrapper.JSObject
import com.whl.quickjs.wrapper.QuickJSContext
import com.xiaoyv.flexiflix.extension.javascript.utils.createPromise
import com.xiaoyv.flexiflix.extension.javascript.utils.paramAsLong
import com.xiaoyv.flexiflix.extension.javascript.utils.paramAsMethod
import com.xiaoyv.flexiflix.extension.javascript.utils.paramAsObj
import java.io.File

/**
 * [QuickJsExtension]
 *
 * @author why
 * @since 5/15/24
 */
class QuickJsExtension(val extension: File) {
    private val tag = "QuickJs"

    private var _context: QuickJSContext? = null
    private var _handlerThread: HandlerThread? = null
    private var _handler: Handler? = null

    fun start() {
        destroy()

        val handlerThread = HandlerThread("Thread - ${extension.nameWithoutExtension}").apply {
            start()
            _handlerThread = this
        }

        val handler = Handler(handlerThread.looper).apply { _handler = this }

        handler.post {
            val context = QuickJSContext.create().apply { _context = this }

            context.globalObject.setProperty("__dirname", extension.parent)
            context.globalObject.setProperty("__filename", extension.absolutePath)
            context.globalObject.setProperty("module", context.createNewJSObject())
            context.globalObject.setProperty("getUrl") { "https://www.baidu.com" }
            context.globalObject.setProperty("request") { params ->
                val param = params.paramAsObj(0, true)

                context.createPromise(handler) {
                    requireNotNull(param) { "request(data) data must not be null!" }
                    QuickJsBridge.requestImpl(context, param)
                }
            }

            context.globalObject.setProperty("setTimeout") {
                val runnable = it.paramAsMethod(0, true)
                val delayTime = it.paramAsLong(0)
                handler.postDelayed({ runnable?.call() }, delayTime)
                true
            }

            context.setConsole(object : QuickJSContext.Console {

                override fun log(info: String?) {
                    Log.d(tag, info!!)
                }

                override fun info(info: String?) {
                    Log.i(tag, info!!)
                }

                override fun warn(info: String?) {
                    Log.w(tag, info!!)
                }

                override fun error(info: String?) {
                    Log.e(tag, info!!)
                }
            })

            context.evaluate(extension.readText(), "index.js")
        }
    }

    fun call(name: String, callback: (String) -> Unit) {
        _handler?.post {
            val context = _context ?: return@post
            val evaluate = context.evaluate("module.exports.extension.$name")
            if (evaluate is JSObject) {
                callback(evaluate.stringify())
            } else {
                callback(evaluate.toString())
            }
        }
    }

    /**
     * 销毁 QuickJSContext 并停止 HandlerThread
     */
    fun destroy() {
        _context?.destroy()
        _context = null

        _handlerThread?.quitSafely()
        _handlerThread = null
    }
}