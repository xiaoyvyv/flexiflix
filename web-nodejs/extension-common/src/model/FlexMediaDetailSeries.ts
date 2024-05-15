import {FlexMediaSectionItem} from "./FlexMediaSectionItem";

/**
 * [FlexMediaDetailSeries]
 *
 * @author why
 * @since 5/8/24
 */
export interface FlexMediaDetailSeries {
    title: string
    mediaId: string
    count: number
    items: FlexMediaSectionItem[]
}
