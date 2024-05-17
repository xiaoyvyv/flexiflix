import {Cheerio, CheerioAPI, load} from "cheerio";
import {
    FlexMediaDetail, FlexMediaPlaylist, FlexMediaPlaylistUrl,
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

    // 解析媒体数据
    const bangumiData = scripts
        .filter((_, el) => $(el).text().indexOf("window.bangumiData") > 0)
        .map((_, el) => $(el).text())
        .toArray()[0]
        ?.match(/bangumiData[\s\S]+?};/)
        ?.[0] || '';

    const bangumiDataEntity = eval(('const ' + bangumiData + ';bangumiData'));
    const currentVideoInfo = bangumiDataEntity['currentVideoInfo'] || {};
    const currentVideoId = bangumiDataEntity['videoId'] || '';
    let currentVideoUrl = ""
    try {
        const ksPlayJson = JSON.parse(currentVideoInfo['ksPlayJson']);
        const ksPlayJsonUrl = ksPlayJson['adaptationSet']?.[0]?.['representation']?.[0]?.['url'];

        const ksPlayJsonHevc = JSON.parse(currentVideoInfo['ksPlayJsonHevc']);
        const ksPlayJsonHevcUrl = ksPlayJsonHevc['adaptationSet']?.[0]?.['representation']?.[0]?.['url'];

        currentVideoUrl = ksPlayJsonHevcUrl || ksPlayJsonUrl || '';
    } catch (e) {
        console.log(e)
    }

    // 解析播放列表
    const bangumiList = scripts
        .filter((_, el) => $(el).text().indexOf("window.bangumiList") > 0)
        .map((_, el) => $(el).text())
        .toArray()[0]
        ?.match(/bangumiList[\s\S]+?};/)
        ?.[0] || '';

    const bangumiListEntity = eval(('const ' + bangumiList + ';bangumiList'));

    const playlists: FlexMediaPlaylist[] = [];
    const playlist: FlexMediaPlaylist = {
        title: '播放列表',
        items: ((bangumiListEntity['items'] || []) as []).map(subItem => {
            const videoId = subItem['videoId'] || '';
            const url: FlexMediaPlaylistUrl = {
                id: videoId,
                title: subItem['episodeName'] + ' - ' + subItem['title'],
                cover: subItem['imgInfo']?.['thumbnailImageCdnUrl'] || '',
                mediaUrl: videoId === currentVideoId ? currentVideoUrl : '',
                size: undefined,
                type: undefined
            }
            return url;
        }),
    };

    playlists.push(playlist);


    return {
        id: id,
        title: bangumiDataEntity['showTitle'] || '',
        description: bangumiDataEntity['bangumiIntro'] || '',
        cover: bangumiDataEntity['bangumiCoverImageH'] || '',
        url: bangumiDataEntity['shareUrl'] || '',
        type: bangumiDataEntity['episodeName'] || '',
        playCount: bangumiDataEntity['playCountShow'] || '',
        createAt: bangumiDataEntity['updateTime'] || '',
        duration: currentVideoInfo['durationMillis'] || 0,
        playlist: playlists,
        size: undefined,
        publisher: undefined,
        series: undefined,
        tags: undefined,
        relativeTabs: undefined,
        extras: utils.emptyExtras()
    };
}


