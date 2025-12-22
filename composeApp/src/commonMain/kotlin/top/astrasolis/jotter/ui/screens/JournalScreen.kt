package top.astrasolis.jotter.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.astrasolis.jotter.i18n.strings
import top.astrasolis.jotter.ui.components.JotterCard
import top.astrasolis.jotter.ui.components.PageTitleBar
import top.astrasolis.jotter.ui.theme.AppTheme
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 日记页面
 * 以时间线列表展示日记条目
 */
@Composable
fun JournalScreen(
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    // 示例数据
    val journals = remember {
        listOf(
            JournalEntry("2024-12-19", "周四", "今天是个好日子...", "☀️"),
            JournalEntry("2024-12-18", "周三", "会议很顺利...", "😊"),
            JournalEntry("2024-12-17", "周二", "学习了新技术...", "📚"),
        )
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        // 页面标题栏 + 添加按钮
        PageTitleBar(
            title = strings.journalTitle,
            actionIcon = Icons.Default.Add,
            actionContentDescription = strings.journalAdd,
            onAction = {
                // TODO: 打开新建日记页面
            },
        )
        
        if (journals.isEmpty()) {
            EmptyJournalState(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = AppTheme.spacing.screenH,
                    vertical = AppTheme.spacing.sm,
                ),
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
            ) {
                items(journals) { journal ->
                    JournalEntryCard(journal = journal)
                }
            }
        }
    }
}

@Composable
private fun JournalEntryCard(journal: JournalEntry) {
    JotterCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.spacing.lg),
            verticalAlignment = Alignment.Top,
        ) {
            // 日期和心情
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = journal.mood,
                    style = MiuixTheme.textStyles.headline1,
                )
                Spacer(modifier = Modifier.height(AppTheme.spacing.xs))
                Text(
                    text = journal.dayOfWeek,
                    style = MiuixTheme.textStyles.footnote1,
                    color = MiuixTheme.colorScheme.onBackgroundVariant,
                )
            }
            
            Spacer(modifier = Modifier.width(AppTheme.spacing.lg))
            
            // 内容
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = journal.date,
                    style = MiuixTheme.textStyles.subtitle,
                    color = MiuixTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(AppTheme.spacing.sm))
                Text(
                    text = journal.preview,
                    style = MiuixTheme.textStyles.body1,
                    color = MiuixTheme.colorScheme.onBackground,
                    maxLines = 2,
                )
            }
        }
    }
}

@Composable
private fun EmptyJournalState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(AppTheme.spacing.xxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MiuixTheme.colorScheme.onBackgroundVariant,
        )
        Spacer(modifier = Modifier.height(AppTheme.spacing.lg))
        Text(
            text = strings.journalEmpty,
            style = MiuixTheme.textStyles.title2,
            color = MiuixTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(AppTheme.spacing.sm))
        Text(
            text = strings.journalEmptyHint,
            style = MiuixTheme.textStyles.body1,
            color = MiuixTheme.colorScheme.onBackgroundVariant,
        )
    }
}

/**
 * 日记条目数据类
 */
private data class JournalEntry(
    val date: String,
    val dayOfWeek: String,
    val preview: String,
    val mood: String,
)
