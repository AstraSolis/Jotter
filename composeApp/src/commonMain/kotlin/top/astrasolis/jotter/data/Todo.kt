package top.astrasolis.jotter.data

import kotlinx.serialization.Serializable

/**
 * 待办事项数据类
 * 统一的待办事项模型，供全应用使用
 * 时间戳使用 epoch milliseconds (Long) 存储
 */
@Serializable
data class Todo(
    /** 唯一标识 (UUID) */
    val id: String,
    /** 标题 */
    val title: String,
    /** 详细描述 */
    val description: String = "",
    /** 是否已完成 */
    val completed: Boolean = false,
    /** 标签 */
    val tag: String? = null,
    /** 优先级 */
    val priority: Priority = Priority.NORMAL,
    /** 提醒时间 (epoch milliseconds) */
    val dueDateTime: Long? = null,
    /** 完成时间 (epoch milliseconds) */
    val completedAt: Long? = null,
    /** 创建时间 (epoch milliseconds) */
    val createdAt: Long,
    /** 更新时间 (epoch milliseconds) */
    val updatedAt: Long,
) {
    @Serializable
    enum class Priority {
        LOW,
        NORMAL,
        HIGH,
    }
}

/**
 * 待办事项列表容器
 * 用于 JSON 序列化
 */
@Serializable
data class TodoList(
    val todos: List<Todo> = emptyList(),
)
