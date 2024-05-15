@file:Suppress("unused", "SpellCheckingInspection")

package com.xiaoyv.extension.yhdm.yhdmba.net

import org.jsoup.nodes.Document
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * [YhdmbaApi]
 *
 * @author why
 * @since 5/11/24
 */
interface YhdmbaApi {

    @GET("/")
    suspend fun queryHomeHtml(): Document

    /**
     * http://www.yhdmba.net/view/10867.html
     */
    @GET("/view/{mediaId}.html")
    suspend fun qeuryDetail(@Path("mediaId") mediaId: String): Document

    /**
     * http://www.yhdmba.net/player/10867-0-0.html
     */
    @GET("/player/{epId}.html")
    suspend fun qeuryDetailPlayer(@Path("epId") playlistId: String): Document
}