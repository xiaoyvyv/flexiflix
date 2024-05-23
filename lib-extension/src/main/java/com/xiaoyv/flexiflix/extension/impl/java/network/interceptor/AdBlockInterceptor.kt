package com.xiaoyv.flexiflix.extension.impl.java.network.interceptor

import android.util.Log
import com.xiaoyv.flexiflix.extension.BuildConfig
import com.xiaoyv.flexiflix.extension.config.settings.AppSettings
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import kotlin.math.ceil

/**
 * [AdBlockInterceptor] M3U8 去除广告
 *
 * @author why
 * @since 5/11/24
 */
class AdBlockInterceptor : Interceptor {
    private val String.tsSeq: Long
        get() = "(\\d+).ts".toRegex().find(this)?.groupValues.orEmpty()
            .getOrNull(1).orEmpty().toLongOrNull() ?: 0

    override fun intercept(chain: Interceptor.Chain): Response {
        // 关闭直接跳过
        if (!AppSettings.Beta.blockM3u8Ad) {
            return chain.proceed(chain.request())
        }

        // 只处理 m3u8
        val request = chain.request()
        val string = request.url.toString()
        if (!string.endsWith(".m3u8", true) && !string.endsWith(".m3u", true)) {
            return chain.proceed(request)
        }

        val response = chain.proceed(request)
        val responseBody = response.body

        // 离群检测算法去广告
        val newBody = blockAdByOutliers(responseBody.string())
            .toResponseBody(responseBody.contentType())

        return response.newBuilder()
            .body(newBody)
            .build()
    }

    /**
     * 离群检测算法去广告，通过 ts 匹配序列号实现
     */
    private fun blockAdByOutliers(content: String): String {
        val tsArray = content.split("\n").filter { it.endsWith(".ts") }
        val tsSeqArray = tsArray.map { it.tsSeq }
        val filterTsSeqArray = removeOutliers(tsSeqArray)

        val tmp = mutableListOf<String>()
        val strings = content.split("\n")
        strings.forEach { old ->
            tmp.add(old)

            // 移除目标 .ts 和其前一个
            if (old.endsWith(".ts") && !filterTsSeqArray.contains(old.tsSeq)) {
                tmp.removeLastOrNull()
                tmp.removeLastOrNull()

                if (tmp.lastOrNull() == "#EXT-X-DISCONTINUITY") {
                    tmp.removeLastOrNull()
                }
            }
        }
        val newContent = tmp.joinToString("\n")
        if (BuildConfig.DEBUG && content != newContent) {
            Log.e(javaClass.simpleName, "原始数据：${tsSeqArray.joinToString(",")}")
            Log.e(javaClass.simpleName, "去广数据：${filterTsSeqArray.joinToString(",")}")
        }
        return newContent
    }

    /**
     * 四分位距，离群值检测算法剔除广告片段
     */
    private fun removeOutliers(data: List<Long>): List<Long> {
        if (data.isEmpty()) return data

        fun percentile(data: List<Long>, percentile: Long): Double {
            val index = percentile / 100.0 * (data.size - 1)
            val lower = data[index.toInt()]
            val upper = data[ceil(index).toInt()]
            return lower + (upper - lower) * (index - index.toInt())
        }

        val sortedData = data.sorted()
        val q1 = percentile(sortedData, 25)
        val q3 = percentile(sortedData, 75)
        val iqr = q3 - q1
        val lowerBound = q1 - 1.5 * iqr
        val upperBound = q3 + 1.5 * iqr

        return sortedData.filter { it >= lowerBound && it <= upperBound }
    }
}