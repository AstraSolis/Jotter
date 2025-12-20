package top.astrasolis.jotter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import top.astrasolis.jotter.ui.theme.AppTheme
import top.astrasolis.jotter.ui.theme.pressScale
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 功能入口数据
 */
data class FeatureEntry(
    val id: String,
    val label: String,
    val icon: ImageVector,
)

/**
 * 功能入口椭圆形按钮行
 * 按钮为胶囊形状，水平排列
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FeatureEntryRow(
    entries: List<FeatureEntry>,
    onEntryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
    ) {
        entries.forEach { entry ->
            CapsuleButton(
                text = entry.label,
                onClick = { onEntryClick(entry.id) },
            )
        }
    }
}

// 胶囊形状
private val CapsuleShape = RoundedCornerShape(50)

/**
 * 胶囊形状按钮
 * 现代化设计：渐变背景 + 柔和阴影 + 按压缩放动画
 */
@Composable
private fun CapsuleButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // 渐变背景色 - 柔和风格
    val gradientColors = listOf(
        MiuixTheme.colorScheme.surface,
        MiuixTheme.colorScheme.surfaceVariant,
    )
    
    Box(
        modifier = modifier
            // 按压缩放动画 - 明显效果 (0.92 缩放)
            .pressScale(onClick = onClick)
            // 柔和阴影
            .shadow(
                elevation = AppTheme.elevation.sm,
                shape = CapsuleShape,
                ambientColor = MiuixTheme.colorScheme.primary.copy(alpha = 0.1f),
                spotColor = MiuixTheme.colorScheme.primary.copy(alpha = 0.15f),
            )
            .clip(CapsuleShape)
            // 渐变背景
            .background(
                brush = Brush.horizontalGradient(colors = gradientColors),
                shape = CapsuleShape,
            )
            .padding(horizontal = 24.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = MiuixTheme.textStyles.body1,
            color = MiuixTheme.colorScheme.onBackground,
        )
    }
}

