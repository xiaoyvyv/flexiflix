package com.xiaoyv.comic.flexiflix.ui.screen.feature.section

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.xiaoyv.comic.flexiflix.ui.screen.feature.detail.navigateMediaDetail
import com.xiaoyv.flexiflix.common.utils.decodeBase64Obj
import com.xiaoyv.flexiflix.common.utils.navigateByPath
import com.xiaoyv.flexiflix.common.utils.toBase64Obj
import com.xiaoyv.flexiflix.extension.model.FlexMediaSection

/**
 * [addMediaSectionScreen]
 *
 * @author why
 * @since 5/8/24
 */
private const val ROUTE_MEDIA_SEARCH = "media-section"
const val PATH_MEDIA_SOURCE_ID = "source"
const val PATH_MEDIA_SECTION = "section"

data class MediaSectionArgument(
    val sourceId: String,
    val section: FlexMediaSection,
) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        sourceId = requireNotNull(savedStateHandle[PATH_MEDIA_SOURCE_ID]),
        section = savedStateHandle.decodeBase64Obj(PATH_MEDIA_SECTION)
    )
}

fun NavController.navigateMediaSection(
    sourceId: String,
    section: FlexMediaSection,
) {
    navigateByPath(
        route = ROUTE_MEDIA_SEARCH,
        params = listOf(sourceId, section.toBase64Obj()),
    )
}

fun NavGraphBuilder.addMediaSectionScreen(navController: NavController) {
    composable(
        route = "%s/{%s}/{%s}".format(
            ROUTE_MEDIA_SEARCH,
            PATH_MEDIA_SOURCE_ID,
            PATH_MEDIA_SECTION,
        ),
        arguments = listOf(
            navArgument(PATH_MEDIA_SOURCE_ID) { type = NavType.StringType },
            navArgument(PATH_MEDIA_SECTION) { type = NavType.StringType },
        )
    ) {
        MediaSectionRoute(
            onNavUp = {
                navController.navigateUp()
            },
            onMediaClick = { sourceId, mediaId ->
                navController.navigateMediaDetail(sourceId, mediaId)
            }
        )
    }
}
