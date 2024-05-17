package com.xiaoyv.flexiflix.extension.impl.java.network.interceptor

import android.util.Base64
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException

/**
 * [Base64ImageInterceptor] 将 base64 编码的图片转为本地链接加载适配
 *
 * http://localhost/api/image?data={Base64ImageData}
 *
 * Base64ImageData 示例：[data:image/jpeg;base64,xxx-xxx-xxx]
 *
 * @author why
 * @since 5/17/24
 */
class Base64ImageInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val httpUrl = request.url
        if (httpUrl.host == "localhost" && httpUrl.encodedPath == "/api/image") {
            val data = httpUrl.queryParameter("data").orEmpty()
            if (data.isNotBlank() && data.contains(",")) {
                runCatching {
                    val mimeType = data
                        .substring(data.indexOf(":") + 1, data.indexOf(";"))
                        .toMediaTypeOrNull() ?: "image/*".toMediaType()

                    val afterLast = data.substringAfterLast(",").replace(" ", "+")
                    val decode = Base64.decode(afterLast, Base64.DEFAULT)
                    return Response.Builder()
                        .request(request)
                        .message("success")
                        .code(200)
                        .protocol(Protocol.HTTP_1_1)
                        .body(decode.toResponseBody(mimeType))
                        .build()
                }.getOrElse {
                    throw IOException(it)
                }
            }
        }

        return chain.proceed(request)
    }

    companion object {

        /**
         * 包装 base64Data 图片为本地链接加载
         */
        @JvmStatic
        fun wrapBase64Image(base64Data: String): String {
            return "http://localhost/api/image?data=$base64Data"
        }
    }
}