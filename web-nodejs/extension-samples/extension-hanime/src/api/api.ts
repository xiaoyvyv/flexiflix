global.__dirname = "/Users/why/AndroidStudioProjects/FlexiFlix/lib-extension/src/main/assets/nodejs-project/extension-samples/extension-hanime/dist";

import {load} from "cheerio";

import axios from "axios";
import utils from "../utils/utils";
import {FlexMediaSection} from "@xiaoyvyv/flexiflex-extension-common";


export const fetchHomeSections = async (): Promise<FlexMediaSection[]> => {
    const res = await axios.get('https://www.iyhdmm.com/');
    const $ = load(res.data || '');
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
