import {FlexMediaUser} from "./FlexMediaUser";

/**
 * [FlexMediaSectionItem]
 *
 * @author why
 * @since 5/8/24
 */
export interface FlexMediaSectionItem {
    id: string
    title: string
    description: string
    cover: string
    user: FlexMediaUser
    extras: Map<string, string>
    overlay: OverlayText
    layout: ImageLayout
}

export interface ImageLayout {
    widthDp: number
    aspectRatio: number
}

export interface OverlayText {
    topStart: string
    topEnd: string
    bottomStart: string
    bottomEnd: string
}
