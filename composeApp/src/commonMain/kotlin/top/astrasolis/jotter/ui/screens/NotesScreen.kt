package top.astrasolis.jotter.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import top.astrasolis.jotter.ui.components.JotterCard
import top.astrasolis.jotter.ui.components.PageTitleBar
import top.astrasolis.jotter.ui.theme.AppTheme
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 笔记页面
 * 以网格视图展示笔记卡片
 */
@Composable
fun NotesScreen(
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    // 示例数据
    val notes = remember {
        listOf(
            NoteItem(1, "会议纪要", "讨论了项目进度和下一步计划...", listOf("工作", "会议")),
            NoteItem(2, "读书笔记", "《Clean Code》第三章：函数应该短小...", listOf("学习")),
            NoteItem(3, "旅行计划", "目的地：杭州\n行程：3天2晚\n预算：2000元", listOf("生活", "旅行")),
            NoteItem(4, "Kotlin 技巧", "使用 sealed class 处理状态...", listOf("技术", "Kotlin")),
            NoteItem(5, "想法", "也许可以尝试一下新的方法...", listOf("灵感")),
        )
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        // 页面标题栏 + 添加按钮
        PageTitleBar(
            title = "笔记",
            actionIcon = Icons.Default.Add,
            actionContentDescription = "新建笔记",
            onAction = {
                // TODO: 打开新建笔记页面
            },
        )
        
        if (notes.isEmpty()) {
            EmptyNotesState(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
            )
        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(160.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = AppTheme.spacing.screenH,
                    vertical = AppTheme.spacing.sm,
                ),
                horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
                verticalItemSpacing = AppTheme.spacing.md,
            ) {
                items(notes) { note ->
                    NoteCard(note = note)
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun NoteCard(note: NoteItem) {
    JotterCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.spacing.lg),
        ) {
            Text(
                text = note.title,
                style = MiuixTheme.textStyles.title3,
                color = MiuixTheme.colorScheme.onBackground,
                maxLines = 1,
            )
            
            Spacer(modifier = Modifier.height(AppTheme.spacing.sm))
            
            Text(
                text = note.content,
                style = MiuixTheme.textStyles.body2,
                color = MiuixTheme.colorScheme.onBackgroundVariant,
                maxLines = 4,
            )
            
            if (note.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(AppTheme.spacing.md))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs),
                ) {
                    note.tags.forEach { tag ->
                        TagChip(text = tag)
                    }
                }
            }
        }
    }
}

@Composable
private fun TagChip(text: String) {
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

@Composable
private fun EmptyNotesState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(AppTheme.spacing.xxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Outlined.Edit,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MiuixTheme.colorScheme.onBackgroundVariant,
        )
        Spacer(modifier = Modifier.height(AppTheme.spacing.lg))
        Text(
            text = "还没有笔记",
            style = MiuixTheme.textStyles.title2,
            color = MiuixTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(AppTheme.spacing.sm))
        Text(
            text = "点击右下角按钮创建新笔记",
            style = MiuixTheme.textStyles.body1,
            color = MiuixTheme.colorScheme.onBackgroundVariant,
        )
    }
}

/**
 * 笔记数据类
 */
private data class NoteItem(
    val id: Int,
    val title: String,
    val content: String,
    val tags: List<String>,
)
