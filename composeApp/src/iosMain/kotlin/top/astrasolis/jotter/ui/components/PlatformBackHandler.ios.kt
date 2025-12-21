package top.astrasolis.jotter.ui.components

import androidx.compose.runtime.Composable

/**
 * iOS 平台返回键处理
 * iOS 没有系统返回键，无需处理
 */
@Composable
actual fun PlatformBackHandler(
    enabled: Boolean,
    onBack: () -> Unit,
) {
    // iOS 没有系统返回键，无需处理
}
