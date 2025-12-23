package top.astrasolis.jotter.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import top.astrasolis.jotter.data.AppContainer
import top.astrasolis.jotter.data.Todo
import top.astrasolis.jotter.i18n.strings
import top.astrasolis.jotter.ui.components.DeleteConfirmDialog
import top.astrasolis.jotter.ui.components.JotterCard
import top.astrasolis.jotter.ui.components.PageTitleBar
import top.astrasolis.jotter.ui.components.SmallTitle
import top.astrasolis.jotter.ui.components.TodoEditDialog
import top.astrasolis.jotter.ui.theme.AppTheme
import top.astrasolis.jotter.utils.DateUtils
import top.yukonga.miuix.kmp.basic.Checkbox
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 待办事项页面
 * 显示待办列表，支持勾选完成、添加、编辑、删除
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodoScreen(
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val todoRepository = remember { AppContainer.todoRepository }
    
    // 从 Repository 加载数据
    var todos by remember { mutableStateOf<List<Todo>>(emptyList()) }
    var refreshTrigger by remember { mutableStateOf(0) }
    
    LaunchedEffect(refreshTrigger) {
        todos = todoRepository.getActiveTodos()
    }
    
    // 对话框状态
    val showEditDialog = remember { mutableStateOf(false) }
    val showDeleteDialog = remember { mutableStateOf(false) }
    var editingTodo by remember { mutableStateOf<Todo?>(null) }
    var deletingTodo by remember { mutableStateOf<Todo?>(null) }
    
    val pendingTodos = todos.filter { !it.completed }
    val completedTodos = todos.filter { it.completed }
    
    // 刷新数据的辅助函数
    fun refreshData() {
        refreshTrigger++
    }
    
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
                editingTodo = null
                showEditDialog.value = true
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
                                todoRepository.completeTodo(id)
                                refreshData()
                            },
                            onClick = {
                                editingTodo = todo
                                showEditDialog.value = true
                            },
                            onLongClick = {
                                deletingTodo = todo
                                showDeleteDialog.value = true
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
                                todoRepository.uncompleteTodo(id)
                                refreshData()
                            },
                            onClick = {
                                editingTodo = todo
                                showEditDialog.value = true
                            },
                            onLongClick = {
                                deletingTodo = todo
                                showDeleteDialog.value = true
                            },
                        )
                    }
                }
            }
        }
    }
    
    // 编辑对话框
    TodoEditDialog(
        show = showEditDialog,
        todo = editingTodo,
        onSave = { todo ->
            todoRepository.saveTodo(todo)
            refreshData()
        },
    )
    
    // 删除确认对话框
    if (deletingTodo != null) {
        DeleteConfirmDialog(
            show = showDeleteDialog,
            itemName = deletingTodo?.title ?: "",
            onConfirm = {
                deletingTodo?.let { todoRepository.deleteTodo(it.id) }
                deletingTodo = null
                refreshData()
            },
            onCancel = {
                deletingTodo = null
            },
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TodoItemCard(
    todo: Todo,
    onToggle: (String) -> Unit,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
) {
    val today = remember { DateUtils.today() }
    
    JotterCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = onLongClick,
                )
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
                
                // 显示描述或分类
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
                
                // 显示截止日期或完成时间
                val dateInfo = buildDateInfoText(todo, today)
                if (dateInfo != null) {
                    Spacer(modifier = Modifier.height(AppTheme.spacing.xxs))
                    Text(
                        text = dateInfo.text,
                        style = MiuixTheme.textStyles.footnote1,
                        color = dateInfo.color,
                    )
                }
            }
        }
    }
}

/**
 * 日期信息文本
 */
private data class DateInfoText(
    val text: String,
    val color: Color,
)

/**
 * 构建日期信息文本
 */
@OptIn(kotlin.time.ExperimentalTime::class)
@Composable
private fun buildDateInfoText(todo: Todo, today: kotlinx.datetime.LocalDate): DateInfoText? {
    // 已完成：显示完成时间（含时分秒）
    if (todo.completed && todo.completedAt != null) {
        val completedDateTime = Instant.fromEpochMilliseconds(todo.completedAt)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        val completedDate = completedDateTime.date
        val dateStr = strings.formatDate(completedDate.year, completedDate.monthNumber, completedDate.dayOfMonth)
        val timeStr = "%02d:%02d:%02d".format(
            completedDateTime.hour,
            completedDateTime.minute,
            completedDateTime.second
        )
        return DateInfoText(
            text = "${strings.todoCompletedAtPrefix} $dateStr $timeStr",
            color = MiuixTheme.colorScheme.onBackgroundVariant,
        )
    }
    
    // 未完成：显示提醒时间
    if (!todo.completed && todo.dueDateTime != null) {
        val reminderDateTime = Instant.fromEpochMilliseconds(todo.dueDateTime)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        val reminderDate = reminderDateTime.date
        val isOverdue = reminderDate < today
        val dateStr = strings.formatDate(reminderDate.year, reminderDate.monthNumber, reminderDate.dayOfMonth)
        val timeStr = "%02d:%02d".format(reminderDateTime.hour, reminderDateTime.minute)
        
        return if (isOverdue) {
            DateInfoText(
                text = "${strings.todoOverdue}: $dateStr $timeStr",
                color = Color(0xFFE53935),
            )
        } else {
            DateInfoText(
                text = "${strings.todoReminderPrefix}: $dateStr $timeStr",
                color = MiuixTheme.colorScheme.primary,
            )
        }
    }
    
    return null
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
