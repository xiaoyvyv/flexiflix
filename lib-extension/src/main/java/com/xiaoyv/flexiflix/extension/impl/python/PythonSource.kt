package com.xiaoyv.flexiflix.extension.impl.python

import com.xiaoyv.flexiflix.extension.model.FlexMediaDetail
import com.xiaoyv.flexiflix.extension.model.FlexMediaDetailTab
import com.xiaoyv.flexiflix.extension.model.FlexMediaSection
import com.xiaoyv.flexiflix.extension.model.FlexMediaSectionItem
import com.xiaoyv.flexiflix.extension.model.FlexMediaUser
import com.xiaoyv.flexiflix.extension.model.FlexSearchOption
import com.xiaoyv.flexiflix.extension.model.FlexSearchOptionItem
import com.xiaoyv.flexiflix.extension.source.Source
import com.xiaoyv.flexiflix.extension.utils.md5
import java.io.File

/**
 * [PythonSource]
 *
 * @author why
 * @since 5/20/24
 */
class PythonSource(private val pythonPath: String) : Source {
    private val hash by lazy { File(pythonPath).md5() }

    override suspend fun fetchHomeSections(): Result<List<FlexMediaSection>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchSectionMediaPages(
        sectionId: String,
        sectionExtras: Map<String, String>,
        page: Int,
    ): Result<List<FlexMediaSectionItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchUserMediaPages(user: FlexMediaUser): Result<List<FlexMediaSectionItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchMediaDetail(
        id: String,
        extras: Map<String, String>,
    ): Result<FlexMediaDetail> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchMediaDetailRelative(
        relativeTab: FlexMediaDetailTab,
        id: String,
        extras: Map<String, String>,
    ): Result<List<FlexMediaSection>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchMediaSearchConfig(): Result<FlexSearchOption> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchSectionMediaFilter(section: FlexMediaSection): Result<List<FlexSearchOptionItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchMediaSearchResult(
        keyword: String,
        page: Int,
        searchMap: Map<String, String>,
    ): Result<List<FlexMediaSectionItem>> {
        TODO("Not yet implemented")
    }

}