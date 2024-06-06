package com.xiaoyv.comic.flexiflix.ui.screen.feature.home

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.xiaoyv.comic.flexiflix.ui.screen.feature.detail.navigateMediaDetail
import com.xiaoyv.comic.flexiflix.ui.screen.feature.search.navigateMediaSearch
import com.xiaoyv.comic.flexiflix.ui.screen.feature.section.navigateMediaSection
import com.xiaoyv.comic.flexiflix.ui.screen.feature.web.navigateWeb
import com.xiaoyv.flexiflix.common.utils.navigateByPath

/**
 * [addMediaHomeScreen]
 *
 * @author why
 * @since 5/8/24
 */
private const val ROUTE_MEDIA_HOME = "media-home"
const val PATH_MEDIA_SOURCE_ID = "source"
const val EXTRA_MEDIA_SOURCE_NAME = "name"

data class MediaHomeArgument(val sourceId: String, val sourceName: String) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        sourceId = requireNotNull(savedStateHandle[PATH_MEDIA_SOURCE_ID]),
        sourceName = savedStateHandle.get<String>(EXTRA_MEDIA_SOURCE_NAME).orEmpty()
    )
}

fun NavController.navigateMediaHome(sourceId: String, name: String) {
    navigateByPath(ROUTE_MEDIA_HOME, listOf(sourceId), mapOf(EXTRA_MEDIA_SOURCE_NAME to name))
}

fun NavGraphBuilder.addMediaHomeScreen(navController: NavController) {
    composable(
        route = "%s/{%s}?%s={%s}".format(
            ROUTE_MEDIA_HOME,
            PATH_MEDIA_SOURCE_ID,
            EXTRA_MEDIA_SOURCE_NAME, EXTRA_MEDIA_SOURCE_NAME
        ),
        arguments = listOf(
            navArgument(PATH_MEDIA_SOURCE_ID) { type = NavType.StringType },
            navArgument(EXTRA_MEDIA_SOURCE_NAME) {
                type = NavType.StringType
                defaultValue = ""
            }
        )
    ) {
        MediaHomeRoute(
            onSectionClick = { sourceId, section ->
                navController.navigateMediaSection(
                    sourceId = sourceId,
                    section = section,
                )
            },
            onSectionMediaClick = { sourceId, media ->
                navController.navigateMediaDetail(sourceId, media.id)
            },
            onSearchClick = { sourceId, sourceName ->
                navController.navigateMediaSearch(sourceId, sourceName)
            },
            onVerifyUrl = { title, url ->
                navController.navigateWeb(title, url)
            }
        )
    }
}
