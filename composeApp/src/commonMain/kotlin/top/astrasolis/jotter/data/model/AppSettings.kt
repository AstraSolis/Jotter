package top.astrasolis.jotter.data.model

import kotlinx.serialization.Serializable

/**
 * 应用设置
 * 存储用户偏好配置
 */
@Serializable
data class AppSettings(
    /** 主题模式 */
    val theme: Theme = Theme.SYSTEM,
    /** 语言 */
    val language: String = "zh-CN",
    /** 是否显示已完成的待办 */
    val showCompletedTodos: Boolean = true,
    /** 默认待办分类 */
    val defaultTodoCategory: String? = null,
    /** 每日提醒时间 (HH:mm 格式) */
    val dailyReminderTime: String? = null,
) {
    @Serializable
    enum class Theme {
        LIGHT,
        DARK,
        SYSTEM,
    }
}

/**
 * 应用状态
 * 存储应用运行时状态，与用户设置分离
 */
@Serializable
data class AppState(
    /** 用户选择的数据存储目录 */
    val dataPath: String,
    /** 上次打开的路由 */
    val lastOpenedRoute: String? = null,
    /** 是否首次启动 */
    val isFirstLaunch: Boolean = true,
    /** 应用版本 (用于数据迁移) */
    val appVersion: String = "1.0.0",
    /** 是否启用开发者模式 */
    val isDeveloperModeEnabled: Boolean = false,
)
