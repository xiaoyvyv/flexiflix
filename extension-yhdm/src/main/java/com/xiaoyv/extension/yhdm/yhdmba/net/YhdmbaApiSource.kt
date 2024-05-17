@file:Suppress("SpellCheckingInspection")

package com.xiaoyv.extension.yhdm.yhdmba.net

import android.util.Base64
import android.webkit.URLUtil
import com.xiaoyv.flexiflix.extension.impl.java.annotation.MediaSource
import com.xiaoyv.flexiflix.extension.model.FlexMediaDetail
import com.xiaoyv.flexiflix.extension.model.FlexMediaDetailTab
import com.xiaoyv.flexiflix.extension.model.FlexMediaPlaylist
import com.xiaoyv.flexiflix.extension.model.FlexMediaPlaylistUrl
import com.xiaoyv.flexiflix.extension.model.FlexMediaSection
import com.xiaoyv.flexiflix.extension.model.FlexMediaSectionItem
import com.xiaoyv.flexiflix.extension.model.FlexMediaUser
import com.xiaoyv.flexiflix.extension.source.HttpParseSource
import com.xiaoyv.flexiflix.extension.utils.UNKNOWN_LONG
import com.xiaoyv.flexiflix.extension.utils.UNKNOWN_STRING
import com.xiaoyv.flexiflix.extension.utils.gson
import com.xiaoyv.flexiflix.extension.utils.parseNumberStr
import com.xiaoyv.flexiflix.extension.utils.runCatchingPrint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URLDecoder


/**
 * [YhdmbaApiSource]
 *
 * @author why
 * @since 5/11/24
 */
@MediaSource(
    id = "yhdmba.net",
    name = "樱花动漫源 - yhdmba.net",
    author = "xiaoyv",
    description = "樱花动漫源（http://www.yhdmba.net）",
    nsfw = false
)
class YhdmbaApiSource : HttpParseSource() {
    override val baseUrl: String
        get() = "http://www.yhdmba.net"

    private val yhdmbaApi by lazy {
        retrofit.create(YhdmbaApi::class.java)
    }

    override val dnsMap: Map<String, String>
        get() = mapOf(
            "yhdmba.net" to "103.135.32.155",
            "www.yhdmba.net" to "103.135.32.155",
        )

    override suspend fun fetchHomeSections(): Result<List<FlexMediaSection>> {
        val document = yhdmbaApi.queryHomeHtml()
        return runCatchingPrint {
            document.select(".myui-panel_bd > .col-lg-wide-75").map { item ->
                val section = item.select(".myui-panel_hd")
                val vodlist = item.select(".myui-vodlist > li")


                FlexMediaSection(
                    id = section.select("a").attr("href").parseNumberStr(),
                    title = section.select("h3").text(),
                    items = vodlist.map { meida ->
                        FlexMediaSectionItem(
                            id = meida.select("a.myui-vodlist__thumb")
                                .attr("href")
                                .parseNumberStr(),

                            cover = meida.select("a.myui-vodlist__thumb")
                                .attr("data-original")
                                .let { if (URLUtil.isNetworkUrl(it)) it else "$baseUrl$it" },
                            title = meida.select("h4").text(),
                            description = meida.select("p.text").text(),
                            layout = FlexMediaSectionItem.ImageLayout(
                                widthDp = 140,
                                aspectRatio = 130 / 195f,
                            ),
                            overlay = FlexMediaSectionItem.OverlayText(
                                topStart = meida.select("span.tag").text(),
                                bottomEnd = meida.select(".pic-text.text-right").text(),
                            ),
                        )
                    }
                )
            }
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

    override suspend fun fetchMediaDetail(
        id: String,
        extras: Map<String, String>
    ): Result<FlexMediaDetail> {
        return runCatchingPrint {
            val document = yhdmbaApi.qeuryDetail(id)
            val row = document.select(".container > .row")
            val title = row.select("h1.title").text()

            val cover = row.select(".picture > img").attr("data-original")
                .let { if (URLUtil.isNetworkUrl(it)) it else baseUrl + it }

            val createAt = row.select("p.data > span")
                .find { it.text().contains("年份") }?.nextElementSibling()?.text()
                .orEmpty()

            val type = row.select("p.data > span")
                .find { it.text().contains("分类") }?.nextElementSibling()?.text()
                .orEmpty()

            val playlist = document.select("ul.nav:nth-child(3) > li > a").map {
                val seriesTitle = it.text()
                val target = document.select(it.attr("href"))
                val seriesItems = target.select("ul > li")
                FlexMediaPlaylist(
                    title = seriesTitle,
                    items = seriesItems.map { media ->
                        FlexMediaPlaylistUrl(
                            id = media.select("a").attr("href")
                                .substringAfterLast("/")
                                .substringBeforeLast("."),
                            title = media.text(),
                        )
                    }
                )
            }

            FlexMediaDetail(
                id = id,
                title = title,
                description = row.select("span.content").text(),
                cover = cover,
                url = "$baseUrl/view/$id.html",
                createAt = createAt,
                size = UNKNOWN_STRING,
                duration = UNKNOWN_LONG,
                type = type,
                publisher = FlexMediaUser(
                    id = UNKNOWN_STRING,
                    avatar = cover,
                    name = title
                ),
                tags = emptyList(),
                playlist = playlist,
                series = emptyList()
            )
        }
    }

    /**
     * 解析播放链接
     *
     * var player_aaaa={"flag":"play","encrypt":2,"trysee":0,"points":0,"link":"\/player\/11900-0-0.html","link_next":"","link_pre":"","vod_data":{"vod_name":"Wake Up, Girls! \u8d85\u8d8a\u6df1\u9650","vod_actor":"\u5409\u5ca1\u8309\u7950,\u6c38\u91ce\u611b\u7406,\u7530\u4e2d\u7f8e\u6d77,\u9752\u5c71\u5409\u80fd","vod_director":"\u5c71\u672c\u5bbd","vod_class":"\u52a8\u753b"},"url":"JTY4JTc0JTc0JTcwJTczJTNBJTJGJTJGJTczJTM5JTJFJTY2JTczJTc2JTZGJTY0JTMxJTJFJTYzJTZGJTZEJTJGJTMyJTMwJTMyJTMzJTMxJTMyJTMwJTMyJTJGJTYzJTU0JTQ5JTMwJTY1JTY3JTY0JTcwJTJGJTY5JTZFJTY0JTY1JTc4JTJFJTZEJTMzJTc1JTM4","url_next":"","from":"fsm3u8","server":"no","note":"","id":"11900","sid":1,"nid":1}</script>
     */
    override suspend fun fetchMediaRawUrl(playlistUrl: FlexMediaPlaylistUrl): Result<FlexMediaPlaylistUrl> {
        return runCatchingPrint {
            val player = yhdmbaApi.qeuryDetailPlayer(playlistId = playlistUrl.id)
            val script = player.select("script").outerHtml()
            val json = "player_aaaa=([\\s\\S]+?)</script>".toRegex()
                .find(script)?.groupValues?.getOrNull(1).orEmpty()
            val playInfo = gson.fromJson(json, YhdmbaPlayInfo::class.java)
            val descyptUrl = if (playInfo.encrypt == 1) {
                withContext(Dispatchers.IO) { URLDecoder.decode(playInfo.url.orEmpty(), "UTF-8") }
            } else {
                val decode = Base64.decode(playInfo.url.orEmpty(), Base64.NO_WRAP).decodeToString()
                withContext(Dispatchers.IO) { URLDecoder.decode(decode, "UTF-8") }
            }

            debugLog { "解码 URL = $descyptUrl" }

            playlistUrl.copy(
                mediaUrls = listOf(
                    FlexMediaPlaylistUrl.SourceUrl(
                        name = "默认",
                        rawUrl = descyptUrl,
                    )
                )
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
}