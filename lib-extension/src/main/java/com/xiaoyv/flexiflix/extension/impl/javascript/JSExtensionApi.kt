package com.xiaoyv.flexiflix.extension.impl.javascript

import com.xiaoyv.flexiflix.extension.model.FlexMediaDetail
import com.xiaoyv.flexiflix.extension.model.FlexMediaDetailTab
import com.xiaoyv.flexiflix.extension.model.FlexMediaSection
import com.xiaoyv.flexiflix.extension.model.FlexMediaSectionItem
import com.xiaoyv.flexiflix.extension.model.FlexMediaUser
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * [JSExtensionApi]
 *
 * @author why
 * @since 5/15/24
 */
interface JSExtensionApi {
    @GET("/api/running")
    suspend fun isRunning(): Response<ResponseBody>

    @GET("/api/info/{md5}")
    suspend fun getExtensionInfo(@Path("md5") md5: String): Map<String, String>

    @GET("/api/home/{hash}")
    suspend fun fetchHomeSections(@Path("hash") hash: String): List<FlexMediaSection>

    @FormUrlEncoded
    @POST("/api/sections/{hash}")
    suspend fun fetchSectionMediaPages(
        @Path("hash") hash: String,
        @Field("id") sectionId: String,
        @Field("page") page: Int,
        @FieldMap sectionExtras: Map<String, String>
    ): List<FlexMediaSectionItem>

    @POST("/api/media/user/{hash}")
    suspend fun fetchUserMediaPages(
        @Path("hash") hash: String,
        @Body user: FlexMediaUser
    ): List<FlexMediaSectionItem>

    @FormUrlEncoded
    @POST("/api/media/detail/{hash}")
    suspend fun fetchMediaDetail(
        @Path("hash") hash: String,
        @Field("id") id: String,
        @FieldMap extras: Map<String, String>
    ): FlexMediaDetail

    @FormUrlEncoded
    @POST("/api/media/detail/relative/{hash}")
    suspend fun fetchMediaDetailRelative(
        @Path("hash") hash: String,
        @Field("id") id: String,
        @FieldMap extras: Map<String, String>,
        @Body relativeTab: FlexMediaDetailTab,
    ): List<FlexMediaSection>
}