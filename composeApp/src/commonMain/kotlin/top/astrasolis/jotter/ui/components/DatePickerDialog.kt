@file:OptIn(kotlin.time.ExperimentalTime::class)
package top.astrasolis.jotter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import top.astrasolis.jotter.i18n.strings
import top.astrasolis.jotter.ui.theme.AppTheme
import top.astrasolis.jotter.utils.DateUtils
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.extra.SuperDialog
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 日期时间选择对话框
 * 提供完整的日历视图选择日期和时间
 */
@Composable
fun DatePickerDialog(
    show: MutableState<Boolean>,
    selectedDateTime: Long?,
    onDateTimeSelected: (Long?) -> Unit,
) {
    val today = remember { DateUtils.today() }
    val tz = TimeZone.currentSystemDefault()
    
    // 解析已选时间
    val initialDateTime = selectedDateTime?.let {
        Instant.fromEpochMilliseconds(it).toLocalDateTime(tz)
    }
    
    var displayMonth by remember { mutableStateOf(initialDateTime?.date ?: today) }
    var tempSelectedDate by remember(selectedDateTime) { mutableStateOf(initialDateTime?.date) }
    var tempHour by remember(selectedDateTime) { mutableIntStateOf(initialDateTime?.hour ?: 9) }
    var tempMinute by remember(selectedDateTime) { mutableIntStateOf(initialDateTime?.minute ?: 0) }
    
    SuperDialog(
        title = strings.todoReminderLabel,
        show = show,
        onDismissRequest = { show.value = false },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            // 月份导航
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = {
                        displayMonth = displayMonth.minus(1, DateTimeUnit.MONTH)
                    },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = null,
                        tint = MiuixTheme.colorScheme.onBackground,
                    )
                }
                
                Text(
                    text = strings.formatYearMonth(displayMonth.year, displayMonth.monthNumber),
                    style = MiuixTheme.textStyles.title3,
                    color = MiuixTheme.colorScheme.onBackground,
                )
                
                IconButton(
                    onClick = {
                        displayMonth = displayMonth.plus(1, DateTimeUnit.MONTH)
                    },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = MiuixTheme.colorScheme.onBackground,
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(AppTheme.spacing.sm))
            
            // 星期标题行
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                listOf(
                    DayOfWeek.MONDAY,
                    DayOfWeek.TUESDAY,
                    DayOfWeek.WEDNESDAY,
                    DayOfWeek.THURSDAY,
                    DayOfWeek.FRIDAY,
                    DayOfWeek.SATURDAY,
                    DayOfWeek.SUNDAY,
                ).forEach { dayOfWeek ->
                    Text(
                        text = strings.getDayOfWeekShort(dayOfWeek.ordinal + 1),
                        style = MiuixTheme.textStyles.footnote2,
                        color = MiuixTheme.colorScheme.onBackgroundVariant,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(AppTheme.spacing.xs))
            
            // 日历网格
            val daysInMonth = getDaysInMonth(displayMonth.year, displayMonth.month)
            val firstDayOfMonth = LocalDate(displayMonth.year, displayMonth.month, 1)
            val startDayOfWeek = firstDayOfMonth.dayOfWeek.ordinal
            
            val calendarDays = buildList {
                repeat(startDayOfWeek) { add(null) }
                for (day in 1..daysInMonth) {
                    add(LocalDate(displayMonth.year, displayMonth.month, day))
                }
            }
            
            // 计算实际行数
            val totalCells = calendarDays.size
            val rowCount = (totalCells + 6) / 7 // 向上取整
            val gridHeight = (rowCount * 36).dp
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier.fillMaxWidth().height(gridHeight),
                horizontalArrangement = Arrangement.spacedBy(0.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                items(calendarDays) { date ->
                    if (date == null) {
                        Box(modifier = Modifier.height(36.dp))
                    } else {
                        val isSelected = date == tempSelectedDate
                        val isToday = date == today
                        
                        Box(
                            modifier = Modifier
                                .height(36.dp)
                                .fillMaxWidth()
                                .clickable { tempSelectedDate = date },
                            contentAlignment = Alignment.Center,
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isSelected -> MiuixTheme.colorScheme.primary
                                            else -> MiuixTheme.colorScheme.background
                                        }
                                    ),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = date.dayOfMonth.toString(),
                                    style = MiuixTheme.textStyles.body2,
                                    color = when {
                                        isSelected -> MiuixTheme.colorScheme.onPrimary
                                        isToday -> MiuixTheme.colorScheme.primary
                                        else -> MiuixTheme.colorScheme.onBackground
                                    },
                                )
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(AppTheme.spacing.xs))
            
            // 时间选择器
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // 小时
                NumberPicker(
                    value = tempHour,
                    range = 0..23,
                    onValueChange = { tempHour = it },
                )
                
                Text(
                    text = ":",
                    style = MiuixTheme.textStyles.title2,
                    color = MiuixTheme.colorScheme.onBackground,
                )
                
                // 分钟
                NumberPicker(
                    value = tempMinute,
                    range = 0..59,
                    onValueChange = { tempMinute = it },
                )
            }
            
            Spacer(modifier = Modifier.height(AppTheme.spacing.md))
            
            // 按钮行
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                TextButton(
                    text = strings.todoClearReminder,
                    onClick = {
                        onDateTimeSelected(null)
                        show.value = false
                    },
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
                ) {
                    TextButton(
                        text = strings.cancel,
                        onClick = { show.value = false },
                    )
                    
                    TextButton(
                        text = strings.confirm,
                        onClick = {
                            val date = tempSelectedDate
                            if (date != null) {
                                val dateTime = LocalDateTime(
                                    date.year, date.month, date.dayOfMonth,
                                    tempHour, tempMinute
                                )
                                val epochMs = dateTime.toInstant(tz).toEpochMilliseconds()
                                onDateTimeSelected(epochMs)
                            } else {
                                onDateTimeSelected(null)
                            }
                            show.value = false
                        },
                    )
                }
            }
        }
    }
}

/**
 * 简单数字选择器
 */
@Composable
private fun NumberPicker(
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = {
                val newValue = if (value > range.first) value - 1 else range.last
                onValueChange(newValue)
            },
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = null,
                tint = MiuixTheme.colorScheme.onBackground,
            )
        }
        
        Text(
            text = "%02d".format(value),
            style = MiuixTheme.textStyles.title2,
            color = MiuixTheme.colorScheme.onBackground,
            modifier = Modifier.width(48.dp),
            textAlign = TextAlign.Center,
        )
        
        IconButton(
            onClick = {
                val newValue = if (value < range.last) value + 1 else range.first
                onValueChange(newValue)
            },
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MiuixTheme.colorScheme.onBackground,
            )
        }
    }
}

/**
 * 获取指定月份的天数
 */
private fun getDaysInMonth(year: Int, month: Month): Int {
    return when (month) {
        Month.JANUARY, Month.MARCH, Month.MAY, Month.JULY,
        Month.AUGUST, Month.OCTOBER, Month.DECEMBER -> 31
        Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30
        Month.FEBRUARY -> if (isLeapYear(year)) 29 else 28
        else -> 30
    }
}

/**
 * 判断是否为闰年
 */
private fun isLeapYear(year: Int): Boolean {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}
