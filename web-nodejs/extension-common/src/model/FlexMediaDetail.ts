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
    type: string
    playCount: string
    createAt: string
    duration: number
    size: string
    publisher: FlexMediaUser
    playlist: FlexMediaPlaylist[]
    series: FlexMediaDetailSeries[]
    tags: FlexMediaTag[]
    relativeTabs: FlexMediaDetailTab[]
    extras: Map<string, string>
}