import {load} from "cheerio";
import utils from "../utils/utils";
import {FlexMediaSection} from "@xiaoyvyv/flexiflex-extension-common";


export const fetchHomeSections = async (): Promise<FlexMediaSection[]> => {
    const res = await fetch('https://www.iyhdmm.com').then((res) => res.text());

    const $ = load(res || '');
    const sections: FlexMediaSection[] = [];

    $(".firs.l > .dtit").get().forEach(item => {
        const section: FlexMediaSection = {
            id: decodeURI($(item).find("a").attr('href') || ''),
            items: [],
            title: $(item).find("h2").text().trim(),
            extras: utils.emptyExtras(),
        }

        sections.push(section);
    });
    return sections;
}
