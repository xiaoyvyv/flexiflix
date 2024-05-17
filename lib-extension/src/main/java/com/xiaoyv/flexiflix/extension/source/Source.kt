package com.xiaoyv.flexiflix.extension.source

import com.xiaoyv.flexiflix.extension.model.FlexMediaPlaylistUrl
import com.xiaoyv.flexiflix.extension.model.FlexMediaSection
import com.xiaoyv.flexiflix.extension.model.FlexMediaSectionItem
import com.xiaoyv.flexiflix.extension.model.FlexMediaUser
import com.xiaoyv.flexiflix.extension.model.FlexMediaDetail
import com.xiaoyv.flexiflix.extension.model.FlexMediaDetailTab
import com.xiaoyv.flexiflix.extension.utils.runCatchingPrint

/**
 * [Source]
 *
 * @author why
 * @since 5/8/24
 */
interface Source {

    /**
     * 获取首页的分类栏位板块
     *
     * 展示效果：
     * ```
     * 【板块标题1】                    【更多>>】
     * 【封面1】【封面2】【封面3】【封面4】【封面5】...
     *
     * 【板块标题2】                    【更多>>】
     * 【封面1】【封面2】【封面3】【封面4】【封面5】...
     * ```
     *
     * 默认第一个 [FlexMediaSection] 会在顶部以 Banner 形式展示
     */
    suspend fun fetchHomeSections(): Result<List<FlexMediaSection>>

    /**
     * 获取对应的某个 section 的全部分页数据
     *
     * @param sectionId 对应 [FlexMediaSection.id]
     * @param sectionExtras 对应 [FlexMediaSection.extras]
     * @param page 页码，从 1 开始
     */
    suspend fun fetchSectionMediaPages(
        sectionId: String,
        sectionExtras: Map<String, String>,
        page: Int
    ): Result<List<FlexMediaSectionItem>>

    /**
     * 获取某个用户全部分页数据
     */
    suspend fun fetchUserMediaPages(user: FlexMediaUser): Result<List<FlexMediaSectionItem>>

    /**
     * 获取媒体条目详情
     *
     * @param id 媒体的ID
     * @param extras 通过section点击进入详情页时，传入的 [FlexMediaSectionItem.extras]
     */
    suspend fun fetchMediaDetail(id: String, extras: Map<String, String>): Result<FlexMediaDetail>

    /**
     * 此方法应用于，点击视频下方其它章节时，根据其章节ID查询真实的播放链接（如果媒体详情的播放列表已经包含视频链接，则不会调用此方法查询）
     *
     * 根据 [FlexMediaPlaylistUrl.id] 获取视频直链，只有 [FlexMediaPlaylistUrl.mediaUrls] 是空的情况才会查询
     */
    suspend fun fetchMediaRawUrl(playlistUrl: FlexMediaPlaylistUrl): Result<FlexMediaPlaylistUrl> {
        return runCatchingPrint { playlistUrl }
    }

    /**
     * 获取媒体条目详情，下方对应的推荐相关栏位板块
     *
     * @param relativeTab 详情页对应的TAB下的拉取
     * @param id 媒体的ID
     * @param extras 通过section点击进入详情页时，传入的 [FlexMediaSectionItem.extras]
     */
    suspend fun fetchMediaDetailRelative(
        relativeTab: FlexMediaDetailTab,
        id: String,
        extras: Map<String, String>
    ): Result<List<FlexMediaSection>>
}