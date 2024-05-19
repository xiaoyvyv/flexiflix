import {FlexSearchOptionItem} from "./FlexSearchOptionItem";

/**
 * 搜索的关键字的 key 和可选的过滤项目
 */
export interface FlexSearchOption {
    /**
     * 搜索关键字的 key
     */
    keywordKey: string | undefined;

    /**
     * 搜索选项
     */
    options: FlexSearchOptionItem[] | undefined;
}

