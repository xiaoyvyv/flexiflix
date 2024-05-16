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
    cover: string
    description: string | undefined
    user: FlexMediaUser | undefined
    extras: Map<string, string> | undefined
    overlay: OverlayText | undefined
    layout: ImageLayout | undefined
}

export interface ImageLayout {
    widthDp: number
    aspectRatio: number
}

export interface OverlayText {
    topStart: string | undefined
    topEnd: string | undefined
    bottomStart: string | undefined
    bottomEnd: string | undefined
}
