import path from "path";
import fs from "fs";
import express from 'express';
import {FlexMediaPlaylistUrl, MediaSourceExtension} from "@xiaoyvyv/flexiflex-extension-common";
import crypto from "crypto";

const modules: Map<string, MediaSourceExtension> = new Map<string, MediaSourceExtension>();

const init = () => {
    // 解析启动参数，保存到 params
    // 格式 node index.js -d=[Extension Dir]
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
    if (extensionDir == null) {
        console.error("必须使用 -d 指定扩展目录");
        process.exit(1);
    }

    // 读取安装的模块数据
    const moduleList = fs.readdirSync(extensionDir).map(item => extensionDir + "/" + item + "/index.js");
    modules.clear();
    moduleList.forEach((item) => {
        if (fs.existsSync(item)) {
            try {
                // 计算文件 MD5
                const hash = crypto.createHash('md5');
                const data = fs.readFileSync(item);
                hash.update(data);
                const md5 = hash.digest('hex').toLowerCase();

                // 加载模块
                const module = require(item) as MediaSourceExtension;
                const extensionId = module.id || '';

                // 储存映射
                modules.set(md5, module);

                console.log(`载入插件 ${extensionId}, Hash: ${md5}`);
            } catch (e) {
                console.error(`载入插件失败：${item}`);
            }
        }
    });

    console.log(`扩展包加载完成：${modules.size} 个`);
};

/**
 * 根据模块名称获取对应的扩展模块
 *
 * @param hash
 */
const getMediaExtension = (hash: string | undefined): MediaSourceExtension => {
    const module = modules.get(hash || '');
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

    // 公共配置
    app.use((req, res, next) => {
        res.set('Connection', 'keep-alive');
        next();
    });

    app.get('/api/running', (req, res) => {
        res.status(200).send({code: 0});
    });

    // 获取插件信息
    app.get('/api/info/:hash', (req, res) => {
        try {
            const extension = getMediaExtension(req.params.hash);
            res.json(extension.info);
        } catch (e) {
            console.log(e);
            res.status(400).json({error: e?.toString(), code: 400});
        }
    });

    // 首页数据拉取
    app.get('/api/home/:hash', async (req, res) => {
        try {
            const extension = getMediaExtension(req.params.hash);
            res.json(await extension.source.fetchHomeSections());
        } catch (e) {
            res.status(400);
            res.json({error: e?.toString(), code: 400});
        }
    });

    // 媒体详情页数据
    app.get('/api/media/detail/:hash', async (req, res) => {
        try {
            const extension = getMediaExtension(req.params.hash);
            const id = req.query.id?.toString() || '';
            const extras = req.query;
            delete extras.id;
            const extrasMap = new Map(Object.entries(extras).map(([key, value]) => [key, String(value)]));

            res.json(await extension.source.fetchMediaDetail(id, extrasMap));
        } catch (e) {
            res.status(400);
            res.json({error: e?.toString(), code: 400});
        }
    });

    // 媒体播放列表的条目的真实播放媒体源链接
    app.post('/api/media/url/:hash', async (req, res) => {
        try {
            const extension = getMediaExtension(req.params.hash);
            const url = req.body as FlexMediaPlaylistUrl;
            res.json(await extension.source.fetchMediaRawUrl(url));
        } catch (e) {
            res.status(400);
            res.json({error: e?.toString(), code: 400});
        }
    });

    // 搜索的配置项
    app.get('/api/search/config/:hash', async (req, res) => {
        try {
            const extension = getMediaExtension(req.params.hash);
            res.json(await extension.source.fetchMediaSearchConfig());
        } catch (e) {
            res.status(400);
            res.json({error: e?.toString(), code: 400});
        }
    });

    // 搜索媒体
    app.get('/api/search/media/:hash', async (req, res) => {
        try {
            const extension = getMediaExtension(req.params.hash);
            const extrasMap = new Map(Object.entries(req.query).map(([key, value]) => [key, String(value)]));
            const keyword = extrasMap.get('keyword') || '';
            const page: number = parseInt(extrasMap.get('page') || '1');
            res.json(await extension.source.fetchMediaSearchResult(keyword, page, extrasMap));
        } catch (e) {
            res.status(400);
            res.json({error: e?.toString(), code: 400});
        }
    });


    // 启动服务器
    const server = app.listen(port, () => {
        console.log(`NodeJs 扩展服务已启动 http://localhost:${port}`);
    });

    // 增加超时时间
    server.keepAliveTimeout = 120 * 1000;
    server.headersTimeout = 120 * 1000;
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

