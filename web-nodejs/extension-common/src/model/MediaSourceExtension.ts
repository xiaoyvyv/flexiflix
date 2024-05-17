import {MediaSourceInfo} from "./MediaSourceInfo";
import {Source} from "../interface/Source";

/**
 * 构建一个插件的对象结构
 *
 * @author why
 * @since 5/8/24
 */
export class MediaSourceExtension {
    /**
     * 插件数据源ID，需要全局唯一，建议搞得抽象一点
     *
     * 需要和 source.id 保持一致，仅支持数字、字母、下划线、短横线组合。
     */
    id: string;

    /**
     * 插件描述信息
     */
    info: MediaSourceInfo;

    /**
     * 插件实现的源
     */
    source: Source;

    constructor(id: string, info: MediaSourceInfo, source: Source) {
        this.id = id;
        this.info = info;
        this.source = source
    }
}
