package com.xiaoyv.comic.flexiflix.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Source
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.xiaoyv.comic.flexiflix.ui.screen.ROUTE_MAIN_TAB_COLLECT
import com.xiaoyv.comic.flexiflix.ui.screen.ROUTE_MAIN_TAB_HISTORY
import com.xiaoyv.comic.flexiflix.ui.screen.ROUTE_MAIN_TAB_PROFILE
import com.xiaoyv.comic.flexiflix.ui.screen.ROUTE_MAIN_TAB_SOURCE
import com.xiaoyv.flexiflix.i18n.I18n

data class TopLevelDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int
)

val TOP_LEVEL_DESTINATIONS = listOf(
    TopLevelDestination(
        route = ROUTE_MAIN_TAB_COLLECT,
        selectedIcon = Icons.Default.EditCalendar,
        unselectedIcon = Icons.Default.EditCalendar,
        iconTextId = I18n.tab_collect
    ),
    TopLevelDestination(
        route = ROUTE_MAIN_TAB_HISTORY,
        selectedIcon = Icons.Default.History,
        unselectedIcon = Icons.Default.History,
        iconTextId = I18n.tab_history
    ),
    TopLevelDestination(
        route = ROUTE_MAIN_TAB_SOURCE,
        selectedIcon = Icons.Default.Api,
        unselectedIcon = Icons.Default.Api,
        iconTextId = I18n.tab_source
    ),
    TopLevelDestination(
        route = ROUTE_MAIN_TAB_PROFILE,
        selectedIcon = Icons.Default.Settings,
        unselectedIcon = Icons.Default.Settings,
        iconTextId = I18n.tab_profile
    )
)

/**
 * 移动端模式下，底部导航栏
 */
@Composable
fun BottomNavBar(
    selectedDestination: String,
    navigateToTopLevelDestination: (TopLevelDestination) -> Unit
) {
    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        TOP_LEVEL_DESTINATIONS.forEach { destination ->
            NavigationBarItem(
                selected = selectedDestination == destination.route,
                alwaysShowLabel = true,
                label = {
                    Text(
                        text = stringResource(id = destination.iconTextId)
                    )
                },
                icon = {
                    Icon(
                        imageVector = destination.selectedIcon,
                        contentDescription = stringResource(id = destination.iconTextId)
                    )
                },
                onClick = { navigateToTopLevelDestination(destination) }
            )
        }
    }
}
