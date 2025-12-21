package top.astrasolis.jotter.data.model

import kotlinx.serialization.Serializable

/**
 * 笔记数据模型
 * 笔记以 UUID 命名存储为 markdown 文件
 * 时间戳使用 epoch milliseconds (Long) 存储
 */
@Serializable
data class Note(
    /** 唯一标识 (UUID) */
    val id: String,
    /** 标题 */
    val title: String,
    /** Markdown 内容 */
    val content: String,
    /** 标签列表 */
    val tags: List<String> = emptyList(),
    /** 创建时间 (epoch milliseconds) */
    val createdAt: Long,
    /** 更新时间 (epoch milliseconds) */
    val updatedAt: Long,
)

/**
 * 笔记元数据
 * 用于索引和快速列表展示，存储在 .metadata.json 中
 */
@Serializable
data class NoteMetadata(
    val id: String,
    val title: String,
    val tags: List<String> = emptyList(),
    val createdAt: Long,
    val updatedAt: Long,
)

/**
 * 笔记索引
 * 存储所有笔记的元数据
 */
@Serializable
data class NoteIndex(
    val notes: List<NoteMetadata> = emptyList(),
)
