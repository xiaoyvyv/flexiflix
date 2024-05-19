package com.xiaoyv.extension.hanime

import org.jsoup.nodes.Document
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * [HanimeApi]
 *
 * @author why
 * @since 5/8/24
 */
interface HanimeApi {

    @GET("/")
    suspend fun queryHomeHtml(): Document

    @GET("/search")
    suspend fun searchWithQueryMap(@QueryMap queryMap: Map<String, String?>): Document

    @GET("/watch")
    suspend fun queryMediaDetail(@Query("v") mediaId: String): Document

    @GET("/search")
    suspend fun searchMedia(
        @Query("page") page: Int,
        @Query("query") query: String? = null,
        @Query("genre") genre: String? = null,
        @Query("broad") broad: String? = null,
        @Query("tags[]") tags: List<String>? = null,
        @Query("sort") sort: String? = null,
        @Query("year") year: String? = null,
        @Query("month") month: String? = null,
    ): Document
}