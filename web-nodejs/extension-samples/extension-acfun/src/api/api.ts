import {load} from "cheerio";
import utils from "../utils/utils";
import {
    FlexMediaSection,
    FlexMediaSectionItem,
    FlexMediaUser,
    ImageLayout,
    OverlayText
} from "@xiaoyvyv/flexiflex-extension-common";

const requestInit: RequestInit = {
    headers: {
        'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:125.0) Gecko/20100101 Firefox/125.0',
        'Referer': 'https://www.acfun.cn'
    }
};

export const fetchHomeSections = async (): Promise<FlexMediaSection[]> => {
    const html = await fetch('https://www.acfun.cn/v/list155/index.htm', requestInit)
        .then((res) => res.text() || "");

    const $ = load(html);
    const sections: FlexMediaSection[] = [];

    $(".channel-slider").toArray().forEach(element => {
        const sliderWrapItems = $(element)
            .find(".slider-wrap ul.slider-con > li")
            .map((_, li) => {
                const item: FlexMediaSectionItem = {
                    id: utils.subLast($(li).find("a").attr("href"), '/'),
                    title: $(li).find("span").text().trim(),
                    cover: $(li).find("img").attr("src") || '',
                    description: $(li).find("span").text().trim(),
                    extras: utils.emptyExtras(),
                    layout: undefined, overlay: undefined, user: undefined,
                };
                return item;
            })
            .toArray();
        const sliderWrap: FlexMediaSection = {
            id: '',
            title: '热门番剧',
            items: sliderWrapItems,
            extras: utils.emptyExtras(),
        };

        const sliderRightItems = $(element)
            .find(".slider-right ul > li")
            .map((_, li) => {
                const item: FlexMediaSectionItem = {
                    id: utils.subLast($(li).find("a").attr("href"), '/'),
                    title: $(li).find("img").attr("alt") || '',
                    cover: $(li).find("img").attr("src") || '',
                    description: $(li).find("img").attr("alt") || '',
                    overlay: <OverlayText>{
                        bottomEnd: $(li).find("i.icon-view-player")
                            .text()
                            .replace("", "")
                            .trim(),
                        topStart: $(li).find("i.icon-danmu")
                            .text()
                            .replace("", "")
                            .trim(),
                    },
                    extras: undefined,
                    user: undefined,
                    layout: undefined
                };
                return item;
            })
            .toArray();

        const sliderRight: FlexMediaSection = {
            id: '',
            title: '热门番剧',
            items: sliderRightItems,
            extras: utils.emptyExtras(),
        };

        sections.push(sliderWrap);
        sections.push(sliderRight);
    })


    return sections;
}
