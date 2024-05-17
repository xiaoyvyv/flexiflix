import {MediaSourceExtension, MediaSourceInfo} from "@xiaoyvyv/flexiflex-extension-common";
import {AcfunSource} from "./source/source";
import utils from "./utils/utils";

/**
 * 自定义一个插件 ID，必须全局唯一
 *
 * 注意：不能包含空格和特殊符号，仅支持数字、字幕、短横线、下划线。
 *
 * 建议格式：语言类型-数据源的地址，用短横线连接
 */
const extensionId = "js-acfun-cn"

/**
 * 插件信息
 */
const extensionInfo: MediaSourceInfo = {
    id: extensionId,
    name: "Acfun.CN - JS 数据源扩展",
    description: "Acfun.CN - JS 数据源扩展",
    author: "xiaoyvyv",
    nsfw: false,
    versionCode: 1,
    versionName: "1.0.0",
    icon: 'https://imgs.aixifan.com/newUpload/51737407_5c0982b66474402dae1b91ee1d31d7b6.jpeg'
};

const main = new MediaSourceExtension(extensionId, extensionInfo, new AcfunSource());

// 必须导出 MediaSourceExtension;
// 注意，这里导出必须直接 module.exports = xxx; xxx 为 MediaSourceExtension 结构的对象;
module.exports = main;

// 检查模块是否被直接运行，可以在开发时直接执行测试该脚本。
// 打包发布时，请确保顶级只有一些初始化模块的逻辑，没有直接执行耗时操作。
if (require.main === module) {
    console.log("Dev: start main");
    const port = process.env.PORT || 3000;
    const environment = process.env.NODE_ENV || 'development';


    main.source
        .fetchMediaDetail("aa5023295", utils.emptyExtras())
        .then((res) => {
            console.log(JSON.stringify(res, null, 4));
        });
}
