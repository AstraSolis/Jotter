package top.astrasolis.jotter.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import top.astrasolis.jotter.data.Todo
import top.astrasolis.jotter.i18n.strings
import top.astrasolis.jotter.ui.components.JotterCard
import top.astrasolis.jotter.ui.components.PageTitleBar
import top.astrasolis.jotter.ui.components.SmallTitle
import top.astrasolis.jotter.ui.theme.AppTheme
import top.astrasolis.jotter.utils.TimeUtils
import top.yukonga.miuix.kmp.basic.Checkbox
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * 待办事项页面
 * 显示待办列表，支持勾选完成
 */
@OptIn(ExperimentalUuidApi::class)
@Composable
fun TodoScreen(
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val currentTime = remember { TimeUtils.now() }
    
    // 示例数据
    var todos by remember {
        mutableStateOf(
            listOf(
                Todo(
                    id = Uuid.random().toString(),
                    title = "完成项目文档",
                    completed = false,
                    category = "工作",
                    createdAt = currentTime,
                    updatedAt = currentTime,
                ),
                Todo(
                    id = Uuid.random().toString(),
                    title = "购买日用品",
                    completed = false,
                    category = "生活",
                    createdAt = currentTime,
                    updatedAt = currentTime,
                ),
                Todo(
                    id = Uuid.random().toString(),
                    title = "锻炼30分钟",
                    completed = true,
                    category = "健康",
                    createdAt = currentTime,
                    updatedAt = currentTime,
                ),
                Todo(
                    id = Uuid.random().toString(),
                    title = "阅读技术书籍",
                    completed = false,
                    category = "学习",
                    createdAt = currentTime,
                    updatedAt = currentTime,
                ),
                Todo(
                    id = Uuid.random().toString(),
                    title = "整理房间",
                    completed = true,
                    category = "生活",
                    createdAt = currentTime,
                    updatedAt = currentTime,
                ),
            )
        )
    }
    
    val pendingTodos = todos.filter { !it.completed }
    val completedTodos = todos.filter { it.completed }
    
    // 提前获取字符串（用于非 @Composable 上下文）
    val newItemTitle = strings.todoNewItem
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        // 页面标题栏 + 添加按钮
        PageTitleBar(
            title = strings.todoTitle,
            actionIcon = Icons.Default.Add,
            actionContentDescription = strings.todoAdd,
            onAction = {
                // 添加新待办
                val createTime = TimeUtils.now()
                val newTodo = Todo(
                    id = Uuid.random().toString(),
                    title = newItemTitle,
                    completed = false,
                    createdAt = createTime,
                    updatedAt = createTime,
                )
                todos = todos + newTodo
            },
        )
        
        if (todos.isEmpty()) {
            EmptyTodoState(
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
                // 待完成
                if (pendingTodos.isNotEmpty()) {
                    item {
                        SmallTitle(
                            text = strings.todoPendingCount(pendingTodos.size),
                            modifier = Modifier.padding(start = AppTheme.spacing.xs),
                        )
                    }
                    items(pendingTodos, key = { it.id }) { todo ->
                        TodoItemCard(
                            todo = todo,
                            onToggle = { id ->
                                todos = todos.map {
                                    if (it.id == id) it.copy(
                                        completed = !it.completed,
                                        updatedAt = TimeUtils.now(),
                                    ) else it
                                }
                            },
                        )
                    }
                }
                
                // 已完成
                if (completedTodos.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(AppTheme.spacing.md))
                        SmallTitle(
                            text = strings.todoCompletedCount(completedTodos.size),
                            modifier = Modifier.padding(start = AppTheme.spacing.xs),
                        )
                    }
                    items(completedTodos, key = { it.id }) { todo ->
                        TodoItemCard(
                            todo = todo,
                            onToggle = { id ->
                                todos = todos.map {
                                    if (it.id == id) it.copy(
                                        completed = !it.completed,
                                        updatedAt = TimeUtils.now(),
                                    ) else it
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TodoItemCard(
    todo: Todo,
    onToggle: (String) -> Unit,
) {
    JotterCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle(todo.id) }
                .padding(AppTheme.spacing.lg),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = todo.completed,
                onCheckedChange = { onToggle(todo.id) },
            )
            
            Spacer(modifier = Modifier.width(AppTheme.spacing.md))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = todo.title,
                    style = MiuixTheme.textStyles.body1,
                    color = if (todo.completed) {
                        MiuixTheme.colorScheme.onBackgroundVariant
                    } else {
                        MiuixTheme.colorScheme.onBackground
                    },
                    textDecoration = if (todo.completed) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    },
                )
                if (todo.category != null || todo.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(AppTheme.spacing.xxs))
                    Row {
                        if (todo.category != null) {
                            Text(
                                text = todo.category,
                                style = MiuixTheme.textStyles.footnote1,
                                color = MiuixTheme.colorScheme.primary,
                            )
                        }
                        if (todo.description.isNotEmpty()) {
                            val prefix = if (todo.category != null) " · " else ""
                            Text(
                                text = "$prefix${todo.description}",
                                style = MiuixTheme.textStyles.footnote1,
                                color = MiuixTheme.colorScheme.onBackgroundVariant,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyTodoState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(AppTheme.spacing.xxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MiuixTheme.colorScheme.onBackgroundVariant,
        )
        Spacer(modifier = Modifier.height(AppTheme.spacing.lg))
        Text(
            text = strings.todoEmpty,
            style = MiuixTheme.textStyles.title2,
            color = MiuixTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(AppTheme.spacing.sm))
        Text(
            text = strings.todoEmptyHint,
            style = MiuixTheme.textStyles.body1,
            color = MiuixTheme.colorScheme.onBackgroundVariant,
        )
    }
}
