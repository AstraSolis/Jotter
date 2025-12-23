@file:OptIn(kotlin.time.ExperimentalTime::class)
package top.astrasolis.jotter.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import top.astrasolis.jotter.data.Todo
import top.astrasolis.jotter.i18n.strings
import top.astrasolis.jotter.ui.theme.AppTheme
import top.astrasolis.jotter.utils.TimeUtils
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.extra.SuperDialog
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
    var todoDueDateTime by remember(todo) { mutableStateOf(todo?.dueDateTime) }
    
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
                                    dueDateTime = todoDueDateTime,
                                    updatedAt = currentTime,
                                )
                            } else {
                                Todo(
                                    id = Uuid.random().toString(),
                                    title = trimmedTitle,
                                    description = todoDescription.trim(),
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
