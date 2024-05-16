package com.xiaoyv.flexiflix.extension.impl.javascript

import android.util.Log
import com.xiaoyv.flexiflix.extension.model.FlexMediaDetail
import com.xiaoyv.flexiflix.extension.model.FlexMediaDetailTab
import com.xiaoyv.flexiflix.extension.model.FlexMediaSection
import com.xiaoyv.flexiflix.extension.model.FlexMediaSectionItem
import com.xiaoyv.flexiflix.extension.model.FlexMediaUser
import com.xiaoyv.flexiflix.extension.model.MediaSourceInfo
import com.xiaoyv.flexiflix.extension.source.Source
import com.xiaoyv.flexiflix.extension.utils.md5
import com.xiaoyv.flexiflix.extension.utils.runCatchingPrint
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import java.io.File

/**
 * [JSExtensionSource]
 *
 * @author why
 * @since 5/15/24
 */
class JSExtensionSource(private val jsPath: String) : Source {
    private val hash by lazy { File(jsPath).md5() }

    /**
     * 确保 NodeJS 服务正在运行
     */
    private suspend fun makeSureServiceRunning() {
        if (JSExtensionService.instacne.isRunning) return
        runCatchingPrint {
            withTimeout(15000) {
                JSExtensionService.instacne.startServer()

                while (true) {
                    val running = runCatching { JSExtensionService.instacne.jsApi.isRunning() }
                        .map { it.isSuccessful }
                        .getOrElse { false }
                    if (running) break
                    delay(100)
                }
            }
        }
    }

    suspend fun fetchJsExtensionInfo(): MediaSourceInfo {
        makeSureServiceRunning()
        val data = JSExtensionService.instacne.jsApi.getExtensionInfo(hash)
        return MediaSourceInfo.loadFromMap(data)
    }

    override suspend fun fetchHomeSections(): Result<List<FlexMediaSection>> {
        return runCatchingPrint {
            makeSureServiceRunning()
            JSExtensionService.instacne.jsApi.fetchHomeSections(hash = hash)
        }
    }

    override suspend fun fetchSectionMediaPages(
        sectionId: String,
        sectionExtras: Map<String, String>,
        page: Int
    ): Result<List<FlexMediaSectionItem>> {
        return runCatchingPrint {
            makeSureServiceRunning()
            JSExtensionService.instacne.jsApi.fetchSectionMediaPages(
                hash = hash,
                sectionId = sectionId,
                page = page,
                sectionExtras = sectionExtras
            )
        }
    }

    override suspend fun fetchUserMediaPages(user: FlexMediaUser): Result<List<FlexMediaSectionItem>> {
        return runCatchingPrint {
            makeSureServiceRunning()
            JSExtensionService.instacne.jsApi.fetchUserMediaPages(
                hash = hash,
                user = user
            )
        }
    }

    override suspend fun fetchMediaDetail(
        id: String,
        extras: Map<String, String>
    ): Result<FlexMediaDetail> {
        return runCatchingPrint {
            makeSureServiceRunning()
            JSExtensionService.instacne.jsApi.fetchMediaDetail(
                hash = hash,
                id = id,
                extras = extras
            )
        }
    }

    override suspend fun fetchMediaDetailRelative(
        relativeTab: FlexMediaDetailTab,
        id: String,
        extras: Map<String, String>
    ): Result<List<FlexMediaSection>> {
        return runCatchingPrint {
            makeSureServiceRunning()
            JSExtensionService.instacne.jsApi.fetchMediaDetailRelative(
                hash = hash,
                relativeTab = relativeTab,
                id = id,
                extras = extras
            )
        }
    }
}