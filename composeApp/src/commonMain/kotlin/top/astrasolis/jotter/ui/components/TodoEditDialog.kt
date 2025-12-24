@file:OptIn(kotlin.time.ExperimentalTime::class)
package top.astrasolis.jotter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import top.astrasolis.jotter.data.AppContainer
import top.astrasolis.jotter.data.Todo
import top.astrasolis.jotter.i18n.strings
import top.astrasolis.jotter.ui.theme.AppTheme
import top.astrasolis.jotter.utils.TimeUtils
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.extra.SuperDialog
import top.yukonga.miuix.kmp.extra.SuperDropdown
import top.yukonga.miuix.kmp.theme.MiuixTheme
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * 待办编辑对话框
 * 支持添加新待办和编辑已有待办
 */
@OptIn(ExperimentalUuidApi::class)
@Composable
fun TodoEditDialog(
    show: MutableState<Boolean>,
    todo: Todo? = null,
    onSave: (Todo) -> Unit,
    onDelete: ((String) -> Unit)? = null,
) {
    val isEditing = todo != null
    val title = if (isEditing) strings.todoEditTitle else strings.todoAddTitle
    
    var todoTitle by remember(todo) { mutableStateOf(todo?.title ?: "") }
    var todoDescription by remember(todo) { mutableStateOf(todo?.description ?: "") }
    var todoTag by remember(todo) { mutableStateOf(todo?.tag ?: "") }
    var todoDueDateTime by remember(todo) { mutableStateOf(todo?.dueDateTime) }
    // 优先级状态 (index 0-4 对应 P1-P5)
    var selectedPriorityIndex by remember(todo) { mutableStateOf((todo?.priority ?: 5) - 1) }
    
    SuperDialog(
        title = title,
        show = show,
        onDismissRequest = { show.value = false },
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // 标题输入
            TextField(
                value = todoTitle,
                onValueChange = { todoTitle = it },
                modifier = Modifier.fillMaxWidth(),
                label = strings.todoTitleLabel,
            )
            
            Spacer(modifier = Modifier.height(AppTheme.spacing.md))
            
            // 描述输入
            TextField(
                value = todoDescription,
                onValueChange = { todoDescription = it },
                modifier = Modifier.fillMaxWidth(),
                label = strings.todoDescLabel,
            )
            
            Spacer(modifier = Modifier.height(AppTheme.spacing.md))
            
            // 标签选择
            TagSelector(
                selectedTag = todoTag.ifEmpty { null },
                onTagSelected = { tag -> todoTag = tag ?: "" },
            )
            
            Spacer(modifier = Modifier.height(AppTheme.spacing.md))
            
            // 优先级选择
            val priorityOptions = (1..5).map { strings.todoPriorityValue(it) }
            top.yukonga.miuix.kmp.basic.Card {
                SuperDropdown(
                    title = strings.todoPriorityLabel,
                    items = priorityOptions,
                    selectedIndex = selectedPriorityIndex,
                    onSelectedIndexChange = { selectedPriorityIndex = it },
                )
            }
            
            Spacer(modifier = Modifier.height(AppTheme.spacing.md))
            
            // 提醒时间选择
            val showDatePicker = remember { mutableStateOf(false) }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = strings.todoReminderLabel,
                        style = MiuixTheme.textStyles.footnote1,
                        color = MiuixTheme.colorScheme.onBackgroundVariant,
                    )
                    Spacer(modifier = Modifier.height(AppTheme.spacing.xxs))
                    Text(
                        text = formatDateTime(todoDueDateTime),
                        style = MiuixTheme.textStyles.body2,
                        color = if (todoDueDateTime != null) {
                            MiuixTheme.colorScheme.primary
                        } else {
                            MiuixTheme.colorScheme.onBackgroundVariant
                        },
                    )
                }
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
                ) {
                    if (todoDueDateTime != null) {
                        TextButton(
                            text = strings.todoClearReminder,
                            onClick = { todoDueDateTime = null },
                        )
                    }
                    TextButton(
                        text = strings.todoSetReminder,
                        onClick = { showDatePicker.value = true },
                    )
                }
            }
            
            // 日期时间选择器对话框
            DatePickerDialog(
                show = showDatePicker,
                selectedDateTime = todoDueDateTime,
                onDateTimeSelected = { dateTime -> todoDueDateTime = dateTime },
            )
            
            Spacer(modifier = Modifier.height(AppTheme.spacing.lg))
            
            // 按钮行
            Row(modifier = Modifier.fillMaxWidth()) {
                TextButton(
                    text = strings.cancel,
                    onClick = { show.value = false },
                    modifier = Modifier.weight(1f),
                )
                
                Spacer(modifier = Modifier.width(AppTheme.spacing.md))
                
                Button(
                    onClick = {
                        val trimmedTitle = todoTitle.trim()
                        if (trimmedTitle.isNotEmpty()) {
                            val currentTime = TimeUtils.now()
                            val savedTodo = if (isEditing && todo != null) {
                                todo.copy(
                                    title = trimmedTitle,
                                    description = todoDescription.trim(),
                                    tag = todoTag.trim().ifEmpty { null },
                                    priority = selectedPriorityIndex + 1,
                                    dueDateTime = todoDueDateTime,
                                    updatedAt = currentTime,
                                )
                            } else {
                                Todo(
                                    id = Uuid.random().toString(),
                                    title = trimmedTitle,
                                    description = todoDescription.trim(),
                                    tag = todoTag.trim().ifEmpty { null },
                                    priority = selectedPriorityIndex + 1,
                                    dueDateTime = todoDueDateTime,
                                    createdAt = currentTime,
                                    updatedAt = currentTime,
                                )
                            }
                            onSave(savedTodo)
                            show.value = false
                        }
                    },
                    modifier = Modifier.weight(1f),
                ) {
                    Text(text = strings.save)
                }
            }
        }
    }
}

/**
 * 格式化日期时间显示
 */
@Composable
private fun formatDateTime(epochMs: Long?): String {
    if (epochMs == null) return strings.todoNoReminder
    
    val dateTime = Instant.fromEpochMilliseconds(epochMs)
        .toLocalDateTime(TimeZone.currentSystemDefault())
    val dateStr = strings.formatDate(dateTime.year, dateTime.monthNumber, dateTime.dayOfMonth)
    val timeStr = "%02d:%02d".format(dateTime.hour, dateTime.minute)
    return "$dateStr $timeStr"
}

/**
 * 标签选择器组件
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TagSelector(
    selectedTag: String?,
    onTagSelected: (String?) -> Unit,
) {
    val tagRepository = remember { AppContainer.tagRepository }
    var availableTags by remember { mutableStateOf(tagRepository.getTags()) }
    var showNewTagInput by remember { mutableStateOf(false) }
    var newTagText by remember { mutableStateOf("") }
    
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = strings.todoTagLabel,
            style = MiuixTheme.textStyles.footnote1,
            color = MiuixTheme.colorScheme.onBackgroundVariant,
        )
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.xs))
        
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
        ) {
            // "无" 选项
            TagChip(
                text = strings.todoNoTag,
                isSelected = selectedTag == null,
                onClick = { onTagSelected(null) },
            )
            
            // 已有标签
            availableTags.forEach { tag ->
                TagChip(
                    text = tag,
                    isSelected = selectedTag == tag,
                    onClick = { onTagSelected(tag) },
                )
            }
            
            // 新建按钮
            TagChip(
                text = "+ ${strings.todoAddTag}",
                isSelected = false,
                isAction = true,
                onClick = { showNewTagInput = true },
            )
        }
        
        // 新建标签输入
        if (showNewTagInput) {
            Spacer(modifier = Modifier.height(AppTheme.spacing.sm))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextField(
                    value = newTagText,
                    onValueChange = { newTagText = it },
                    modifier = Modifier.weight(1f),
                    label = strings.todoAddTag,
                )
                TextButton(
                    text = strings.confirm,
                    onClick = {
                        val trimmed = newTagText.trim()
                        if (trimmed.isNotEmpty()) {
                            tagRepository.addTag(trimmed)
                            availableTags = tagRepository.getTags()
                            onTagSelected(trimmed)
                            newTagText = ""
                            showNewTagInput = false
                        }
                    },
                )
            }
        }
    }
}

/**
 * 标签 Chip 组件
 */
@Composable
private fun TagChip(
    text: String,
    isSelected: Boolean,
    isAction: Boolean = false,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(16.dp)
    val backgroundColor = when {
        isSelected -> MiuixTheme.colorScheme.primary.copy(alpha = 0.15f)
        isAction -> MiuixTheme.colorScheme.surfaceVariant
        else -> MiuixTheme.colorScheme.surfaceVariant
    }
    val borderColor = if (isSelected) {
        MiuixTheme.colorScheme.primary
    } else {
        MiuixTheme.colorScheme.outline.copy(alpha = 0.3f)
    }
    val textColor = when {
        isSelected -> MiuixTheme.colorScheme.primary
        isAction -> MiuixTheme.colorScheme.primary
        else -> MiuixTheme.colorScheme.onSurface
    }
    
    Text(
        text = text,
        style = MiuixTheme.textStyles.footnote1,
        color = textColor,
        modifier = Modifier
            .clip(shape)
            .background(backgroundColor)
            .border(1.dp, borderColor, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
    )
}
