package com.xiaoyv.comic.flexiflix.ui.screen.feature.search.result

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.xiaoyv.comic.flexiflix.model.SearchRequestParam
import com.xiaoyv.comic.flexiflix.ui.screen.feature.detail.navigateMediaDetail
import com.xiaoyv.flexiflix.common.utils.decodeBase64Obj
import com.xiaoyv.flexiflix.common.utils.navigateByPath
import com.xiaoyv.flexiflix.common.utils.toBase64Obj

/**
 * [addMediaSearchResultScreen]
 *
 * @author why
 * @since 5/8/24
 */
const val ROUTE_MEDIA_SEARCH_RESULT = "media-search-result"
const val EXTRA_MEDIA_SOURCE_ID = "source"
const val EXTRA_MEDIA_SEARCH_PARAM = "param"

data class MediaSearchResultArgument(val sourceId: String, val param: SearchRequestParam) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        sourceId = requireNotNull(savedStateHandle[EXTRA_MEDIA_SOURCE_ID]),
        param = savedStateHandle.decodeBase64Obj(EXTRA_MEDIA_SEARCH_PARAM)
    )
}

fun NavController.navigateMediaSearchResult(sourceId: String, param: SearchRequestParam) {
    navigateByPath(
        route = ROUTE_MEDIA_SEARCH_RESULT,
        params = listOf(sourceId, param.toBase64Obj()),
    )
}

fun NavGraphBuilder.addMediaSearchResultScreen(navController: NavController) {
    composable(
        route = ROUTE_MEDIA_SEARCH_RESULT + "/{${EXTRA_MEDIA_SOURCE_ID}}/{${EXTRA_MEDIA_SEARCH_PARAM}}",
        arguments = listOf(
            navArgument(EXTRA_MEDIA_SOURCE_ID) { type = NavType.StringType },
            navArgument(EXTRA_MEDIA_SEARCH_PARAM) { type = NavType.StringType }
        )
    ) {
        MediaSearchResultRoute(
            onNavUp = {
                navController.navigateUp()
            },
            onSectionMediaClick = { sourceId, item ->
                navController.navigateMediaDetail(sourceId, item.id)
            }
        )
    }
}
