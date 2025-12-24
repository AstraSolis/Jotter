package top.astrasolis.jotter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import top.astrasolis.jotter.data.PriorityColors
import top.astrasolis.jotter.i18n.strings
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 优先级 Badge 组件
 * 显示 P1-P5 彩色标签
 */
@Composable
fun PriorityBadge(
    priority: Int,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = Color(PriorityColors.forPriority(priority))
    // P1-P3 使用白色文字，P4-P5 使用深色文字
    val textColor = if (priority <= 3) Color.White else MiuixTheme.colorScheme.onBackground
    
    Text(
        text = strings.todoPriorityValue(priority),
        style = MiuixTheme.textStyles.footnote2,
        color = textColor,
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(backgroundColor)
            .padding(horizontal = 6.dp, vertical = 2.dp),
    )
}
