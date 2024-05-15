import {MediaSourceInfo} from "./MediaSourceInfo";
import {Source} from "../interface/Source";

/**
 * [MediaSourceExtension]
 *
 * @author why
 * @since 5/8/24
 */
export class MediaSourceExtension {
    id: string;
    info: MediaSourceInfo;
    source: Source;

    constructor(id: string, info: MediaSourceInfo, source: Source) {
        this.id = id;
        this.info = info;
        this.source = source
    }
}
