package com.xiaoyv.comic.flexiflix.ui.screen.feature.search

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.xiaoyv.comic.flexiflix.data.media.MediaRepository
import com.xiaoyv.comic.flexiflix.model.SearchRequestParam
import com.xiaoyv.flexiflix.common.model.StateContent
import com.xiaoyv.flexiflix.common.utils.defaultPaging
import com.xiaoyv.flexiflix.common.utils.mutableStateFlowOf
import com.xiaoyv.flexiflix.extension.model.FlexKeyValue
import com.xiaoyv.flexiflix.extension.model.FlexMediaSectionItem
import com.xiaoyv.flexiflix.extension.model.FlexSearchOptionItem
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [MediaSearchViewModel]
 *
 * @author why
 * @since 5/9/24
 */
@HiltViewModel
class MediaSearchViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    savedStateHandle: SavedStateHandle,
    private val mediaRepository: MediaRepository,
) : ViewModel() {
    val args = MediaSearchArgument(savedStateHandle)

    private val _searchOptionsState = mutableStateFlowOf(MediaSearchState())
    val searchOptionsState get() = _searchOptionsState.asStateFlow()

    /**
     * 搜索关键字和选中的过滤项目
     */
    private val _keyword = mutableStateFlowOf<String>("")
    val keyword get() = _keyword.asStateFlow()

    private val _selectedOptions = mutableStateFlowOf(listOf<FlexSearchOptionItem>())
    val selectedOptions get() = _selectedOptions.asStateFlow()

    /**
     * 搜索请求参数
     */
    fun buildParam(): SearchRequestParam {
        return SearchRequestParam(
            sourceId = args.sourceId,
            keyword = _keyword.value,
            queryMap = _selectedOptions.value.toQueryMap()
        )
    }

    /**
     * 搜索数据
     */
    val items: StateFlow<PagingData<FlexMediaSectionItem>> =
        combine(_keyword, _selectedOptions) { query, filters ->
            query to filters
        }.flatMapLatest { (query, filters) ->
            searchMediaPagingSource(query, filters)
        }.stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    /**
     * 搜索关键字改变
     */
    fun onKeywordChange(keyword: String) {
        _keyword.update { keyword }
    }

    /**
     * 过滤项目选取改变
     */
    fun onSelectedValuesChange(option: FlexSearchOptionItem, selected: List<FlexKeyValue>) {
        val oldOptions = _selectedOptions.value.toMutableList()

        // 替换旧的该项数据
        oldOptions.removeIf { it.key == option.key }

        // 添加一个只有选取项的 FlexSearchOptionItem
        oldOptions.add(
            FlexSearchOptionItem(
                key = option.key,
                values = selected
            )
        )

        // 更新
        _selectedOptions.update { oldOptions }
    }

    init {
        loadSearchOptions()
    }

    /**
     * 搜索分页数据源
     */
    private fun searchMediaPagingSource(
        keyword: String,
        options: List<FlexSearchOptionItem>,
    ): Flow<PagingData<FlexMediaSectionItem>> {
        return defaultPaging {
            mediaRepository.searchSource(
                sourceId = args.sourceId,
                keyword = keyword,
                queryMap = options.toQueryMap()
            )
        }
    }


    /**
     * 加载搜索的参数配置项目
     */
    private fun loadSearchOptions() {
        viewModelScope.launch {
            _searchOptionsState.update { it.copy(loadState = LoadState.Loading) }
            val state = mediaRepository.getMediaSearchOption(args.sourceId)
                .map {
                    MediaSearchState(
                        loadState = LoadState.NotLoading(true),
                        data = StateContent.Payload(it)
                    )
                }
                .getOrElse {
                    MediaSearchState(loadState = LoadState.Error(it))
                }
            _searchOptionsState.update { state }
        }
    }

    /**
     * 装换为链接的请求参数
     */
    private fun List<FlexSearchOptionItem>.toQueryMap(): Map<String, String> {
        return associate {
            val mergeSymbol = it.mergeSymbol
                .orEmpty()
                .trim()
                .ifBlank { "," }

            val queryValue = it.values
                .orEmpty()
                .joinToString(mergeSymbol) { kv -> kv.key }

            it.key to queryValue
        }
    }
}
