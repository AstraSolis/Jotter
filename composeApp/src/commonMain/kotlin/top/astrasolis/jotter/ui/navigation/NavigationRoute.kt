package top.astrasolis.jotter.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * 应用导航路由定义
 */
enum class NavigationRoute(
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector = icon,
) {
    HOME(
        label = "首页",
        icon = Icons.Default.Home,
    ),
    JOURNAL(
        label = "日记",
        icon = Icons.Default.DateRange,
    ),
    TODO(
        label = "待办",
        icon = Icons.Default.CheckCircle,
    ),
    NOTES(
        label = "笔记",
        icon = Icons.Outlined.Edit,
    ),
    SETTINGS(
        label = "设置",
        icon = Icons.Default.Settings,
    ),
}
