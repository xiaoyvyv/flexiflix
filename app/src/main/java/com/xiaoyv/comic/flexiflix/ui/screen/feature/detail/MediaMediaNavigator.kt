package com.xiaoyv.comic.flexiflix.ui.screen.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.xiaoyv.flexiflix.common.utils.navigateSafe

/**
 * [addMediaDetailScreen]
 *
 * @author why
 * @since 5/8/24
 */
const val ROUTE_MEDIA_MEDIA = "media-media"
const val EXTRA_MEDIA_SOURCE_ID = "source"
const val EXTRA_MEDIA_DETAIL_ID = "id"

data class MediaDetailArgument(val sourceId: String, val mediaId: String) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        sourceId = requireNotNull(savedStateHandle[EXTRA_MEDIA_SOURCE_ID]),
        mediaId = requireNotNull(savedStateHandle[EXTRA_MEDIA_DETAIL_ID]),
    )
}

fun NavController.navigateMediaDetail(sourceId: String, mediaId: String) {
    navigateSafe(ROUTE_MEDIA_MEDIA, sourceId, mediaId)
}

fun NavGraphBuilder.addMediaDetailScreen(navController: NavController) {
    composable(
        route = ROUTE_MEDIA_MEDIA + "/{${EXTRA_MEDIA_SOURCE_ID}}/{${EXTRA_MEDIA_DETAIL_ID}}",
        arguments = listOf(
            navArgument(EXTRA_MEDIA_SOURCE_ID) { type = NavType.StringType },
            navArgument(EXTRA_MEDIA_DETAIL_ID) { type = NavType.StringType },
        )
    ) {
        MediaDetailRoute(
            onNavUp = { navController.popBackStack() },
            onSectionMediaClick = { sourceId, media ->
                navController.navigateMediaDetail(sourceId, media.id)
            }
        )
    }
}
