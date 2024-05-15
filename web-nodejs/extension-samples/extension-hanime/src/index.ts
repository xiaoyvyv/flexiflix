/*
import {HanimeSource} from "./source/source";

const extensionId = "";

const info: MediaSourceInfo = {
    author: "xiaoyvyv",
    description: "",
    id: "hanime.com",
    name: "Hanime - JS 数据源扩展",
    nsfw: false,
    versionCode: 1,
    versionName: "1.0.0"
};


const main = new MediaSourceExtension(extensionId, info, new HanimeSource());

module.exports = main;

// 检查模块是否被直接运行，可以在开发时直接执行该脚本，打包发布时，请确保顶级没有直接执行耗时操作
if (require.main === module) {
    console.log("Dev: start main");

    main.source.fetchHomeSections().then((res) => {
        console.log(JSON.stringify(res, null, 4));
    });
}*/

// @ts-ignore
import {load} from "cheerio";

// setTimeout(() => {
//     console.log("延时");
// }, 3000);
// console.log("延时通过")
//
//
// const $ = load("<html><head><title>你好</title></head></html>");
// console.log(myGlobalVar);
import {MediaSourceExtension, MediaSourceInfo} from "@xiaoyvyv/flexiflex-extension-common";

const info: MediaSourceInfo = {
    author: "xiaoyvyv",
    description: "",
    id: "hanime.com",
    name: "Hanime - JS 数据源扩展",
    nsfw: false,
    versionCode: 1,
    versionName: "1.0.0"
};

export const extension = {
    info: info,
    fetchHomeSections: async () => {
        await request({
            url: getUrl(),
            data: {
                "id": "xxx"
            }
        }).then((res) => {
            console.log(JSON.stringify(res, null, 4))
        }).catch(error => {
            console.log("error -> " + error)
        });

        console.log("通过");
    }
}

