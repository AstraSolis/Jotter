package top.astrasolis.jotter.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import top.yukonga.miuix.kmp.basic.TopAppBar

/**
 * Jotter 顶部导航栏
 */
@Composable
fun JotterTopBar(
    title: String,
    scrollBehavior: ScrollBehavior? = null,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = title,
        navigationIcon = navigationIcon,
        actions = actions,
        scrollBehavior = scrollBehavior,
    )
}
