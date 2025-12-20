package top.astrasolis.jotter.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import top.astrasolis.jotter.ui.theme.AppTheme
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 通用条目卡片组件
 * 适用于日记、笔记、待办等各类条目的展示
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EntryCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    timestamp: String? = null,
    tags: List<String> = emptyList(),
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    JotterCard(
        modifier = modifier.then(
            if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.spacing.lg),
            verticalAlignment = Alignment.Top,
        ) {
            // 前置内容（如图标、头像等）
            if (leadingContent != null) {
                leadingContent()
                Spacer(modifier = Modifier.width(AppTheme.spacing.md))
            }
            
            // 主内容区
            Column(modifier = Modifier.weight(1f)) {
                // 标题行
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = title,
                        style = MiuixTheme.textStyles.title3,
                        color = MiuixTheme.colorScheme.onBackground,
                        maxLines = 1,
                        modifier = Modifier.weight(1f),
                    )
                    
                    if (timestamp != null) {
                        Spacer(modifier = Modifier.width(AppTheme.spacing.sm))
                        Text(
                            text = timestamp,
                            style = MiuixTheme.textStyles.footnote1,
                            color = MiuixTheme.colorScheme.onBackgroundVariant,
                        )
                    }
                }
                
                // 副标题/内容预览
                if (subtitle.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(AppTheme.spacing.xs))
                    Text(
                        text = subtitle,
                        style = MiuixTheme.textStyles.body2,
                        color = MiuixTheme.colorScheme.onBackgroundVariant,
                        maxLines = 2,
                    )
                }
                
                // 标签
                if (tags.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(AppTheme.spacing.sm))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs),
                        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs),
                    ) {
                        tags.forEach { tag ->
                            EntryTag(text = tag)
                        }
                    }
                }
            }
            
            // 后置内容（如操作按钮等）
            if (trailingContent != null) {
                Spacer(modifier = Modifier.width(AppTheme.spacing.md))
                trailingContent()
            }
        }
    }
}

/**
 * 条目标签
 */
@Composable
private fun EntryTag(text: String) {
    Surface(
        modifier = Modifier.clip(RoundedCornerShape(AppTheme.radii.sm)),
        color = MiuixTheme.colorScheme.primaryContainer,
    ) {
        Text(
            text = text,
            style = MiuixTheme.textStyles.footnote2,
            color = MiuixTheme.colorScheme.primary,
            modifier = Modifier.padding(
                horizontal = AppTheme.spacing.sm,
                vertical = AppTheme.spacing.xxs,
            ),
        )
    }
}
