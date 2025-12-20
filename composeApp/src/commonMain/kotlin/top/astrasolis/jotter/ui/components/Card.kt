package top.astrasolis.jotter.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import top.astrasolis.jotter.ui.theme.horizontalPadding
import top.yukonga.miuix.kmp.basic.Card as MiuixCard

/**
 * Jotter 卡片组件
 * 封装 Miuix Card，自动添加水平内边距
 */
@Composable
fun JotterCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    MiuixCard(
        modifier = modifier.horizontalPadding(),
    ) {
        content()
    }
}
