import {FlexMediaUser} from "./FlexMediaUser";

/**
 * 板块下面的小条目卡片数据结构
 *
 * @author why
 * @since 5/8/24
 */
export interface FlexMediaSectionItem {
    /**
     * 条目ID
     */
    id: string

    /**
     * 标题
     */
    title: string

    /**
     * 条目卡片封面
     */
    cover: string

    /**
     * 条目描述
     */
    description: string | undefined

    /**
     * 条目用户
     */
    user: FlexMediaUser | undefined

    /**
     * 条目扩展信息
     */
    extras: Map<string, string> | undefined

    /**
     * 条目 UI 覆盖层的文字信息
     */
    overlay: OverlayText | undefined

    /**
     * 条目的布局尺寸
     */
    layout: ImageLayout | undefined
}

export interface ImageLayout {
    /**
     * 条目卡片的宽度，定位DP
     */
    widthDp: number

    /**
     * 条目卡片的宽高比，默认 16/9
     */
    aspectRatio: number
}

export interface OverlayText {
    /**
     * 条目卡片 UI 覆盖层，左上角文字
     */
    topStart: string | undefined

    /**
     * 条目卡片 UI 覆盖层，右上角文字
     */
    topEnd: string | undefined

    /**
     * 条目卡片 UI 覆盖层，左下角文字
     */
    bottomStart: string | undefined

    /**
     * 条目卡片 UI 覆盖层，右下角文字
     */
    bottomEnd: string | undefined
}
