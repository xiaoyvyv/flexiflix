import {
    FlexMediaDetail,
    FlexMediaDetailTab,
    FlexMediaPlaylistUrl,
    FlexMediaSection, FlexMediaSectionItem, FlexMediaUser,
    Source
} from "@xiaoyvyv/flexiflex-extension-common";

import * as api from '../api/api';

/**
 * 实现数据源接口的逻辑
 */
export class AcfunSource implements Source {

    async fetchHomeSections(): Promise<FlexMediaSection[]> {
        return await api.fetchHomeSections();
    }

    async fetchMediaDetail(id: string, extras: Map<string, string>): Promise<FlexMediaDetail> {
        throw new Error("Not yet implemented");
    }

    async fetchMediaDetailRelative(relativeTab: FlexMediaDetailTab, id: string, extras: Map<string, string>): Promise<FlexMediaSection[]> {
        throw new Error("Not yet implemented");
    }

    async fetchMediaRawUrl(playlistUrl: FlexMediaPlaylistUrl): Promise<FlexMediaPlaylistUrl> {
        throw new Error("Not yet implemented");
    }

    async fetchSectionMediaPages(sectionId: string, sectionExtras: Map<string, string>, page: number): Promise<FlexMediaSectionItem[]> {
        throw new Error("Not yet implemented");
    }

    async fetchUserMediaPages(user: FlexMediaUser): Promise<FlexMediaSectionItem[]> {
        throw new Error("Not yet implemented");
    }
}
