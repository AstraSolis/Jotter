@file:OptIn(kotlin.time.ExperimentalTime::class)
package top.astrasolis.jotter.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * 获取当前时间的 epoch milliseconds
 * 使用 expect/actual 模式实现跨平台
 */
expect fun currentTimeMillis(): Long

/**
 * 时间工具函数
 */
object TimeUtils {
    /**
     * 获取当前时间的 epoch milliseconds
     */
    fun now(): Long = currentTimeMillis()
    
    /**
     * 获取今日日期
     */
    fun today(): LocalDate {
        val instant = Instant.fromEpochMilliseconds(now())
        return instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
    }
    
    /**
     * 获取当前年份
     */
    fun currentYear(): Int {
        val instant = Instant.fromEpochMilliseconds(now())
        return instant.toLocalDateTime(TimeZone.currentSystemDefault()).year
    }
    
    /**
     * 将 epoch milliseconds 转换为 LocalDate
     */
    fun toLocalDate(epochMillis: Long): LocalDate {
        val instant = Instant.fromEpochMilliseconds(epochMillis)
        return instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
    }
}
