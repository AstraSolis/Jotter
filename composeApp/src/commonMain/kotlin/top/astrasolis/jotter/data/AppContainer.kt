package top.astrasolis.jotter.data

import top.astrasolis.jotter.data.repository.JournalRepository
import top.astrasolis.jotter.data.repository.NoteRepository
import top.astrasolis.jotter.data.repository.SettingsRepository
import top.astrasolis.jotter.data.repository.TagRepository
import top.astrasolis.jotter.data.repository.TodoRepository
import top.astrasolis.jotter.platform.DirectoryPicker
import top.astrasolis.jotter.platform.PlatformFileSystem

/**
 * 应用级依赖容器
 * 提供各个组件的单例实例
 */
object AppContainer {
    // 延迟初始化的文件系统
    val fileSystem: PlatformFileSystem by lazy { PlatformFileSystem() }
    
    // 目录选择器
    val directoryPicker: DirectoryPicker by lazy { DirectoryPicker() }
    
    // 数据目录管理器
    val dataDirectoryManager: DataDirectoryManager by lazy { 
        DataDirectoryManager(fileSystem) 
    }
    
    // 设置仓库
    val settingsRepository: SettingsRepository by lazy { 
        SettingsRepository(fileSystem) 
    }
    
    // 待办事项仓库
    val todoRepository: TodoRepository by lazy { 
        TodoRepository(fileSystem) { settingsRepository.getDataPath() }
    }
    
    // 日记仓库
    val journalRepository: JournalRepository by lazy { 
        JournalRepository(fileSystem) { settingsRepository.getDataPath() }
    }
    
    // 笔记仓库
    val noteRepository: NoteRepository by lazy { 
        NoteRepository(fileSystem) { settingsRepository.getDataPath() }
    }
    
    // 标签仓库
    val tagRepository: TagRepository by lazy { 
        TagRepository(fileSystem) { settingsRepository.getDataPath() }
    }
    
    /**
     * 初始化应用
     * 在应用启动时调用，确保数据目录结构存在
     */
    fun initialize() {
        val dataPath = settingsRepository.getDataPath()
        dataDirectoryManager.initializeDataDirectory(dataPath)
        
        // 归档已完成超过7天的待办
        todoRepository.archiveCompletedTodos()
    }
    
    /**
     * 检查是否首次启动
     */
    fun isFirstLaunch(): Boolean {
        return settingsRepository.isFirstLaunch()
    }
    
    /**
     * 检查数据路径是否有效
     */
    fun isDataPathValid(): Boolean {
        val dataPath = settingsRepository.getDataPath()
        return dataDirectoryManager.isValidDataDirectory(dataPath) ||
               !fileSystem.exists(dataPath) // 如果目录不存在，可以创建
    }
}
