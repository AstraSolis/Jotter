package top.astrasolis.jotter.ui.components

import androidx.compose.runtime.Composable

/**
 * Wasm JS Web 平台返回键处理
 * Web 平台没有系统返回键，无需处理
 */
@Composable
actual fun PlatformBackHandler(
    enabled: Boolean,
    onBack: () -> Unit,
) {
    // Web 平台没有系统返回键，无需处理
}
