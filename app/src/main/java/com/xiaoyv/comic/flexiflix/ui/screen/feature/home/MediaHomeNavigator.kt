package com.xiaoyv.comic.flexiflix.ui.screen.feature.home

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.xiaoyv.comic.flexiflix.ui.screen.feature.detail.ROUTE_MEDIA_MEDIA
import com.xiaoyv.comic.flexiflix.ui.screen.feature.detail.navigateMediaDetail
import com.xiaoyv.flexiflix.common.utils.navigateSafe

/**
 * [addMediaHomeScreen]
 *
 * @author why
 * @since 5/8/24
 */
const val ROUTE_MEDIA_HOME = "media-home"
const val EXTRA_MEDIA_SOURCE_ID = "source"

data class MediaHomeArgument(val sourceId: String) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        sourceId = requireNotNull(savedStateHandle[EXTRA_MEDIA_SOURCE_ID])
    )
}

fun NavController.navigateMediaHome(sourceId: String) {
    navigateSafe(ROUTE_MEDIA_HOME, sourceId)
}

fun NavGraphBuilder.addMediaHomeScreen(navController: NavController) {
    composable(
        route = ROUTE_MEDIA_HOME + "/{${EXTRA_MEDIA_SOURCE_ID}}",
        arguments = listOf(navArgument(EXTRA_MEDIA_SOURCE_ID) {
            type = NavType.StringType
        })
    ) {
        MediaHomeRoute(
            onSectionClick = { sourceId, section ->

            },
            onSectionMediaClick = { sourceId, media ->
                navController.navigateMediaDetail(sourceId, media.id)
            }
        )
    }
}
