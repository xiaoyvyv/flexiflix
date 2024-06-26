import {
    FlexMediaDetail,
    FlexMediaDetailTab,
    FlexMediaPlaylistUrl,
    FlexMediaSection,
    FlexMediaSectionItem,
    FlexMediaUser,
    FlexSearchOption,
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
        return await api.fetchMediaDetail(id, extras);
    }

    async fetchMediaDetailRelative(relativeTab: FlexMediaDetailTab, id: string, extras: Map<string, string>): Promise<FlexMediaSection[]> {
        throw new Error("Not yet implemented");
    }

    async fetchMediaRawUrl(playlistUrl: FlexMediaPlaylistUrl): Promise<FlexMediaPlaylistUrl> {
        return await api.fetchMediaRawUrl(playlistUrl);
    }

    async fetchSectionMediaPages(sectionId: string, sectionExtras: Map<string, string>, page: number): Promise<FlexMediaSectionItem[]> {
        throw new Error("Not yet implemented");
    }

    async fetchUserMediaPages(user: FlexMediaUser): Promise<FlexMediaSectionItem[]> {
        throw new Error("Not yet implemented");
    }

    async fetchMediaSearchConfig(): Promise<FlexSearchOption> {
        return {keywordKey: "keyword", options: undefined}
    }

    async fetchMediaSearchResult(keyword: string, page: number, searchMap: Map<string, string>): Promise<FlexMediaSectionItem[]> {
        return await api.fetchMediaSearchResult(keyword, page, searchMap);
    }
}
