package com.xiaoyv.flexiflix.extension.javascript

import android.util.Log
import com.whl.quickjs.wrapper.JSObject
import com.whl.quickjs.wrapper.QuickJSContext
import com.xiaoyv.flexiflix.extension.java.MediaSourceFactory
import com.xiaoyv.flexiflix.extension.java.utils.toJson
import com.xiaoyv.flexiflix.extension.javascript.utils.objMap
import com.xiaoyv.flexiflix.extension.javascript.utils.string
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap
import java.io.File


/**
 * [QuickJsBridge]
 *
 * @author why
 * @since 5/14/24
 */
object QuickJsBridge {
    private interface Request {
        @GET("{url}")
        suspend fun get(
            @Path("url", encoded = true) url: String,
            @HeaderMap headers: Map<String, String> = emptyMap(),
            @QueryMap query: Map<String, String> = emptyMap(),
        ): Response<ResponseBody>

        @FormUrlEncoded
        @POST("{url}")
        suspend fun postForm(
            @Path("url", encoded = true) url: String,
            @HeaderMap headers: Map<String, String> = emptyMap(),
            @FieldMap fieldMap: Map<String, String> = emptyMap(),
        ): Response<ResponseBody>
    }

    private val request by lazy {
        MediaSourceFactory.retrofitBuilder
            .baseUrl("http://localhost:3000")
            .build()
            .create(Request::class.java)
    }

    @JvmStatic
    fun run(extension: File, function: String, callback: QuickJsExtension.(String) -> Unit) {
        val ex = QuickJsExtension(extension)
        ex.start()
        val time = System.currentTimeMillis()
        ex.call(function) {
            callback(ex, it)
            Log.e("QuickJs", "time: ${System.currentTimeMillis() - time}ms")
        }
    }

    /**
     * QuickJs 网络请求全局函数实现
     */
    suspend fun requestImpl(jsContext: QuickJSContext, param: JSObject): JSObject {
        val url = param.string("url")
        val method = param.string("method")
        val data = param.objMap("data")
        val headers = param.objMap("headers")
        var res: Response<ResponseBody>? = null

        if (method.isBlank() || method.equals("get", true)) {
            res = request.get(
                url = url,
                headers = headers.mapValues { it.toString() },
                query = data.mapValues { it.toString() }
            )
        }

        if (method.equals("post", true)) {
            res = request.postForm(
                url = url,
                headers = headers.mapValues { it.toString() },
                fieldMap = data.mapValues { it.toString() }
            )
        }

        val response = requireNotNull(res) { "Not support for $method" }

        return response.toJsObject(jsContext)
    }

    private fun Response<ResponseBody>?.toJsObject(jsContext: QuickJSContext): JSObject {
        this ?: return jsContext.createNewJSObject()
        val anyMap = mapOf(
            "headers" to headers().toMap(),
            "code" to code(),
            "message" to message(),
            "data" to (body() ?: errorBody())?.string()
        )
        return jsContext.parse(anyMap.toJson()) as JSObject
    }
}