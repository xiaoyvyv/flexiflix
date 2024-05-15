package com.xiaoyv.flexiflix.extension.java.annotation

/**
 * [MediaSource]
 *
 * 标记一个 Class 为数据源
 *
 * @author why
 * @since 5/8/24
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class MediaSource(
    /**
     * 数据源唯一ID
     */
    val id: String,

    /**
     * 数据源名称
     */
    val name: String,

    /**
     * 数据源描述
     */
    val description: String,

    /**
     * 数据源作者
     */
    val author: String,

    /**
     * 成人内容
     */
    val nsfw: Boolean,

    /**
     * 数据源版本代码
     */
    val versionCode: Int = 1,

    /**
     * 数据源版本名称
     */
    val versionName: String = "v1.0.0"
)