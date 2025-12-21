package top.astrasolis.jotter.ui.components

import androidx.compose.runtime.Composable

/**
 * JVM 桌面平台返回键处理
 * 桌面平台没有系统返回键，无需处理
 */
@Composable
actual fun PlatformBackHandler(
    enabled: Boolean,
    onBack: () -> Unit,
) {
    // 桌面平台没有系统返回键，无需处理
}
