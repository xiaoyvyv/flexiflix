import path from "path";
import fs from "fs";
import express from 'express';
import {MediaSourceExtension} from "@xiaoyvyv/flexiflex-extension-common";

const modules: Map<string, MediaSourceExtension> = new Map<string, MediaSourceExtension>();
const moduleNames: string[] = [];

const init = () => {
    // 解析启动参数，保存到 params
    // 格式 node index.js -d=[Extension Dir] -i=[Active config.json]
    const params: Map<string, string> = new Map<string, string>();
    const args = process.argv.slice(2);
    for (let i = 0; i < args.length; i++) {
        const item = args[i];
        if (item.startsWith("-") && item.indexOf("=") !== -1) {
            const split = item.split("=");
            let argPath = split[1];
            if (argPath.startsWith(".")) {
                argPath = path.resolve(__dirname, argPath);
            }
            const name = split[0].replace("-", "");
            params.set(name, argPath);
        }
    }

    // 校验参数
    const extensionDir = params.get('d');
    const activeConfigPath = params.get('i');
    if (extensionDir == null || activeConfigPath == null) {
        console.error("必须使用 -d 指定扩展目录和 -i 指的安装的插件");
        process.exit(1);
    }

    // 解析安装的模块数据
    const moduleList: string[] = JSON.parse(fs.readFileSync(activeConfigPath).toString());
    modules.clear();
    moduleNames.length = 0;
    moduleList.forEach((item) => {
        modules.set(item, require(extensionDir + "/" + item));
        moduleNames.push(item);
    });

    console.log(`扩展包加载完成`, moduleNames);
};

/**
 * 根据模块名称获取对应的扩展模块
 *
 * @param extension
 */
const getMediaExtension = (extension: string | undefined): MediaSourceExtension => {
    const module = modules.get(extension || '');
    console.log(module)
    if (module === undefined) throw new Error("插件不存在！");
    if (module.source === undefined) throw new Error("插件数据源不存在！");
    return module;
}


/**
 * 开启服务
 *
 * @param port 端口
 */
const startServer = (port = 3000) => {
    const app = express();

    // 中间件，解析 JSON 请求体
    app.use(express.json());

    // 首页数据拉取
    app.get('/api/:extension/home', async (req, res) => {
        try {
            const extension = getMediaExtension(req.params.extension);
            res.json(await extension.source.fetchHomeSections());
        } catch (e) {
            res.status(400);
            res.json({error: e?.toString(), code: 400});
        }
    });

    // 示例 API 端点，模拟一个需要时间处理的请求
    app.get('/api/slow', async (req, res) => {
        await delay(3000);
        res.json({error: 'This is a slow response', code: 400});
    });

    // 启动服务器
    app.listen(port, () => {
        console.log(`NodeJs 扩展服务已启动 http://localhost:${port}`);
    });
};

/**
 * 延时
 *
 * @param mills
 */
function delay(mills: number) {
    return new Promise((resolve) => {
        setTimeout(resolve, mills);
    });
}

init();
startServer();

export default {
    init: init,
    startServer: startServer
}

