package com.xiaoyv.comic.flexiflix.ui.screen.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.xiaoyv.flexiflix.common.utils.navigateByPath

/**
 * [addMediaDetailScreen]
 *
 * @author why
 * @since 5/8/24
 */
private const val ROUTE_MEDIA_MEDIA = "media-media"
const val PATH_MEDIA_SOURCE_ID = "source"
const val PATH_MEDIA_DETAIL_ID = "id"

data class MediaDetailArgument(val sourceId: String, val mediaId: String) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        sourceId = requireNotNull(savedStateHandle[PATH_MEDIA_SOURCE_ID]),
        mediaId = requireNotNull(savedStateHandle[PATH_MEDIA_DETAIL_ID]),
    )
}

fun NavController.navigateMediaDetail(sourceId: String, mediaId: String) {
    navigateByPath(ROUTE_MEDIA_MEDIA, listOf(sourceId, mediaId))
}

fun NavGraphBuilder.addMediaDetailScreen(navController: NavController) {
    composable(
        route = "%s/{%s}/{%s}".format(
            ROUTE_MEDIA_MEDIA,
            PATH_MEDIA_SOURCE_ID,
            PATH_MEDIA_DETAIL_ID
        ),
        arguments = listOf(
            navArgument(PATH_MEDIA_SOURCE_ID) { type = NavType.StringType },
            navArgument(PATH_MEDIA_DETAIL_ID) { type = NavType.StringType },
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
