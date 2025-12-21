package top.astrasolis.jotter.data

import okio.Path
import top.astrasolis.jotter.platform.PlatformFileSystem

/**
 * 数据目录管理器
 * 负责初始化和管理数据存储目录结构
 */
class DataDirectoryManager(
    private val fileSystem: PlatformFileSystem,
) {
    /**
     * 初始化数据目录结构
     * 创建必要的子目录
     */
    fun initializeDataDirectory(dataPath: Path) {
        // 创建主要目录
        val directories = listOf(
            dataPath / "config",
            dataPath / "journals",
            dataPath / "notes",
            dataPath / "todos",
            dataPath / "todos" / "archive",
        )
        
        directories.forEach { dir ->
            fileSystem.createDirectories(dir)
        }
    }
    
    /**
     * 验证数据目录是否有效
     * 检查目录是否存在且可写
     */
    fun isValidDataDirectory(path: Path): Boolean {
        if (!fileSystem.exists(path)) {
            return false
        }
        
        if (!fileSystem.isDirectory(path)) {
            return false
        }
        
        // 尝试创建测试文件来检查写权限
        return try {
            val testFile = path / ".jotter_test"
            fileSystem.writeText(testFile, "test")
            fileSystem.delete(testFile)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * 迁移数据到新目录
     * @param oldPath 旧数据目录
     * @param newPath 新数据目录
     * @return 是否迁移成功
     */
    fun migrateData(oldPath: Path, newPath: Path): Boolean {
        if (oldPath == newPath) return true
        
        return try {
            // 初始化新目录
            initializeDataDirectory(newPath)
            
            // 复制数据
            val dataDirs = listOf("journals", "notes", "todos")
            dataDirs.forEach { dirName ->
                val sourceDir = oldPath / dirName
                val destDir = newPath / dirName
                
                if (fileSystem.exists(sourceDir)) {
                    fileSystem.copyDirectory(sourceDir, destDir)
                }
            }
            
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * 获取数据目录统计信息
     */
    fun getDataStats(dataPath: Path): DataStats {
        var journalCount = 0
        var noteCount = 0
        var todoCount = 0
        
        // 统计日记数量
        val journalsDir = dataPath / "journals"
        if (fileSystem.exists(journalsDir)) {
            fileSystem.list(journalsDir).forEach { yearDir ->
                if (fileSystem.isDirectory(yearDir)) {
                    fileSystem.list(yearDir).forEach { monthDir ->
                        if (fileSystem.isDirectory(monthDir)) {
                            journalCount += fileSystem.list(monthDir)
                                .count { it.name.endsWith(".md") }
                        }
                    }
                }
            }
        }
        
        // 统计笔记数量
        val notesDir = dataPath / "notes"
        if (fileSystem.exists(notesDir)) {
            noteCount = fileSystem.list(notesDir)
                .count { it.name.endsWith(".md") }
        }
        
        // TODO: 统计待办数量需要读取 JSON 文件
        
        return DataStats(
            journalCount = journalCount,
            noteCount = noteCount,
            todoCount = todoCount,
        )
    }
}

/**
 * 数据统计信息
 */
data class DataStats(
    val journalCount: Int,
    val noteCount: Int,
    val todoCount: Int,
)
