import {FlexMediaUser} from "./FlexMediaUser";
import {FlexMediaPlaylist} from "./FlexMediaPlaylist";
import {FlexMediaDetailSeries} from "./FlexMediaDetailSeries";
import {FlexMediaTag} from "./FlexMediaTag";
import {FlexMediaDetailTab} from "./FlexMediaDetailTab";

/**
 * [FlexMediaDetail]
 *
 * @author why
 * @since 5/8/24
 */
export interface FlexMediaDetail {
    id: string
    title: string
    description: string
    cover: string
    url: string
    type: string | undefined
    playCount: string | undefined
    createAt: string | undefined
    duration: number | undefined
    size: string | undefined
    publisher: FlexMediaUser | undefined
    playlist: FlexMediaPlaylist[] | undefined
    series: FlexMediaDetailSeries[] | undefined
    tags: FlexMediaTag[] | undefined
    relativeTabs: FlexMediaDetailTab[] | undefined
    extras: Map<string, string> | undefined
}