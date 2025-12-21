package top.astrasolis.jotter.ui.components

import androidx.compose.runtime.Composable

/**
 * 跨平台返回键处理
 * 在 Android 上拦截系统返回键，其他平台无操作
 */
@Composable
expect fun PlatformBackHandler(
    enabled: Boolean = true,
    onBack: () -> Unit,
)
