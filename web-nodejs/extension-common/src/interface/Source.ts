import {FlexMediaSection} from "../model/FlexMediaSection";
import {FlexMediaSectionItem} from "../model/FlexMediaSectionItem";
import {FlexMediaUser} from "../model/FlexMediaUser";
import {FlexMediaDetail} from "../model/FlexMediaDetail";
import {FlexMediaPlaylistUrl} from "../model/FlexMediaPlaylistUrl";
import {FlexMediaDetailTab} from "../model/FlexMediaDetailTab";
import {FlexSearchOption} from "../model/FlexSearchOption";

/**
 * [Source]
 *
 * @author why
 * @since 5/8/24
 */
export interface Source {

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
    fetchHomeSections: () => Promise<FlexMediaSection[]>

    /**
     * 获取对应的某个 section 的全部分页数据
     *
     * @param sectionId 对应 [FlexMediaSection.id]
     * @param sectionExtras 对应 [FlexMediaSection.extras]
     * @param page 页码，从 1 开始
     */
    fetchSectionMediaPages: (
        sectionId: string,
        sectionExtras: Map<string, string>,
        page: number
    ) => Promise<FlexMediaSectionItem[]>

    /**
     * 获取某个用户全部分页数据
     */
    fetchUserMediaPages: (user: FlexMediaUser) => Promise<FlexMediaSectionItem[]>

    /**
     * 获取媒体条目详情
     *
     * @param id 媒体的ID
     * @param extras 通过section点击进入详情页时，传入的 [FlexMediaSectionItem.extras]
     */
    fetchMediaDetail: (id: string, extras: Map<string, string>) => Promise<FlexMediaDetail>

    /**
     * 此方法应用于，点击视频下方其它章节时，根据其章节ID查询真实的播放链接（如果媒体详情的播放列表已经包含视频链接，则不会调用此方法查询）
     *
     * 根据 [FlexMediaPlaylistUrl.id] 获取视频直链，只有 [FlexMediaPlaylistUrl.mediaUrl] 是空的情况才会查询
     */
    fetchMediaRawUrl: (playlistUrl: FlexMediaPlaylistUrl) => Promise<FlexMediaPlaylistUrl>
    /**
     * 获取媒体条目详情，下方对应的推荐相关栏位板块
     *
     * @param relativeTab 详情页对应的TAB下的拉取
     * @param id 媒体的ID
     * @param extras 通过section点击进入详情页时，传入的 [FlexMediaSectionItem.extras]
     */
    fetchMediaDetailRelative: (
        relativeTab: FlexMediaDetailTab,
        id: string,
        extras: Map<string, string>
    ) => Promise<FlexMediaSection[]>

    /**
     * 获取搜索的配置项数据，比如关键词的 key 和可选项等
     */
    fetchMediaSearchConfig: () => Promise<FlexSearchOption>

    /**
     * 搜索媒体数据
     *
     * @param searchMap 搜索的参数，<key-value> 结构，多个 value 用`,`区分
     */
    fetchMediaSearchResult: (
        keyword: string,
        page: number,
        searchMap: Map<string, string>,
    ) => Promise<FlexMediaSectionItem[]>
}