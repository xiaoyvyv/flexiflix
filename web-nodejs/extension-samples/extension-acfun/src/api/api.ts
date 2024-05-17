// noinspection SpellCheckingInspection

import {CheerioAPI, load} from "cheerio";
import {
    FlexMediaDetail, FlexMediaDetailSeries,
    FlexMediaPlaylist,
    FlexMediaPlaylistUrl, FlexMediaPlaylistUrlRaw,
    FlexMediaSection,
    FlexMediaSectionItem,
    OverlayText
} from "@xiaoyvyv/flexiflex-extension-common";
import utils from "../utils/utils";
import {it} from "node:test";


const requestInit: RequestInit = {
    headers: {
        'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:125.0) Gecko/20100101 Firefox/125.0',
        'Referer': 'https://www.acfun.cn'
    }
};

/**
 * 根据 bangumiData 获取媒体播放真实数据 FlexMediaPlaylistUrlRaw[]
 *
 * @param bangumiData
 */
const bangumiDataMediaUrls = (bangumiData: any) => {
    const currentVideoInfo = bangumiData['currentVideoInfo'] || {};
    let currentVideoUrls: FlexMediaPlaylistUrlRaw[] = [];
    try {
        const ksPlayJson = JSON.parse(currentVideoInfo['ksPlayJsonHevc'] || currentVideoInfo['ksPlayJson']);
        const adaptationSet = ksPlayJson['adaptationSet'] ?.[0] || {};
        const representation: [] = adaptationSet['representation'] || [];

        currentVideoUrls = representation.map(item => {
            const data: FlexMediaPlaylistUrlRaw = {
                name: item['qualityLabel'] || '',
                rawUrl: item['url'] || item['backupUrl']?.[0] || '',
                type: item['qualityType'] || '',
                size: '',
            };
            return data;
        });
    } catch (e) {
        console.log(e)
    }

    return currentVideoUrls;
};

/**
 * 拉取首页数据
 */
export async function fetchHomeSections(): Promise<FlexMediaSection[]> {
    const html = await fetch('https://www.acfun.cn/v/list155/index.htm', requestInit)
        .then((res) => res.text() || "");

    const $: CheerioAPI = load(html);

    // 解析数据集合
    const script = $("script")
        .filter((_, el) => $(el).text().indexOf("__INITIAL_STATE__") > 0)
        .map((_, el) => $(el).text())
        .toArray()[0] || '';
    const data = (script.match(/__INITIAL_STATE__[\s\S]+?};/) || [])[0] || '';
    const js = 'const ' + data + ';__INITIAL_STATE__';
    const entity = eval(js);

    const blockList: Array<any> = entity['channel']?.['blockList'] || [];

    const sections: FlexMediaSection[] = [];

    function contentToItems(content: any) {
        const webContents: [] = content["webContents"] || [];
        return webContents.map(sub => {
            const overlayText: OverlayText = {
                bottomEnd: '播放：' + sub['formatViewCount'],
                topStart: '🔥' + sub['formatCommentCount'],
                bottomStart: undefined,
                topEnd: undefined,
            };
            const sectionItem: FlexMediaSectionItem = {
                id: utils.subLast(sub['link'], '/'),
                title: sub['title'] || '',
                description: sub['title'] || '',
                cover: sub['image'] || '',
                extras: utils.emptyExtras(),
                overlay: overlayText,
                layout: undefined,
                user: undefined
            }
            return sectionItem;
        });
    }

    // 顶部热门 Banner
    const banner: [] = blockList[0]?.['content'] || [];
    const banners = banner.map(content => {
        const section: FlexMediaSection = {
            extras: utils.emptyExtras(),
            id: '',
            title: '热门番剧',
            items: contentToItems(content),
        }
        return section;
    });

    // 下方栏目
    const items = blockList
        .filter(item => item['blockType'] === 16)
        .map(item => {
            const content = (item['content'] || [])[0] || {};

            const section: FlexMediaSection = {
                extras: utils.emptyExtras(),
                id: item['id'] || '',
                title: item['name'],
                items: contentToItems(content),
            }
            return section;
        });

    sections.push(...banners);
    sections.push(...items);

    return sections;
}

/**
 * 拉取媒体详情数据
 *
 * @param id
 * @param extras
 */
export async function fetchMediaDetail(id: string, extras: Map<string, string>): Promise<FlexMediaDetail> {
    const html = await fetch(`https://www.acfun.cn/bangumi/${id}`, requestInit)
        .then((res) => res.text() || "");

    const $ = load(html);
    const scripts = $("script")
        .filter((_, el) => $(el).text().indexOf("window.bangumiData") > 0)
        .toArray()[0];
    const script = $(scripts).text();

    const init = "var window = { addEventListener: (a,b) => {}};"
    const out = ";window;"
    const obj = eval((init + script + out));
    const bangumiData = obj['bangumiData'] || {};
    const bangumiList = obj['bangumiList'] || {};

    // 解析媒体数据
    const currentVideoId = bangumiData['videoId'] || '';
    const currentVideoInfo = bangumiData['currentVideoInfo'] || {};
    const duration = currentVideoInfo['durationMillis'] || 0;
    const currentVideoUrls = bangumiDataMediaUrls(bangumiData);

    const playlists: FlexMediaPlaylist[] = [];
    const playlist: FlexMediaPlaylist = {
        title: '播放列表',
        items: ((bangumiList['items'] || []) as []).map(subItem => {
            const bangumiId = subItem['bangumiId'] || '';
            const itemId = subItem['itemId'] || '';
            const videoId = subItem['videoId'] || '';
            const title = subItem['title'] || '';
            const episodeName = subItem['episodeName'] || '';
            const url: FlexMediaPlaylistUrl = {
                id: 'aa' + bangumiId + '_36188_' + itemId,
                title: title === '' ? episodeName : episodeName + '-' + title,
                cover: subItem['imgInfo']?.['thumbnailImageCdnUrl'] || '',
                mediaUrls: videoId === currentVideoId ? currentVideoUrls : undefined,
            }
            return url;
        }),
    };

    playlists.push(playlist);

    const relatedBangumi: [] = bangumiData['relatedBangumis'] || [];
    const related: FlexMediaDetailSeries = {
        count: relatedBangumi.length,
        mediaId: id,
        title: "相关系列",
        items: relatedBangumi.map(item => {
            const data: FlexMediaSectionItem = {
                id: 'aa' + item['id'],
                title: item['name'] || '',
                cover: bangumiData['bangumiCoverImageH'] || '',
                description: undefined,
                user: undefined,
                extras: undefined,
                overlay: undefined,
                layout: undefined
            };
            return data;
        }),
    };

    const recommendBangumi: [] = bangumiData['recommendBangumis'] || [];
    const recommend: FlexMediaDetailSeries = {
        count: recommendBangumi.length,
        mediaId: id,
        title: "番剧推荐",
        items: recommendBangumi.map(item => {
            const data: FlexMediaSectionItem = {
                id: 'aa' + item['id'],
                title: item['title'] || '',
                cover: item['coverImageH'] || '',
                description: undefined,
                user: undefined,
                extras: undefined,
                overlay: {
                    topStart: undefined,
                    topEnd: undefined,
                    bottomStart: item['lastUpdateItemName'],
                    bottomEnd: item['stowCountShow']
                },
                layout: undefined
            };
            return data;
        }),
    };

    // 系列和推荐
    const series: FlexMediaDetailSeries[] = [];
    if (related.items.length > 0) series.push(related);
    if (recommend.items.length > 0) series.push(recommend);

    return {
        id: id,
        title: bangumiData['showTitle'] || '',
        description: bangumiData['bangumiIntro'] || '',
        cover: bangumiData['bangumiCoverImageH'] || '',
        url: bangumiData['shareUrl'] || '',
        type: bangumiData['episodeName'] || '',
        playCount: bangumiData['playCountShow'] || '',
        createAt: bangumiData['updateTime'] || '',
        duration: duration,
        playlist: playlists,
        size: undefined,
        publisher: undefined,
        series: series,
        tags: undefined,
        relativeTabs: undefined,
        extras: utils.emptyExtras()
    };
}

/**
 * 根据媒体播放的 FlexMediaPlaylistUrl 获取真实的播放地址，只有 FlexMediaPlaylistUrl.mediaUrl 为空时才会调用查询
 *
 * @param playlistUrl
 */
export async function fetchMediaRawUrl(playlistUrl: FlexMediaPlaylistUrl): Promise<FlexMediaPlaylistUrl> {
    // aa6002917_36188_1740687
    const id = playlistUrl.id;
    // aa6006226_36188_1971845
    // https://www.acfun.cn/bangumi/aa6006226_36188_1971845?quickViewId=videoInfo_new&ajaxpipe=1&t=a
    const bangumiData = await fetch(`https://www.acfun.cn/bangumi/${id}?quickViewId=videoInfo_new&ajaxpipe=1&t=${Date.now()}`)
        .then(res => res.text())
        .then(html => html.replace("/*<!-- fetch-stream -->*/", ""))
        .then(json => {
            const html = JSON.parse(json)['html'] || '';
            const $ = load(html);
            const init = "var window = {};"
            const js = init + $(".videoInfo").text();
            const out = ";window.bangumiData;"
            return eval((init + js + out));
        });

    if (bangumiData['bangumiPaymentType']?.['value'] === 2) {
        throw Error("该条目源非免费资源！")
    }

    playlistUrl.mediaUrls = bangumiDataMediaUrls(bangumiData);

    return playlistUrl;
}



