@file:Suppress("SpellCheckingInspection")

package com.xiaoyv.extension.yhdm.yhdmba.net

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class YhdmbaPlayInfo(
    @SerializedName("encrypt") var encrypt: Int = 0,
    @SerializedName("flag") var flag: String? = null,
    @SerializedName("from") var from: String? = null,
    @SerializedName("id") var id: String? = null,
    @SerializedName("link") var link: String? = null,
    @SerializedName("link_next") var linkNext: String? = null,
    @SerializedName("link_pre") var linkPre: String? = null,
    @SerializedName("nid") var nid: Int = 0,
    @SerializedName("note") var note: String? = null,
    @SerializedName("points") var points: Int = 0,
    @SerializedName("server") var server: String? = null,
    @SerializedName("sid") var sid: Int = 0,
    @SerializedName("trysee") var trysee: Int = 0,
    @SerializedName("url") var url: String? = null,
    @SerializedName("url_next") var urlNext: String? = null,
    @SerializedName("vod_data") var vodData: Map<String, String>? = null
)