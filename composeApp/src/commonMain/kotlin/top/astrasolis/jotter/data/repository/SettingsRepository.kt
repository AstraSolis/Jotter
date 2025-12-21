package top.astrasolis.jotter.data.repository

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okio.Path
import top.astrasolis.jotter.data.model.AppSettings
import top.astrasolis.jotter.data.model.AppState
import top.astrasolis.jotter.platform.PlatformFileSystem
import top.astrasolis.jotter.platform.toPath

/**
 * 设置仓库
 * 管理应用设置和状态的持久化
 */
class SettingsRepository(
    private val fileSystem: PlatformFileSystem,
) {
    private val json = Json { 
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
    
    private val configDir: Path
        get() = fileSystem.getAppConfigDir()
    
    private val settingsFile: Path
        get() = configDir / "settings.json"
    
    private val appStateFile: Path
        get() = configDir / "app_state.json"
    
    /**
     * 加载应用设置
     * 如果文件不存在，返回默认设置
     */
    fun loadSettings(): AppSettings {
        val content = fileSystem.readText(settingsFile) ?: return AppSettings()
        return try {
            json.decodeFromString<AppSettings>(content)
        } catch (e: Exception) {
            // 解析失败，返回默认设置
            AppSettings()
        }
    }
    
    /**
     * 保存应用设置
     */
    fun saveSettings(settings: AppSettings) {
        val content = json.encodeToString(settings)
        fileSystem.writeText(settingsFile, content)
    }
    
    /**
     * 加载应用状态
     * 如果文件不存在，返回 null
     */
    fun loadAppState(): AppState? {
        val content = fileSystem.readText(appStateFile) ?: return null
        return try {
            json.decodeFromString<AppState>(content)
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * 保存应用状态
     */
    fun saveAppState(state: AppState) {
        val content = json.encodeToString(state)
        fileSystem.writeText(appStateFile, content)
    }
    
    /**
     * 检查是否首次启动
     */
    fun isFirstLaunch(): Boolean {
        val state = loadAppState() ?: return true
        return state.isFirstLaunch
    }
    
    /**
     * 获取数据存储路径
     * 如果未设置，返回默认路径
     */
    fun getDataPath(): Path {
        val state = loadAppState()
        return state?.dataPath?.toPath() ?: fileSystem.getDefaultDataDir()
    }
    
    /**
     * 更新数据存储路径
     */
    fun updateDataPath(newPath: Path) {
        val currentState = loadAppState() ?: AppState(
            dataPath = newPath.toString(),
            isFirstLaunch = false,
        )
        saveAppState(currentState.copy(
            dataPath = newPath.toString(),
            isFirstLaunch = false,
        ))
    }
    
    /**
     * 标记首次启动完成
     */
    fun markFirstLaunchComplete() {
        val currentState = loadAppState() ?: AppState(
            dataPath = fileSystem.getDefaultDataDir().toString(),
            isFirstLaunch = false,
        )
        saveAppState(currentState.copy(isFirstLaunch = false))
    }
    
    /**
     * 检查是否启用开发者模式
     */
    fun isDeveloperModeEnabled(): Boolean {
        val state = loadAppState() ?: return false
        return state.isDeveloperModeEnabled
    }
    
    /**
     * 设置开发者模式
     */
    fun setDeveloperModeEnabled(enabled: Boolean) {
        val currentState = loadAppState() ?: return
        saveAppState(currentState.copy(isDeveloperModeEnabled = enabled))
    }
    
    /**
     * 重置为首次启动状态（用于调试引导页）
     */
    fun resetToFirstLaunch() {
        val currentState = loadAppState() ?: return
        saveAppState(currentState.copy(isFirstLaunch = true))
    }
    
    /**
     * 重置所有设置为默认值
     */
    fun resetAllSettings() {
        saveSettings(AppSettings())
    }
    
    /**
     * 获取存储统计信息
     */
    fun getStorageInfo(): StorageInfo {
        val dataPath = getDataPath()
        val journalsDir = dataPath / "journals"
        val notesDir = dataPath / "notes"
        val todosDir = dataPath / "todos"
        
        return StorageInfo(
            dataPath = dataPath.toString(),
            journalCount = countFiles(journalsDir),
            noteCount = countFiles(notesDir),
            todoCount = countFiles(todosDir),
        )
    }
    
    private fun countFiles(path: Path): Int {
        return try {
            if (fileSystem.exists(path)) {
                fileSystem.list(path).size
            } else {
                0
            }
        } catch (e: Exception) {
            0
        }
    }
}

/**
 * 存储统计信息
 */
data class StorageInfo(
    val dataPath: String,
    val journalCount: Int,
    val noteCount: Int,
    val todoCount: Int,
)
