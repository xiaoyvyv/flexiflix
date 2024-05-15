@file:Suppress("unused", "SpellCheckingInspection")

package com.xiaoyv.extension.yhdm.iyinghua.io

import com.xiaoyv.flexiflix.extension.java.annotation.MediaSource
import com.xiaoyv.flexiflix.extension.java.model.FlexMediaDetail
import com.xiaoyv.flexiflix.extension.java.model.FlexMediaDetailSeries
import com.xiaoyv.flexiflix.extension.java.model.FlexMediaDetailTab
import com.xiaoyv.flexiflix.extension.java.model.FlexMediaSection
import com.xiaoyv.flexiflix.extension.java.model.FlexMediaSectionItem
import com.xiaoyv.flexiflix.extension.java.model.FlexMediaPlaylist
import com.xiaoyv.flexiflix.extension.java.model.FlexMediaPlaylistUrl
import com.xiaoyv.flexiflix.extension.java.model.FlexMediaTag
import com.xiaoyv.flexiflix.extension.java.model.FlexMediaUser
import com.xiaoyv.flexiflix.extension.java.source.HttpParseSource
import com.xiaoyv.flexiflix.extension.java.utils.UNKNOWN_LONG
import com.xiaoyv.flexiflix.extension.java.utils.UNKNOWN_STRING
import com.xiaoyv.flexiflix.extension.java.utils.parseNumberStr
import com.xiaoyv.flexiflix.extension.java.utils.regex
import com.xiaoyv.flexiflix.extension.java.utils.runCatchingPrint

/**
 * [IyingHuaSource]
 *
 * @author why
 * @since 5/11/24
 */
@MediaSource(
    id = "iyinghua.io",
    name = "樱花动漫源 - iyinghua.io",
    author = "xiaoyv",
    description = "樱花动漫源（http://www.iyinghua.io）",
    nsfw = false
)
class IyingHuaSource : HttpParseSource() {
    override val baseUrl: String
        get() = "http://www.iyinghua.io"

    private val iyhdmmApi by lazy {
        retrofit.create(IyingHuaApi::class.java)
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
                            overlay = FlexMediaSectionItem.OverlayText(
                                topStart = p?.text().orEmpty(),
                                bottomEnd = duration
                            ),
                            layout = FlexMediaSectionItem.ImageLayout(
                                widthDp = 140,
                                aspectRatio = 198 / 275f
                            )
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
                            overlay = FlexMediaSectionItem.OverlayText(
                                bottomEnd = media.select("font").text(),
                            ),
                            layout = FlexMediaSectionItem.ImageLayout(
                                widthDp = 140,
                                aspectRatio = 198 / 275f
                            )
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

    override suspend fun fetchMediaDetail(
        id: String,
        extras: Map<String, String>
    ): Result<FlexMediaDetail> {
        return runCatchingPrint {
            val document = iyhdmmApi.qeuryDetail(id)
            val fire = document.select(".fire")
            val sinfo = fire.select(".sinfo")
            val title = fire.select(".rate h1").text()
            val cover = fire.select(".thumb img").attr("src")

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

            val playlistTitles = fire.select(".tabs #menu0 li").map { it.text() }
            val playlist = fire.select("div[class=movurl]").mapIndexedNotNull { index, item ->
                val items = item.select("ul li")
                if (items.isEmpty()) return@mapIndexedNotNull null

                FlexMediaPlaylist(
                    title = playlistTitles.getOrNull(index).orEmpty().ifBlank { "默认" },
                    items = items.mapNotNull { media ->
                        FlexMediaPlaylistUrl(
                            id = media.select("a").attr("href")
                                .orEmpty()
                                .substringAfterLast("/")
                                .substringBeforeLast("."),
                            title = media.select("a").text(),
                            mediaUrl = UNKNOWN_STRING
                        )
                    }
                )
            }

            FlexMediaDetail(
                id = id,
                title = title,
                description = fire.select(".info").text(),
                cover = cover,
                url = "$baseUrl/show/$id.html",
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
                playlist = playlist,
                series = emptyList()
            )
        }
    }

    override suspend fun fetchMediaRawUrl(playlistUrl: FlexMediaPlaylistUrl): Result<FlexMediaPlaylistUrl> {
        return runCatchingPrint {
            val player = iyhdmmApi.qeuryDetailPlayer(playlistUrl.id)
            val epTitle = player.regex("script", "addPlayHistory\\('(.*?)'", 1)
            val videoUrl = player.select("div[data-vid]").attr("data-vid")
                .substringBefore("$")
                .trim()

            playlistUrl.copy(title = epTitle, mediaUrl = videoUrl)
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