package top.astrasolis.jotter.data.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

/**
 * 日记数据模型
 * 日记按日期存储为单独的 markdown 文件
 * 时间戳使用 epoch milliseconds (Long) 存储
 */
@Serializable
data class Journal(
    /** 日期作为唯一标识 (格式: yyyy-MM-dd) */
    val date: LocalDate,
    /** 标题 */
    val title: String,
    /** Markdown 内容 */
    val content: String,
    /** 心情标签 */
    val mood: String? = null,
    /** 天气 */
    val weather: String? = null,
    /** 创建时间 (epoch milliseconds) */
    val createdAt: Long,
    /** 更新时间 (epoch milliseconds) */
    val updatedAt: Long,
)

/**
 * 日记元数据
 * 用于快速列表展示，不包含完整内容
 */
@Serializable
data class JournalMetadata(
    val date: LocalDate,
    val title: String,
    val mood: String? = null,
    val weather: String? = null,
    val updatedAt: Long,
)
