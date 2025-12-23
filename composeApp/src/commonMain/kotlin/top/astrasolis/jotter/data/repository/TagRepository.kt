package top.astrasolis.jotter.data.repository

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okio.Path
import top.astrasolis.jotter.platform.PlatformFileSystem

/**
 * 标签列表容器
 */
@Serializable
data class TagList(
    val tags: List<String> = emptyList(),
)

/**
 * 标签仓库
 * 管理用户创建的标签列表
 */
class TagRepository(
    private val fileSystem: PlatformFileSystem,
    private val getDataPath: () -> Path,
) {
    private val json = Json { 
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
    
    private val tagsFile: Path
        get() = getDataPath() / "todos" / "tags.json"
    
    /**
     * 获取所有标签
     */
    fun getTags(): List<String> {
        val content = fileSystem.readText(tagsFile) ?: return emptyList()
        return try {
            json.decodeFromString<TagList>(content).tags
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    /**
     * 添加标签
     */
    fun addTag(tag: String) {
        val trimmed = tag.trim()
        if (trimmed.isEmpty()) return
        
        val tags = getTags().toMutableList()
        if (!tags.contains(trimmed)) {
            tags.add(trimmed)
            saveTags(tags)
        }
    }
    
    /**
     * 删除标签
     */
    fun deleteTag(tag: String) {
        val tags = getTags().filter { it != tag }
        saveTags(tags)
    }
    
    private fun saveTags(tags: List<String>) {
        val dir = tagsFile.parent
        if (dir != null) {
            fileSystem.createDirectories(dir)
        }
        val content = json.encodeToString(TagList(tags))
        fileSystem.writeText(tagsFile, content)
    }
}
