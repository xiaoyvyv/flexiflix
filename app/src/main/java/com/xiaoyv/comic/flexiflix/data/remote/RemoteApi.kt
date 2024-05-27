@file:Suppress("SpellCheckingInspection")

package com.xiaoyv.comic.flexiflix.data.remote

import com.xiaoyv.comic.flexiflix.model.OnlineExtension
import retrofit2.http.GET

/**
 * [RemoteApi]
 *
 * @author why
 * @since 5/24/24
 */
interface RemoteApi {

    /**
     * 获取在线插件
     */
    @GET("https://raw.githubusercontent.com/xiaoyvyv/flexiflix-extension/main/index.json")
    suspend fun getOnlineExtensions(): List<OnlineExtension>
}