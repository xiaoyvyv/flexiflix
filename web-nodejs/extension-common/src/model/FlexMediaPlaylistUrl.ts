/**
 * [FlexMediaPlaylistUrl]
 *
 * @author why
 * @since 5/8/24
 */
export interface FlexMediaPlaylistUrl {
    /**
     * 媒体源 ID
     */
    id: string

    /**
     * 标题
     */
    title: string

    /**
     * 媒体真实链接源，可以有多个不同的分辨率
     */
    mediaUrls: FlexMediaPlaylistUrlRaw[] | undefined

    /**
     * 封面
     */
    cover: string | undefined
}

/**
 * 真实的媒体地址
 */
export interface FlexMediaPlaylistUrlRaw {
    name: string
    rawUrl: string
    type: string | undefined
    size: string | undefined
}