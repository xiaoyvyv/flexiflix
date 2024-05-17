import {FlexMediaPlaylistUrl} from "./FlexMediaPlaylistUrl";

/**
 * 播放列表
 *
 * @author why
 * @since 5/8/24
 */
export interface FlexMediaPlaylist {
    /**
     * 播放列表标题
     */
    title: string

    /**
     * 播放列表的播放媒体条目
     */
    items: FlexMediaPlaylistUrl[]
}
