import {FlexMediaSectionItem} from "./FlexMediaSectionItem";

/**
 * [FlexMediaSection]
 *
 * @author why
 * @since 5/8/24
 */
export interface FlexMediaSection {
    id: string
    title: string
    items: FlexMediaSectionItem[]
    extras: Map<string, string>
}





