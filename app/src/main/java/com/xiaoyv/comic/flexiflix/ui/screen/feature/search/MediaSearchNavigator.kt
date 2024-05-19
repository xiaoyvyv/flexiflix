package com.xiaoyv.comic.flexiflix.ui.screen.feature.search

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.xiaoyv.comic.flexiflix.ui.screen.feature.search.result.navigateMediaSearchResult
import com.xiaoyv.flexiflix.common.utils.navigateByPath

/**
 * [addMediaSearchScreen]
 *
 * @author why
 * @since 5/8/24
 */
const val ROUTE_MEDIA_SEARCH = "media-search"
const val EXTRA_MEDIA_SOURCE_ID = "source"
const val EXTRA_MEDIA_SOURCE_NAME = "name"

data class MediaSearchArgument(val sourceId: String, val sourceName: String) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        sourceId = requireNotNull(savedStateHandle[EXTRA_MEDIA_SOURCE_ID]),
        sourceName = savedStateHandle.get<String>(EXTRA_MEDIA_SOURCE_NAME).orEmpty()
    )
}

fun NavController.navigateMediaSearch(sourceId: String, name: String) {
    navigateByPath(ROUTE_MEDIA_SEARCH, listOf(sourceId), mapOf(EXTRA_MEDIA_SOURCE_NAME to name))
}

fun NavGraphBuilder.addMediaSearchScreen(navController: NavController) {
    composable(
        route = ROUTE_MEDIA_SEARCH + "/{${EXTRA_MEDIA_SOURCE_ID}}?$EXTRA_MEDIA_SOURCE_NAME={${EXTRA_MEDIA_SOURCE_NAME}}",
        arguments = listOf(
            navArgument(EXTRA_MEDIA_SOURCE_ID) { type = NavType.StringType },
            navArgument(EXTRA_MEDIA_SOURCE_NAME) {
                type = NavType.StringType
                defaultValue = ""
            }
        )
    ) {
        MediaSearchRoute(
            onNavUp = {
                navController.navigateUp()
            },
            onSearch = { sourceId, param ->
                navController.navigateMediaSearchResult(sourceId, param)
            }
        )
    }
}
