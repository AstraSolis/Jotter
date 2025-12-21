package top.astrasolis.jotter.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 小标题组件
 */
@Composable
fun SmallTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier,
        style = MiuixTheme.textStyles.subtitle,
        color = MiuixTheme.colorScheme.onBackgroundVariant,
    )
}
