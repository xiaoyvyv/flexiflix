import {FlexKeyValue} from "./FlexKeyValue";

/**
 * 选项和其能勾选的值
 *
 * 比如过滤参数为 type=a|b|c|d，其UI需要显示过滤菜单为：类型：选项A|选项B|选项C|选项D
 *
 * ```javascript
 *     const item : FlexSearchOptionItem = {
 *         key : "type",
 *         keyLabel : "类型",
 *         values : [
 *             <FlexKeyValue>{key: "a", value: "选项A"},
 *             <FlexKeyValue>{key: "b", value: "选项B"},
 *             <FlexKeyValue>{key: "c", value: "选项C"},
 *             <FlexKeyValue>{key: "d", value: "选项D"}
 *         ],
 *         maxSelect : 1,
 *         mergeSymbol : ','
 *     }
 * ```
 */
export interface FlexSearchOptionItem {
    key?: string,
    keyLabel?: string
    values?: FlexKeyValue[]

    /**
     * 最多选几个值，-1 无限制
     */
    maxSelect: number,

    /**
     * 同一个 `key` 选了多个 `value` 时，用以合并拼接的字符，默认 `,`。
     *
     * 示例：`query=value1,value2,value3`
     *
     * 如果你的数据源，针对有多个值的情况，需要将其拆分为多个同名 `query=value1&query=value2&query=value3` 形式。
     *
     * 请在数据源实现层根据传入的 `mergeSymbol` 自行拆分。
     */
    mergeSymbol?: string,
}
