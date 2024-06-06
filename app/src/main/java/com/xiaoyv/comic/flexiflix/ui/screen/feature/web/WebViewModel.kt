package com.xiaoyv.comic.flexiflix.ui.screen.feature.web

import android.webkit.CookieManager
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.xiaoyv.flexiflix.common.utils.debugLog
import com.xiaoyv.flexiflix.common.utils.mutableStateFlowOf
import com.xiaoyv.flexiflix.extension.MediaSourceFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import okhttp3.Cookie
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import javax.inject.Inject

/**
 * [WebViewModel]
 *
 * @author why
 * @since 5/29/24
 */
@HiltViewModel
class WebViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
) : ViewModel() {
    internal val args = WebArgument(stateHandle)

    private val _uiState = mutableStateFlowOf(WebState(url = args.url, title = args.title))
    val uiState = _uiState.asStateFlow()
}