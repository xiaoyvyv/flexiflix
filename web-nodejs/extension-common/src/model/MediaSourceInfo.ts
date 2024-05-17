/**
 * MediaSourceInfo
 *
 * @author why
 * @since 5/8/24
 */
export interface MediaSourceInfo {
    /**
     * 插件数据源ID，需要全局唯一，建议搞得抽象一点
     *
     * 仅支持数字、字母、下划线、短横线组合。
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
     * 图标链接
     *
     * - 支持网络图片
     * - 支持 base64 编码的图片数据，固定格式为 http://localhost/api/image?data=[ImageBase64Data]，ImageBase64Data 为完整的图片 Base64 编码
     *
     * 示例链接：http://localhost/api/image?data=data:image/jpeg;base64,FASLKFKAMCZLFAKFQALCAS;FALFQPF==ASF=...
     */
    icon: string

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
