/*
@file:Suppress("unused", "SpellCheckingInspection")

package com.xiaoyv.extension.yhdm.iyhdmm.com

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.xiaoyv.flexiflix.extension.java.model.FlexMediaDetail
import com.xiaoyv.flexiflix.extension.java.model.FlexMediaDetailSeries
import com.xiaoyv.flexiflix.extension.java.model.FlexMediaDetailTab
import com.xiaoyv.flexiflix.extension.java.model.FlexMediaSection
import com.xiaoyv.flexiflix.extension.java.model.FlexMediaSectionItem
import com.xiaoyv.flexiflix.extension.java.model.FlexMediaSource
import com.xiaoyv.flexiflix.extension.java.model.FlexMediaTag
import com.xiaoyv.flexiflix.extension.java.model.FlexMediaUser
import com.xiaoyv.flexiflix.extension.java.source.HttpParseSource
import com.xiaoyv.flexiflix.extension.java.utils.UNKNOWN_LONG
import com.xiaoyv.flexiflix.extension.java.utils.UNKNOWN_STRING
import com.xiaoyv.flexiflix.extension.java.utils.gson
import com.xiaoyv.flexiflix.extension.java.utils.parseNumberStr
import com.xiaoyv.flexiflix.extension.java.utils.regex
import com.xiaoyv.flexiflix.extension.java.utils.runCatchingPrint
import okhttp3.Cookie
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.jsoup.nodes.Document
import java.net.URLDecoder
import java.net.URLEncoder
import kotlin.math.roundToLong

*/
/**
 * [IyhdmmSource]
 *
 * @author why
 * @since 5/10/24
 *//*

class IyhdmmSource : HttpParseSource() {
    override val baseUrl: String
        get() = "https://www.iyhdmm.com"

    override val ignoreCertificate: Boolean
        get() = true

    private val iyhdmmApi by lazy {
        retrofit.create(IyhdmmApi::class.java)
    }

    override suspend fun fetchHomeSections(): Result<List<FlexMediaSection>> {
        return runCatchingPrint {
            val document = iyhdmmApi.queryHomeHtml()
            val mediaSections = mutableListOf<FlexMediaSection>()

            val banner = FlexMediaSection(
                id = UNKNOWN_STRING,
                title = "Hot Banner",
                items = document.select(".heros li").map { item ->
                    FlexMediaSectionItem(
                        id = item.select("a").attr("href").parseNumberStr(),
                        cover = item.select("img").attr("src"),
                        title = item.select("p").text(),
                        description = item.select("em").text(),
                    )
                }
            )

            val area = document.select(".firs > .dtit").mapNotNull { item ->
                val element = item.nextElementSibling() ?: return@mapNotNull null

                FlexMediaSection(
                    id = item.select("a").attr("href"),
                    title = item.select("h2").text(),
                    items = element.select("ul > li").map { media ->
                        val p = media.select("p").lastOrNull()
                        val duration = p?.select("font")?.remove()?.text().orEmpty()

                        FlexMediaSectionItem(
                            id = media.select(".tname > a").attr("href").parseNumberStr(),
                            title = media.select(".tname").text(),
                            cover = media.select("img").attr("src"),
                            description = media.select("p").text(),
                            overlayBL = p?.text().orEmpty(),
                            overlayBR = duration,
                            widthDp = 140,
                            aspectRatio = 198 / 275f
                        )
                    }
                )
            }

            val side = document.select(".side.r .pics").let { item ->
                val element = item.firstOrNull()
                val header = element?.previousElementSibling()

                FlexMediaSection(
                    id = header?.select("a")?.attr("href").orEmpty(),
                    title = header?.select("h2")?.text().orEmpty(),
                    items = element?.select("ul > li")?.map { media ->
                        FlexMediaSectionItem(
                            id = media.select("h2 > a").attr("href").parseNumberStr(),
                            cover = media.select("img").attr("src"),
                            title = media.select("h2").text(),
                            description = media.select("p").text(),
                            overlayBL = media.select("font").text(),
                            widthDp = 140,
                            aspectRatio = 198 / 275f
                        )
                    }.orEmpty()
                )
            }

            mediaSections.add(banner)
            mediaSections.addAll(area)
            mediaSections.add(side)
            mediaSections
        }
    }

    override suspend fun fetchSectionMediaPages(
        sectionId: String,
        sectionExtras: Map<String, String>,
        page: Int
    ): Result<List<FlexMediaSectionItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchUserMediaPages(user: FlexMediaUser): Result<List<FlexMediaSectionItem>> {
        TODO("Not yet implemented")
    }

    private val cahce = HashMap<String, Document>()

    override suspend fun fetchMediaDetail(
        id: String,
        extras: Map<String, String>
    ): Result<FlexMediaDetail> {
        return runCatchingPrint {
            val rawMediaId = id.substringBefore("-")
            val mediaUrl = "$baseUrl/showp/${rawMediaId}.html"

            val document = cahce.getOrPut(rawMediaId) {
                iyhdmmApi.qeuryDetail(rawMediaId)
            }

            val titile = document.regex("script", "addPlayHistory\\('(.*?)'", 1)
            val playerUrl = if (id.contains("-")) {
                fetchMediaUrl(id, titile)
            } else {
                val movurl = document.select("div[class=movurl]")
                val selectMovurl = movurl.select("div[class=movurl]")
                    .maxByOrNull { it.select("ul > li").size }
                val selectEp = selectMovurl?.select("ul > li").orEmpty().firstOrNull()
                val parseId = selectEp?.select("a")?.attr("href")
                    .orEmpty()
                    .substringAfterLast("/")
                    .substringBeforeLast(".")

                fetchMediaUrl(parseId, titile)
            }

            val fire = document.select(".fire")
            val sinfo = fire.select(".sinfo")
            val title = fire.select(".rate h1").text()

            val createAt = sinfo.select("span")
                .find { it.text().contains("上映") }?.text()
                .orEmpty()
                .substringAfterLast(":").trim()

            val tags = sinfo.select("span")
                .find { it.text().contains("标签") }?.select("a")
                .orEmpty()
                .map { FlexMediaTag(id = it.attr("href"), name = it.text()) }

            val type = sinfo.select("span")
                .find { it.text().contains("类型") }?.text()
                .orEmpty()
                .substringAfterLast(":").trim()
            val cover = fire.select(".thumb img").attr("src")

            val detailSeries = fire.select("div[class=movurl]").mapNotNull { item ->
                val items = item.select("ul li")
                if (items.isEmpty()) return@mapNotNull null

                FlexMediaDetailSeries(
                    mediaId = rawMediaId,
                    count = items.size,
                    items = items.mapNotNull { media ->
                        FlexMediaSectionItem(
                            id = media.select("a").attr("href")
                                .orEmpty()
                                .substringAfterLast("/")
                                .substringBeforeLast("."),
                            title = media.select("a").text(),
                            cover = cover,
                            description = UNKNOWN_STRING
                        )
                    }
                )
            }

            FlexMediaDetail(
                id = rawMediaId,
                title = title,
                description = fire.select(".info").text(),
                cover = fire.select(".thumb img").attr("src"),
                url = mediaUrl,
                createAt = createAt,
                size = UNKNOWN_STRING,
                duration = UNKNOWN_LONG,
                type = type,
                publisher = FlexMediaUser(
                    id = UNKNOWN_STRING,
                    avatar = cover,
                    name = title
                ),
                tags = tags,
                source = listOf(playerUrl),
                series = detailSeries
            )
        }
    }

    override suspend fun fetchMediaDetailRelative(
        relativeTab: FlexMediaDetailTab,
        id: String,
        extras: Map<String, String>
    ): Result<List<FlexMediaSection>> {
        TODO("Not yet implemented")
    }

    private suspend fun fetchMediaUrl(parseId: String, titile: String): FlexMediaSource {
        val split = parseId.split("-")
        require(split.size == 3) { "查询播放链接的ID错误：$parseId" }
        val meidaId = split[0]
        val playIndex = split[1]
        val epIndex = split[2]
        val r = "0." + System.currentTimeMillis().toString() + "000"
        val referer = "$baseUrl/vp/$meidaId-$playIndex-$epIndex.html"

        iyhdmmApi.qeuryDetailPlayer(mediaAndSourceId = parseId)
        // 注入 cookie var code_name = escape(name) + "^",code_url = escape(url) + "_$_"
        computeCookie(referer, titile)
        val encrypt = iyhdmmApi.queryM3u8(referer = referer, meidaId, playIndex, epIndex, r = r)
        val descyptUrl = descyptUrl(encrypt)

        // https://www.iyhdmm.com/playurl?aid=21267&playindex=1&epindex=1&r=0.5695767296244953
        // https://www.iyhdmm.com/playurl?aid=21267&playindex=1&epindex=0&r=0.1715426714414000
        // https://vip.ffzy-play5.com/20221105/1094_b904a25c/index.m3u8
        // https://vip.ffzy-online2.com/20230103/5924_4b41c456/index.m3u8
        debugLog { "解码 URL = $descyptUrl" }

        return FlexMediaSource(
            type = "m3u8",
            url = descyptUrl,
            size = ""
        )
    }

    */
/**
 * 获取视频 url 前，算法注入 cookie，流程解密自：https://www.iyhdmm.com/tpsf/js/pck.js?ver=215139
 *//*

    private fun computeCookie(referer: String, titile: String) {
        val httpUrl = baseUrl.toHttpUrl()
        val cookies = cookieJar.loadForRequest(httpUrl)
        val rand = (System.currentTimeMillis() / 1000f).roundToLong() shr 17
        val m2t =
            ((((rand * 21 + 154) * ((rand % 64) + 13)) * (rand % 32 + 34) * ((rand % 16) + 87) * ((rand % 8) + 65)) + 751)
        debugLog { "rand: $rand,m2t:$m2t" }

        val t1 = cookies.find { it.name == "t1" }?.value?.toLongOrNull() ?: 0L
        val t1Seconds = (t1 / 1000f).roundToLong()
        val t1SecondsShr5 = t1Seconds shr 5
        val fixValue = 35236
        val k2 =
            (((((t1SecondsShr5 * (t1SecondsShr5 % 256) + 1) + fixValue) * (t1SecondsShr5 % 128 + 1)) * (t1SecondsShr5 % 16 + 1)) + t1SecondsShr5).toString()

        var t2: String
        while (true) {
            t2 = System.currentTimeMillis().toString()
            val randTimeLast3Str = t2.substring(t2.length - 3)
            val k2LastChar = k2.substring(k2.length - 1)
            if ((randTimeLast3Str.indexOf(k2LastChar) >= 0)) {
                break
            }
        }

        cookieJar.saveFromResponse(
            httpUrl,
            listOf(
                requireNotNull(Cookie.parse(httpUrl, "m2t=$m2t")),
                requireNotNull(Cookie.parse(httpUrl, "k2=$k2")),
                requireNotNull(Cookie.parse(httpUrl, "t2=$t2")),
                requireNotNull(
                    Cookie.parse(
                        httpUrl,
                        "qike123=${
                            URLEncoder.encode(
                                titile,
                                "utf-8"
                            )
                        }^${URLEncoder.encode(referer, "utf-8")}_\$_|"
                    )
                )
            )
        )
    }

    */
/**
 * 解密 Url
 *//*

    private fun descyptUrl(encodeString: String): String {

        */
/**
 * 解密算法，流程解密自：https://www.iyhdmm.com/tpsf/js/pck.js?ver=215139
 *//*

        fun decode(encodeString: String): String {
            var decodedString = encodeString
            if (!encodeString.startsWith("{") && !encodeString.startsWith("}")) {
                var tmp = ""
                val fix1561 = 1561
                val strLength = encodeString.length
                var i = 0
                while (i < strLength) {
                    var valueBase10 = encodeString.substring(i, i + 2).toInt(16)
                    valueBase10 =
                        (((valueBase10 + 1048576) - fix1561) - (strLength / 2.0 - 1 - i / 2.0)).toInt() % 256
                    tmp = (valueBase10.toChar() + tmp)
                    i += 2
                }
                decodedString = tmp
            }
            return decodedString
        }

        val json = gson.fromJson(decode(encodeString), UrlInfo::class.java)
        return URLDecoder.decode(json.vurl, "utf-8")
    }

    @Keep
    private data class UrlInfo(
        @SerializedName("inv") var inv: String? = null,
        @SerializedName("purl") var purl: String? = null,
        @SerializedName("vurl") var vurl: String? = null
    )
}*/
