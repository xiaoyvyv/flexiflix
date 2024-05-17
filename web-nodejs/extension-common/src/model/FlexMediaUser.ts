/**
 * 发布条目的用户
 *
 * @author why
 * @since 5/8/24
 */
export interface FlexMediaUser {
    /**
     * 用户ID
     */
    id: string

    /**
     * 用户名称
     */
    name: string

    /**
     * 用户头像
     */
    avatar: string

    /**
     * 角色
     */
    role: string | undefined
}
