package top.astrasolis.jotter.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

/**
 * Android 平台返回键处理
 * 使用 BackHandler 拦截系统返回键
 */
@Composable
actual fun PlatformBackHandler(
    enabled: Boolean,
    onBack: () -> Unit,
) {
    BackHandler(enabled = enabled, onBack = onBack)
}
