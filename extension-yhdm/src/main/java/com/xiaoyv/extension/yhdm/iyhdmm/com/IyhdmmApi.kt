@file:Suppress("unused", "SpellCheckingInspection")

package com.xiaoyv.extension.yhdm.iyhdmm.com

import org.jsoup.nodes.Document
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface IyhdmmApi {

    @GET("/")
    suspend fun queryHomeHtml(): Document

    /**
     * https://www.iyhdmm.com/showp/21267.html
     */
    @GET("/showp/{mediaId}.html")
    suspend fun qeuryDetail(@Path("mediaId") mediaId: String): Document

    /**
     * https://www.iyhdmm.com/vp/21267-2-0.html
     */
    @GET("/vp/{mediaAndSourceId}.html")
    suspend fun qeuryDetailPlayer(
        @Path("mediaAndSourceId") mediaAndSourceId: String
    ): Document


    @GET("/playurl")
    suspend fun queryM3u8(
        @Header("Referer") referer: String,
        @Query("aid") aid: String,
        @Query("playindex") playindex: String,
        @Query("epindex") epindex: String,
        @Query("r") r: String,
    ): String
}