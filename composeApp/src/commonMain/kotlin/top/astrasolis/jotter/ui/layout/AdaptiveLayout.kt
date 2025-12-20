package top.astrasolis.jotter.ui.layout

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import top.yukonga.miuix.kmp.basic.Scaffold

/**
 * 简化布局容器
 * 不使用底部导航栏，通过功能按钮和返回按钮导航
 */
@Composable
fun AdaptiveLayout(
    topBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = topBar,
        floatingActionButton = floatingActionButton,
    ) { innerPadding ->
        content(innerPadding)
    }
}


