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
}
