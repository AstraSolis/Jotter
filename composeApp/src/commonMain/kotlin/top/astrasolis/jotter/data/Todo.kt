package top.astrasolis.jotter.data

/**
 * 待办事项数据类
 * 统一的待办事项模型，供全应用使用
 */
data class Todo(
    val id: Int,
    val title: String,
    val completed: Boolean,
    val category: String? = null,
    val dueDate: String? = null,
)
