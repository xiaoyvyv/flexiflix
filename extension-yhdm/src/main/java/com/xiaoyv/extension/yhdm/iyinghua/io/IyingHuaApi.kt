@file:Suppress("unused", "SpellCheckingInspection")

package com.xiaoyv.extension.yhdm.iyinghua.io

import org.jsoup.nodes.Document
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface IyingHuaApi {

    @GET("/")
    suspend fun queryHomeHtml(): Document


    /**
     * https://www.iyinghua.io/show/21267.html
     */
    @GET("/show/{mediaId}.html")
    suspend fun qeuryDetail(@Path("mediaId") mediaId: String): Document

    /**
     * https://www.iyinghua.io/v/21267-0.html
     */
    @GET("/v/{epId}.html")
    suspend fun qeuryDetailPlayer(@Path("epId") epId: String): Document
}