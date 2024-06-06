package com.xiaoyv.comic.flexiflix.ui.screen.feature.web

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.xiaoyv.flexiflix.common.utils.navigateByPath
import com.xiaoyv.flexiflix.extension.utils.decodeUrl
import com.xiaoyv.flexiflix.extension.utils.encodeUrl

/**
 * [addWebScreen]
 *
 * @author why
 * @since 5/8/24
 */
private const val ROUTE_MEDIA_SEARCH = "web"
const val EXTRA_WEB_TITLE = "title"
const val EXTRA_WEB_URL = "url"

data class WebArgument(val title: String, val url: String) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        title = savedStateHandle.get<String>(EXTRA_WEB_TITLE).decodeUrl(),
        url = savedStateHandle.get<String>(EXTRA_WEB_URL).decodeUrl()
    )
}

fun NavController.navigateWeb(title: String, url: String) {
    navigateByPath(
        route = ROUTE_MEDIA_SEARCH,
        optional = mapOf(
            EXTRA_WEB_TITLE to title.encodeUrl(),
            EXTRA_WEB_URL to url.encodeUrl()
        ),
    )
}

fun NavGraphBuilder.addWebScreen(navController: NavController) {
    composable(
        route = "%s?%s={%s}&%s={%s}".format(
            ROUTE_MEDIA_SEARCH,
            EXTRA_WEB_TITLE, EXTRA_WEB_TITLE,
            EXTRA_WEB_URL, EXTRA_WEB_URL,
        ),
        arguments = listOf(
            navArgument(EXTRA_WEB_TITLE) { type = NavType.StringType },
            navArgument(EXTRA_WEB_URL) { type = NavType.StringType },
        )
    ) {
        WebRoute(
            onNavUp = {
                navController.navigateUp()
            },
        )
    }
}
