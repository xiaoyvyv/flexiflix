import {FlexMediaSectionItem} from "./FlexMediaSectionItem";

/**
 * 通用的包含多媒体条目的板块，带一个标题或一些媒体条目信息
 *
 * @author why
 * @since 5/8/24
 */
export interface FlexMediaSection {
    /**
     * 板块标题
     */
    id: string

    /**
     * 板块名称
     */
    title: string

    /**
     * 改板块包含的条目数据
     */
    items: FlexMediaSectionItem[]

    /**
     * 扩展信息
     */
    extras: Map<string, string> | undefined
}





