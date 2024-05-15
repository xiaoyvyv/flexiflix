/**
 * MediaSourceInfo
 *
 * @author why
 * @since 5/8/24
 */
export interface MediaSourceInfo {
    /**
     * 数据源唯一ID
     */
    id: string

    /**
     * 数据源名称
     */
    name: string

    /**
     * 数据源描述
     */
    description: string

    /**
     * 数据源作者
     */
    author: string

    /**
     * 成人内容
     */
    nsfw: boolean

    /**
     * 数据源版本代码
     */
    versionCode: number

    /**
     * 数据源版本名称
     */
    versionName: string
}
