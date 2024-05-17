@file:Suppress("SpellCheckingInspection")

package com.xiaoyv.extension.hanime

import com.xiaoyv.flexiflix.extension.impl.java.annotation.MediaSource
import com.xiaoyv.flexiflix.extension.model.FlexMediaDetail
import com.xiaoyv.flexiflix.extension.model.FlexMediaDetailSeries
import com.xiaoyv.flexiflix.extension.model.FlexMediaDetailTab
import com.xiaoyv.flexiflix.extension.model.FlexMediaSection
import com.xiaoyv.flexiflix.extension.model.FlexMediaSectionItem
import com.xiaoyv.flexiflix.extension.model.FlexMediaPlaylist
import com.xiaoyv.flexiflix.extension.model.FlexMediaPlaylistUrl
import com.xiaoyv.flexiflix.extension.model.FlexMediaTag
import com.xiaoyv.flexiflix.extension.model.FlexMediaUser
import com.xiaoyv.flexiflix.extension.source.HttpParseSource
import com.xiaoyv.flexiflix.extension.utils.UNKNOWN_LONG
import com.xiaoyv.flexiflix.extension.utils.UNKNOWN_STRING
import com.xiaoyv.flexiflix.extension.utils.childText
import com.xiaoyv.flexiflix.extension.utils.hrefParam
import com.xiaoyv.flexiflix.extension.utils.metaContentByProperty
import com.xiaoyv.flexiflix.extension.utils.parseNumber
import com.xiaoyv.flexiflix.extension.utils.regex
import com.xiaoyv.flexiflix.extension.utils.runCatchingPrint
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.jsoup.nodes.Element

/**
 * [HanimeSource]
 *
 * @author why
 * @since 5/8/24
 */
@MediaSource(
    id = "hanime.com",
    name = "Hanime",
    author = "xiaoyv",
    description = "Datasource for Hanime",
    nsfw = true
)
class HanimeSource : HttpParseSource() {

    override val baseUrl: String
        get() = "https://hanime1.me"

    /**
     * 自定义 DNS
     */
    override val dnsMap: Map<String, String>
        get() = mapOf(
            "hanime1.me" to "172.67.69.183",
            "www.hanime1.me" to "172.67.69.183",
            "vdownload.hembed.com" to "89.187.183.13"
        )

    private val hanimeApi by lazy {
        retrofit.create(HanimeApi::class.java)
    }

    override suspend fun fetchHomeSections(): Result<List<FlexMediaSection>> {
        return runCatchingPrint {
            val document = hanimeApi.queryHomeHtml()
            val sections = arrayListOf<FlexMediaSection>()

            val hiddenXs = document.select(".nav-bottom-padding > .hidden-xs")
            val bannerCover = hiddenXs.select("div > img").lastOrNull()?.attr("src")
            val bannerSection = hiddenXs.select("#home-banner-wrapper").let {
                FlexMediaSection(
                    id = UNKNOWN_STRING,
                    title = "Hot Banner",
                    items = listOf(
                        FlexMediaSectionItem(
                            title = it.select("h1").text(),
                            description = it.select("h4").text(),
                            id = it.select(".home-banner-play-btn").hrefParam("v"),
                            cover = bannerCover.orEmpty(),
                        )
                    )
                )
            }

            val mediaSections = document.select("#home-rows-wrapper > a")
                .mapIndexedNotNull { index, element ->
                    val sectionId = element.attr("href")
                    val sectionTitle = element.text()

                    val rowItem = element.nextElementSibling() ?: return@mapIndexedNotNull null
                    val rowMedias: List<FlexMediaSectionItem>

                    val bigItems = rowItem.select(".home-rows-videos-wrapper > a")
                    val smallItems =
                        rowItem.select(".home-rows-videos-wrapper > div.multiple-link-wrapper.hidden-xs")

                    if (bigItems.isNotEmpty()) {
                        rowMedias = bigItems.map { media ->
                            FlexMediaSectionItem(
                                id = media.hrefParam("v"),
                                title = media.select(".home-rows-videos-title").text(),
                                cover = media.select("img").attr("src"),
                                description = UNKNOWN_STRING,
                                layout = FlexMediaSectionItem.ImageLayout(
                                    widthDp = 140,
                                    aspectRatio = 3 / 4f
                                ),
                            )
                        }
                    } else {
                        rowMedias = smallItems.map { media -> media.toMediaItem() }
                    }

                    FlexMediaSection(
                        id = sectionId,
                        title = sectionTitle,
                        items = rowMedias
                    )
                }

            sections.add(0, bannerSection)
            sections.addAll(mediaSections)

            sections
        }
    }

    /**
     * 这里源的 sectionId 真实数据是 url
     */
    override suspend fun fetchSectionMediaPages(
        sectionId: String,
        sectionExtras: Map<String, String>,
        page: Int
    ): Result<List<FlexMediaSectionItem>> {
        return runCatchingPrint {
            if (sectionId.isBlank()) return@runCatchingPrint emptyList()
            val httpUrl = sectionId.toHttpUrl()
            val queryMap = httpUrl.queryParameterNames
                .associateWith { httpUrl.queryParameter(it) }
                .toMutableMap()

            queryMap["page"] = page.toString()

            val document = hanimeApi.searchWithQueryMap(queryMap)

            document.select(".content-padding-new .search-doujin-videos.hidden-xs")
                .map { media -> media.toMediaItem() }
        }
    }

    override suspend fun fetchUserMediaPages(user: FlexMediaUser): Result<List<FlexMediaSectionItem>> {
        return runCatchingPrint {


            listOf()
        }
    }

    override suspend fun fetchMediaDetail(
        id: String,
        extras: Map<String, String>
    ): Result<FlexMediaDetail> {
        return runCatchingPrint {
            val document = hanimeApi.queryMediaDetail(id)
            val descriptionPanel =
                document.select(".video-details-wrapper > .video-description-panel")
            val countTime = descriptionPanel.select(".hidden-xs").text().split("\\s+".toRegex())
            val tagsWrapper = document.select(".video-tags-wrapper .single-video-tag")
            val userWrapper = document.select(".desktop-inline-mobile-block").firstOrNull()
            val player = document.select("video#player")
            val playlist = document.select(".hidden-xs > #video-playlist-wrapper")

            FlexMediaDetail(
                id = id,
                title = document.metaContentByProperty("og:title"),
                description = document.metaContentByProperty("og:description"),
                cover = document.metaContentByProperty("og:image"),
                url = document.metaContentByProperty("og:url"),
                playCount = countTime.getOrNull(0).orEmpty(),
                createAt = countTime.getOrNull(1).orEmpty(),
                duration = UNKNOWN_LONG,
                size = document.regex("head", "File size:(.*?)\n", 1),
                type = userWrapper?.select(".hidden-xs a")?.text().orEmpty(),
                publisher = FlexMediaUser(
                    id = userWrapper?.select("#video-artist-name")?.attr("href").orEmpty(),
                    name = userWrapper?.select("#video-artist-name")?.text().orEmpty(),
                    avatar = userWrapper?.select("#video-user-avatar")?.attr("src").orEmpty(),
                    role = UNKNOWN_STRING
                ),
                playlist = player.select("source").map { source ->
                    FlexMediaPlaylist(
                        title = source.attr("size"),
                        items = listOf(
                            FlexMediaPlaylistUrl(
                                id = source.attr("size"),
                                title = source.attr("size"),
                                type = source.attr("type"),
                                mediaUrl = source.attr("src"),
                                size = source.attr("size"),
                            )
                        )
                    )
                },
                series = listOf(
                    FlexMediaDetailSeries(
                        mediaId = id,
                        count = playlist.select("h4").lastOrNull()?.text().parseNumber(),
                        items = playlist.select(".related-watch-wrap").map { media ->
                            media.toMediaItem()
                        }
                    )
                ),
                tags = tagsWrapper.mapNotNull {
                    val url = baseUrl + it.select("a").attr("href")
                    if (url.isBlank()) null else FlexMediaTag(id = url, name = it.text())
                },
                relativeTabs = listOf(
                    FlexMediaDetailTab(
                        id = "r",
                        mediaId = id,
                        title = "相关影片"
                    ),
                    FlexMediaDetailTab(
                        id = "c",
                        mediaId = id,
                        title = "评论"
                    ),
                ),
            )
        }
    }

    override suspend fun fetchMediaRawUrl(playlistUrl: FlexMediaPlaylistUrl): Result<FlexMediaPlaylistUrl> {
        return runCatchingPrint { playlistUrl }
    }

    override suspend fun fetchMediaDetailRelative(
        relativeTab: FlexMediaDetailTab,
        id: String,
        extras: Map<String, String>
    ): Result<List<FlexMediaSection>> {
        return runCatchingPrint {


            listOf()
        }
    }

    private fun Element.toMediaItem(): FlexMediaSectionItem {
        val media = this
        return FlexMediaSectionItem(
            id = media.select("a.overlay").hrefParam("v"),
            title = media.select(".card-mobile-title").text(),
            user = FlexMediaUser(
                id = media.select(".card-mobile-user")
                    .hrefParam("query"),
                name = media.select(".card-mobile-user").text(),
                avatar = UNKNOWN_STRING,
                role = UNKNOWN_STRING
            ),
            description = UNKNOWN_STRING,
            cover = media.select("img").lastOrNull()?.attr("src")
                .orEmpty(),
            overlay = FlexMediaSectionItem.OverlayText(
                bottomEnd = media.select(".card-mobile-duration")
                    .childText(0),
                bottomStart = media.select(".card-mobile-duration")
                    .childText(1),
            ),
            layout = FlexMediaSectionItem.ImageLayout(
                aspectRatio = 16 / 9f
            )
        )
    }
}