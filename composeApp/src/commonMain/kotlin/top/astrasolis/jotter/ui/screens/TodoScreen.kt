package top.astrasolis.jotter.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer

import androidx.compose.ui.unit.dp
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import top.astrasolis.jotter.data.AppContainer
import top.astrasolis.jotter.data.Todo
import top.astrasolis.jotter.i18n.strings
import top.astrasolis.jotter.ui.components.DeleteConfirmDialog
import top.astrasolis.jotter.ui.components.JotterCard
import top.astrasolis.jotter.ui.components.PageTitleBar
import top.astrasolis.jotter.ui.components.PriorityBadge
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
@OptIn(ExperimentalFoundationApi::class, kotlin.time.ExperimentalTime::class)
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
        todos = todoRepository.getAllTodos()
    }
    
    // 对话框状态
    val showEditDialog = remember { mutableStateOf(false) }
    val showDeleteDialog = remember { mutableStateOf(false) }
    var editingTodo by remember { mutableStateOf<Todo?>(null) }
    var deletingTodo by remember { mutableStateOf<Todo?>(null) }
    
    // 未完成待办：分为无时间和有时间两部分
    val allPendingTodos = todos.filter { !it.completed }
    // 无时间的待办按优先级排序
    val noDatePendingTodos = allPendingTodos
        .filter { it.dueDateTime == null }
        .sortedBy { it.priority }
    // 有时间的待办按日期分组，每组按优先级排序
    val pendingTodosByDate: Map<LocalDate, List<Todo>> = allPendingTodos
        .filter { it.dueDateTime != null }
        .groupBy { todo ->
            Instant.fromEpochMilliseconds(todo.dueDateTime!!)
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date
        }
        .mapValues { (_, todosInDate) -> todosInDate.sortedBy { it.priority } }
        .toSortedMap()  // 按日期升序（最早的在前）
    
    val completedTodos = todos.filter { it.completed }
    
    // 按完成日期对已完成待办进行分组（最近的日期在前），每组内按优先级排序
    val completedTodosByDate: Map<LocalDate, List<Todo>> = completedTodos
        .filter { it.completedAt != null }
        .groupBy { todo ->
            Instant.fromEpochMilliseconds(todo.completedAt!!)
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date
        }
        .mapValues { (_, todosInDate) -> todosInDate.sortedBy { it.priority } }
        .toSortedMap(compareByDescending { it })
    
    // 记录每个日期组的展开状态（未完成和已完成分开管理）
    // - 未完成的分组默认展开
    var expandedPendingDates by remember(pendingTodosByDate.keys) { 
        mutableStateOf<Set<LocalDate>>(pendingTodosByDate.keys.toSet()) 
    }
    // - 已完成的分组默认收起
    var expandedCompletedDates by remember { 
        mutableStateOf<Set<LocalDate>>(emptySet()) 
    }
    
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
                if (allPendingTodos.isNotEmpty()) {
                    item {
                        SmallTitle(
                            text = strings.todoPendingCount(allPendingTodos.size),
                            modifier = Modifier.padding(start = AppTheme.spacing.xs),
                        )
                    }
                    
                    // 1. 先显示无时间的待办（不显示分组标题）
                    if (noDatePendingTodos.isNotEmpty()) {
                        items(noDatePendingTodos, key = { it.id }) { todo ->
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
                    
                    // 2. 然后按日期分组显示有时间的待办
                    pendingTodosByDate.forEach { (date, todosInDate) ->
                        item(key = "header-pending-$date") {
                            DateGroupHeader(
                                date = date,
                                count = todosInDate.size,
                                expanded = date in expandedPendingDates,
                                onToggle = {
                                    expandedPendingDates = if (date in expandedPendingDates) {
                                        expandedPendingDates - date
                                    } else {
                                        expandedPendingDates + date
                                    }
                                },
                            )
                        }
                        
                        if (date in expandedPendingDates) {
                            items(todosInDate, key = { it.id }) { todo ->
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
                    }
                }
                
                // 已完成 - 按日期分组，支持展开/收起
                if (completedTodosByDate.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(AppTheme.spacing.md))
                        SmallTitle(
                            text = strings.todoCompletedCount(completedTodos.size),
                            modifier = Modifier.padding(start = AppTheme.spacing.xs),
                        )
                    }
                    
                    completedTodosByDate.forEach { (date, todosInDate) ->
                        // 日期组 Header（可点击展开/收起）
                        item(key = "header-$date") {
                            DateGroupHeader(
                                date = date,
                                count = todosInDate.size,
                                expanded = date in expandedCompletedDates,
                                onToggle = {
                                    expandedCompletedDates = if (date in expandedCompletedDates) {
                                        expandedCompletedDates - date
                                    } else {
                                        expandedCompletedDates + date
                                    }
                                },
                            )
                        }
                        
                        // 仅在展开时显示该组的待办
                        if (date in expandedCompletedDates) {
                            items(todosInDate, key = { it.id }) { todo ->
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

@OptIn(ExperimentalFoundationApi::class, kotlin.time.ExperimentalTime::class)
@Composable
private fun TodoItemCard(
    todo: Todo,
    onToggle: (String) -> Unit,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
) {
    val today = remember { DateUtils.today() }
    
    // 判断是否逾期
    val isOverdue = remember(todo) {
        if (todo.completed || todo.dueDateTime == null) {
            false
        } else {
            val dueDate = Instant.fromEpochMilliseconds(todo.dueDateTime)
                .toLocalDateTime(TimeZone.currentSystemDefault()).date
            dueDate < today
        }
    }
    
    JotterCard {
        val dateInfo = buildDateInfoText(todo, today)
        
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
            // 左侧：Checkbox + 优先级 + 标题 + 备注
            Checkbox(
                checked = todo.completed,
                onCheckedChange = { onToggle(todo.id) },
            )
            
            Spacer(modifier = Modifier.width(AppTheme.spacing.md))
            
            PriorityBadge(priority = todo.priority)
            
            Spacer(modifier = Modifier.width(AppTheme.spacing.sm))
            
            Text(
                text = todo.title,
                style = MiuixTheme.textStyles.body1,
                color = when {
                    todo.completed -> MiuixTheme.colorScheme.onBackgroundVariant
                    isOverdue -> Color(0xFFE53935)
                    else -> MiuixTheme.colorScheme.onBackground
                },
            )
            
            if (todo.description.isNotEmpty()) {
                Spacer(modifier = Modifier.width(AppTheme.spacing.xs))
                Text(
                    text = "(${todo.description})",
                    style = MiuixTheme.textStyles.footnote1,
                    color = MiuixTheme.colorScheme.onBackgroundVariant,
                    maxLines = 1,
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // 右侧：标签/时间（Column 用于两行时垂直排列）
            Column(
                horizontalAlignment = Alignment.End,
            ) {
                // 标签（如果有）
                if (todo.tag != null) {
                    Text(
                        text = todo.tag,
                        style = MiuixTheme.textStyles.footnote2,
                        color = MiuixTheme.colorScheme.primary,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MiuixTheme.colorScheme.primary.copy(alpha = 0.1f))
                            .padding(horizontal = 8.dp, vertical = 2.dp),
                    )
                }
                
                // 时间（如果有）
                if (dateInfo != null) {
                    if (todo.tag != null) {
                        Spacer(modifier = Modifier.height(AppTheme.spacing.xxs))
                    }
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
                text = "$dateStr $timeStr",
                color = Color(0xFFE53935),
            )
        } else {
            DateInfoText(
                text = "$dateStr $timeStr",
                color = MiuixTheme.colorScheme.primary,
            )
        }
    }
    
    return null
}


/**
 * 日期分组 Header - 可展开/收起
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DateGroupHeader(
    date: LocalDate,
    count: Int,
    expanded: Boolean,
    onToggle: () -> Unit,
) {
    // 箭头旋转动画
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "arrow_rotation",
    )
    
    val dateStr = strings.formatDate(date.year, date.monthNumber, date.dayOfMonth)
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppTheme.radii.md))
            .combinedClickable(onClick = onToggle)
            .padding(vertical = AppTheme.spacing.sm, horizontal = AppTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = if (expanded) strings.actionCollapse else strings.actionExpand,
            modifier = Modifier
                .size(20.dp)
                .graphicsLayer { rotationZ = rotation },
            tint = MiuixTheme.colorScheme.onBackgroundVariant,
        )
        
        Spacer(modifier = Modifier.width(AppTheme.spacing.xs))
        
        Text(
            text = "$dateStr ($count)",
            style = MiuixTheme.textStyles.footnote1,
            color = MiuixTheme.colorScheme.onBackgroundVariant,
        )
    }
}

/**
 * 无时间分组 Header - 用于显示未设置时间的待办
 */
@Composable
private fun NoDateGroupHeader(
    count: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = AppTheme.spacing.sm, horizontal = AppTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "${strings.todoNoDateGroup} ($count)",
            style = MiuixTheme.textStyles.footnote1,
            color = MiuixTheme.colorScheme.onBackgroundVariant,
        )
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
