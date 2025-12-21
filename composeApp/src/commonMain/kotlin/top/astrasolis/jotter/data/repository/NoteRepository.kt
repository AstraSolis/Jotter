package top.astrasolis.jotter.data.repository

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okio.Path
import top.astrasolis.jotter.data.model.Note
import top.astrasolis.jotter.data.model.NoteIndex
import top.astrasolis.jotter.data.model.NoteMetadata
import top.astrasolis.jotter.platform.PlatformFileSystem
import top.astrasolis.jotter.utils.TimeUtils

/**
 * 笔记仓库
 * 笔记内容存储为 Markdown 文件，元数据存储在 JSON 索引中
 */
class NoteRepository(
    private val fileSystem: PlatformFileSystem,
    private val getDataPath: () -> Path,
) {
    private val json = Json { 
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
    
    private val notesDir: Path
        get() = getDataPath() / "notes"
    
    private val indexFile: Path
        get() = notesDir / ".metadata.json"
    
    /**
     * 获取笔记文件路径
     */
    private fun notePath(id: String): Path = notesDir / "$id.md"
    
    /**
     * 获取所有笔记（元数据）
     */
    fun getAllNotes(): List<NoteMetadata> {
        return loadIndex().notes
    }
    
    /**
     * 获取单个笔记
     */
    fun getNote(id: String): Note? {
        val index = loadIndex()
        val metadata = index.notes.find { it.id == id } ?: return null
        
        val path = notePath(id)
        val content = fileSystem.readText(path) ?: return null
        
        return Note(
            id = metadata.id,
            title = metadata.title,
            content = content,
            tags = metadata.tags,
            createdAt = metadata.createdAt,
            updatedAt = metadata.updatedAt,
        )
    }
    
    /**
     * 保存笔记
     */
    fun saveNote(note: Note) {
        // 保存内容
        fileSystem.createDirectories(notesDir)
        fileSystem.writeText(notePath(note.id), note.content)
        
        // 更新索引
        val currentTime = TimeUtils.now()
        val index = loadIndex()
        val existingIndex = index.notes.indexOfFirst { it.id == note.id }
        
        val metadata = NoteMetadata(
            id = note.id,
            title = note.title,
            tags = note.tags,
            createdAt = if (existingIndex >= 0) index.notes[existingIndex].createdAt else currentTime,
            updatedAt = currentTime,
        )
        
        val updatedNotes = if (existingIndex >= 0) {
            index.notes.toMutableList().apply { set(existingIndex, metadata) }
        } else {
            index.notes + metadata
        }
        
        saveIndex(NoteIndex(updatedNotes))
    }
    
    /**
     * 删除笔记
     */
    fun deleteNote(id: String): Boolean {
        // 删除内容
        val deleted = fileSystem.delete(notePath(id))
        
        // 更新索引
        val index = loadIndex()
        val updatedNotes = index.notes.filter { it.id != id }
        saveIndex(NoteIndex(updatedNotes))
        
        return deleted
    }
    
    /**
     * 按标签搜索笔记
     */
    fun getNotesByTag(tag: String): List<NoteMetadata> {
        return loadIndex().notes.filter { it.tags.contains(tag) }
    }
    
    /**
     * 搜索笔记
     */
    fun searchNotes(query: String): List<NoteMetadata> {
        val index = loadIndex()
        return index.notes.filter { metadata ->
            metadata.title.contains(query, ignoreCase = true) ||
            metadata.tags.any { it.contains(query, ignoreCase = true) } ||
            // 搜索内容
            run {
                val content = fileSystem.readText(notePath(metadata.id)) ?: ""
                content.contains(query, ignoreCase = true)
            }
        }
    }
    
    /**
     * 获取所有标签
     */
    fun getAllTags(): List<String> {
        return loadIndex().notes
            .flatMap { it.tags }
            .distinct()
            .sorted()
    }
    
    /**
     * 创建新笔记
     */
    fun createNote(title: String, content: String, tags: List<String> = emptyList()): Note {
        val currentTime = TimeUtils.now()
        val id = generateNoteId()
        
        val note = Note(
            id = id,
            title = title,
            content = content,
            tags = tags,
            createdAt = currentTime,
            updatedAt = currentTime,
        )
        
        saveNote(note)
        return note
    }
    
    private fun generateNoteId(): String {
        // 使用时间戳 + 随机数生成简单的 ID
        return "${TimeUtils.now()}-${(0..999999).random()}"
    }
    
    private fun loadIndex(): NoteIndex {
        val content = fileSystem.readText(indexFile) ?: return NoteIndex()
        return try {
            json.decodeFromString<NoteIndex>(content)
        } catch (e: Exception) {
            NoteIndex()
        }
    }
    
    private fun saveIndex(index: NoteIndex) {
        fileSystem.createDirectories(notesDir)
        val content = json.encodeToString(index)
        fileSystem.writeText(indexFile, content)
    }
}
