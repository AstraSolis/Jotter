package top.astrasolis.jotter.utils

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * 日期工具类
 */
object DateUtils {
    
    /**
     * 获取当前日期
     * 使用 kotlin.time.Clock.System (Kotlin 2.1+)
     */
    @OptIn(ExperimentalTime::class)
    fun today(): LocalDate {
        val now = Clock.System.now()
        return now.toLocalDateTime(TimeZone.currentSystemDefault()).date
    }
    
    /**
     * 格式化日期为中文格式
     * 例如：2025年12月20日
     */
    fun formatDateChinese(date: LocalDate): String {
        return "${date.year}年${date.month.ordinal + 1}月${date.day}日"
    }
    
    /**
     * 获取星期的中文表示
     */
    fun getDayOfWeekChinese(date: LocalDate): String {
        return when (date.dayOfWeek) {
            DayOfWeek.MONDAY -> "星期一"
            DayOfWeek.TUESDAY -> "星期二"
            DayOfWeek.WEDNESDAY -> "星期三"
            DayOfWeek.THURSDAY -> "星期四"
            DayOfWeek.FRIDAY -> "星期五"
            DayOfWeek.SATURDAY -> "星期六"
            DayOfWeek.SUNDAY -> "星期日"
        }
    }
}
