import {FlexMediaUser} from "./FlexMediaUser";
import {FlexMediaPlaylist} from "./FlexMediaPlaylist";
import {FlexMediaDetailSeries} from "./FlexMediaDetailSeries";
import {FlexMediaTag} from "./FlexMediaTag";
import {FlexMediaDetailTab} from "./FlexMediaDetailTab";

/**
 * 条目详细信息
 *
 * @author why
 * @since 5/8/24
 */
export interface FlexMediaDetail {
    /**
     * 条目 ID
     */
    id: string

    /**
     * 条目标题
     */
    title: string

    /**
     * 描述
     */
    description: string

    /**
     * 封面
     */
    cover: string

    /**
     * 分享链接
     */
    url: string

    /**
     * 类型描述
     */
    type: string | undefined

    /**
     * 播放次数
     */
    playCount: string | undefined

    /**
     * 时间
     */
    createAt: string | undefined

    /**
     * 时长
     */
    duration: number | undefined

    /**
     * 大小
     */
    size: string | undefined

    /**
     * 发布者
     */
    publisher: FlexMediaUser | undefined

    /**
     * 包含的播放列表
     */
    playlist: FlexMediaPlaylist[] | undefined

    /**
     * 该条目的系列数据
     */
    series: FlexMediaDetailSeries[] | undefined

    /**
     * 条目的标签
     */
    tags: FlexMediaTag[] | undefined

    /**
     * 条目下方的 TAB UI配置
     */
    relativeTabs: FlexMediaDetailTab[] | undefined

    /**
     * 条目的扩展信息
     */
    extras: Map<string, string> | undefined
}