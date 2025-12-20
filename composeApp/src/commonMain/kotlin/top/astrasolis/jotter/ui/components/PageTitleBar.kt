package top.astrasolis.jotter.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import top.astrasolis.jotter.ui.theme.AppTheme
import top.astrasolis.jotter.ui.theme.pressScale
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 页面标题栏组件
 * 左侧标题 + 右侧可选操作按钮
 */
@Composable
fun PageTitleBar(
    title: String,
    modifier: Modifier = Modifier,
    actionIcon: ImageVector? = null,
    actionContentDescription: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = AppTheme.spacing.screenH,
                vertical = AppTheme.spacing.lg,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 标题
        Text(
            text = title,
            style = MiuixTheme.textStyles.title2,
            color = MiuixTheme.colorScheme.onBackground,
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        // 操作按钮
        if (actionIcon != null && onAction != null) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .pressScale(onClick = onAction)
                    .padding(8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = actionIcon,
                    contentDescription = actionContentDescription,
                    tint = MiuixTheme.colorScheme.primary,
                )
            }
        }
    }
}
