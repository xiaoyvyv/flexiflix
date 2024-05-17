import {FlexMediaSectionItem} from "./FlexMediaSectionItem";

/**
 * 条目的详情页的相关系列的板块
 *
 * @author why
 * @since 5/8/24
 */
export interface FlexMediaDetailSeries {
    /**
     * 标题
     */
    title: string

    /**
     * 当前条目的ID
     */
    mediaId: string

    /**
     * 数目
     */
    count: number

    /**
     * 系列板块包含的小条目
     */
    items: FlexMediaSectionItem[]
}
