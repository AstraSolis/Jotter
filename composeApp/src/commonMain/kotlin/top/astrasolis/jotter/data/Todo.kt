package top.astrasolis.jotter.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.intOrNull

/**
 * 优先级序列化器
 * 兼容旧数据中的 enum 字符串格式 (LOW, NORMAL, HIGH) 和新的 Int 格式 (1-5)
 */
object PrioritySerializer : KSerializer<Int> {
    override val descriptor = PrimitiveSerialDescriptor("Priority", PrimitiveKind.INT)
    
    override fun serialize(encoder: Encoder, value: Int) {
        encoder.encodeInt(value)
    }
    
    override fun deserialize(decoder: Decoder): Int {
        val jsonDecoder = decoder as? JsonDecoder
            ?: return decoder.decodeInt()
        
        return when (val element = jsonDecoder.decodeJsonElement()) {
            is JsonPrimitive -> {
                // 尝试解析为 Int
                element.intOrNull ?: run {
                    // 旧 enum 格式转换
                    when (element.content.uppercase()) {
                        "LOW" -> 4
                        "NORMAL" -> 5
                        "HIGH" -> 2
                        else -> 5
                    }
                }
            }
            else -> 5
        }
    }
}

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
    /** 优先级 1-5，数字越小优先级越高，默认5 */
    @Serializable(with = PrioritySerializer::class)
    val priority: Int = 5,
    /** 提醒时间 (epoch milliseconds) */
    val dueDateTime: Long? = null,
    /** 完成时间 (epoch milliseconds) */
    val completedAt: Long? = null,
    /** 创建时间 (epoch milliseconds) */
    val createdAt: Long,
    /** 更新时间 (epoch milliseconds) */
    val updatedAt: Long,
)

/**
 * 待办事项列表容器
 * 用于 JSON 序列化
 */
@Serializable
data class TodoList(
    val todos: List<Todo> = emptyList(),
)
