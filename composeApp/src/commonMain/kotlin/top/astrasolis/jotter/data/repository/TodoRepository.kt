@file:OptIn(kotlin.time.ExperimentalTime::class)
package top.astrasolis.jotter.data.repository

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okio.Path
import top.astrasolis.jotter.data.Todo
import top.astrasolis.jotter.data.TodoList
import top.astrasolis.jotter.platform.PlatformFileSystem
import top.astrasolis.jotter.utils.TimeUtils
import kotlin.time.Duration.Companion.days

/**
 * 待办事项仓库
 * 管理待办事项的 CRUD 操作
 */
class TodoRepository(
    private val fileSystem: PlatformFileSystem,
    private val getDataPath: () -> Path,
) {
    private val json = Json { 
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
    
    private val todosDir: Path
        get() = getDataPath() / "todos"
    
    private val activeFile: Path
        get() = todosDir / "active.json"
    
    private fun archiveFile(year: Int): Path =
        todosDir / "archive" / "$year.json"
    
    /**
     * 获取所有活跃待办
     */
    fun getActiveTodos(): List<Todo> {
        val content = fileSystem.readText(activeFile) ?: return emptyList()
        return try {
            json.decodeFromString<TodoList>(content).todos
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    /**
     * 获取指定日期的待办
     */
    fun getTodosByDate(date: LocalDate): List<Todo> {
        val tz = TimeZone.currentSystemDefault()
        return getActiveTodos().filter { todo ->
            todo.dueDateTime?.let { epochMs ->
                Instant.fromEpochMilliseconds(epochMs)
                    .toLocalDateTime(tz).date == date
            } ?: false
        }
    }
    
    /**
     * 获取今日待办
     * - 未完成的待办：无时间则始终显示，有时间则在当天或已逾期时显示
     * - 已完成的待办：按完成时间判断，只显示今天完成的
     */
    fun getTodayTodos(): List<Todo> {
        val today = TimeUtils.today()
        val tz = TimeZone.currentSystemDefault()
        return getActiveTodos().filter { todo ->
            if (todo.completed) {
                // 已完成：按完成时间判断
                todo.completedAt?.let { epochMs ->
                    Instant.fromEpochMilliseconds(epochMs)
                        .toLocalDateTime(tz).date == today
                } ?: false
            } else {
                // 未完成：无时间则始终显示，有时间则在当天或已逾期时显示
                todo.dueDateTime == null ||
                    Instant.fromEpochMilliseconds(todo.dueDateTime)
                        .toLocalDateTime(tz).date <= today
            }
        }
    }
    
    /**
     * 保存单个待办
     * 如果存在则更新，不存在则添加
     */
    fun saveTodo(todo: Todo) {
        val todos = getActiveTodos().toMutableList()
        val existingIndex = todos.indexOfFirst { it.id == todo.id }
        
        if (existingIndex >= 0) {
            todos[existingIndex] = todo.copy(updatedAt = TimeUtils.now())
        } else {
            todos.add(todo)
        }
        
        saveActiveTodos(todos)
    }
    
    /**
     * 删除待办
     */
    fun deleteTodo(id: String) {
        val todos = getActiveTodos().filter { it.id != id }
        saveActiveTodos(todos)
    }
    
    /**
     * 完成待办
     */
    fun completeTodo(id: String) {
        val todos = getActiveTodos().toMutableList()
        val index = todos.indexOfFirst { it.id == id }
        
        if (index >= 0) {
            val currentTime = TimeUtils.now()
            todos[index] = todos[index].copy(
                completed = true,
                completedAt = currentTime,
                updatedAt = currentTime,
            )
            saveActiveTodos(todos)
        }
    }
    
    /**
     * 取消完成待办
     */
    fun uncompleteTodo(id: String) {
        val todos = getActiveTodos().toMutableList()
        val index = todos.indexOfFirst { it.id == id }
        
        if (index >= 0) {
            todos[index] = todos[index].copy(
                completed = false,
                completedAt = null,
                updatedAt = TimeUtils.now(),
            )
            saveActiveTodos(todos)
        }
    }
    
    /**
     * 归档已完成的待办
     * 将已完成超过指定天数的待办移至归档文件
     */
    fun archiveCompletedTodos(daysOld: Int = 7) {
        val currentTimeMs = TimeUtils.now()
        val cutoffTime = currentTimeMs - (daysOld * 24 * 60 * 60 * 1000L)
        
        val todos = getActiveTodos()
        val (toArchive, toKeep) = todos.partition { todo ->
            todo.completed && todo.completedAt != null && todo.completedAt < cutoffTime
        }
        
        if (toArchive.isEmpty()) return
        
        // 按年份分组归档
        val tz = TimeZone.currentSystemDefault()
        val currentYear = TimeUtils.currentYear()
        
        val byYear = toArchive.groupBy { todo ->
            todo.completedAt?.let { epochMs ->
                Instant.fromEpochMilliseconds(epochMs)
                    .toLocalDateTime(tz).year
            } ?: currentYear
        }
        
        byYear.forEach { (year, yearTodos) ->
            val archivePath = archiveFile(year)
            val existing = loadArchivedTodos(year)
            val merged = (existing + yearTodos).distinctBy { it.id }
            saveArchivedTodos(year, merged)
        }
        
        // 保存剩余的活跃待办
        saveActiveTodos(toKeep)
    }
    
    /**
     * 获取归档的待办
     */
    fun getArchivedTodos(year: Int): List<Todo> {
        return loadArchivedTodos(year)
    }
    
    private fun saveActiveTodos(todos: List<Todo>) {
        fileSystem.createDirectories(todosDir)
        val content = json.encodeToString(TodoList(todos))
        fileSystem.writeText(activeFile, content)
    }
    
    private fun loadArchivedTodos(year: Int): List<Todo> {
        val path = archiveFile(year)
        val content = fileSystem.readText(path) ?: return emptyList()
        return try {
            json.decodeFromString<TodoList>(content).todos
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    private fun saveArchivedTodos(year: Int, todos: List<Todo>) {
        val path = archiveFile(year)
        fileSystem.createDirectories(path.parent!!)
        val content = json.encodeToString(TodoList(todos))
        fileSystem.writeText(path, content)
    }
}
