package com.xiaoyv.flexiflix.extension.java.network.converter

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.Type


/**
 * Class: [WebHtmlConverter]
 *
 * Retrofit 适配器，将响应数据转为 Html 字符串
 *
 * @author why
 * @since 11/24/23
 */
class WebHtmlConverter : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        return if (type == String::class.java) ItemConverter else null
    }

    private object ItemConverter : Converter<ResponseBody, String> {
        @Throws(IOException::class)
        override fun convert(value: ResponseBody): String {
            return value.use { it.string() }
        }
    }

    companion object {
        fun create(): WebHtmlConverter {
            return WebHtmlConverter()
        }
    }
}