package top.astrasolis.jotter.i18n

/**
 * UI 字符串资源接口
 * 定义应用中所有需要国际化的文本
 */
interface Strings {
    // ==================== 导航 ====================
    val navHome: String
    val navJournal: String
    val navTodo: String
    val navNotes: String
    val navSettings: String
    
    // ==================== 首页 ====================
    val homeQuoteDefault: String
    val homeQuoteSource: String
    val homeTodayTodo: String
    val homeTodayTodoEmpty: String
    val homeViewAll: String
    
    // ==================== 待办 ====================
    val todoTitle: String
    val todoAdd: String
    val todoNewItem: String
    val todoPending: String
    val todoCompleted: String
    val todoEmpty: String
    val todoEmptyHint: String
    val todoCategoryWork: String
    val todoCategoryLife: String
    val todoCategoryHealth: String
    val todoCategoryStudy: String
    
    // ==================== 日记 ====================
    val journalTitle: String
    val journalAdd: String
    val journalEmpty: String
    val journalEmptyHint: String
    
    // ==================== 笔记 ====================
    val notesTitle: String
    val notesAdd: String
    val notesEmpty: String
    val notesEmptyHint: String
    
    // ==================== 设置 ====================
    val settingsTitle: String
    // 界面设置
    val settingsUiGroup: String
    val settingsAppearance: String
    val settingsTheme: String
    val settingsThemeMode: String
    val settingsThemeLight: String
    val settingsThemeDark: String
    val settingsThemeSystem: String
    val settingsLanguage: String
    // 数据管理
    val settingsDataGroup: String
    val settingsStorage: String
    val settingsDataDir: String
    val settingsBackupRestore: String
    val settingsExportData: String
    val settingsExportDataSummary: String
    val settingsImportData: String
    val settingsImportDataSummary: String
    // 更多
    val settingsMoreGroup: String
    val settingsAbout: String
    val settingsDeveloperTools: String
    
    // ==================== 关于页面 ====================
    val aboutTitle: String
    val aboutAppName: String
    val aboutDescription: String
    val aboutProjectLinks: String
    val aboutGitHub: String
    val aboutDevModeActivated: String
    val aboutDevModeHint: String  // "再点 %d 次激活开发者模式"
    
    // ==================== 开发者工具 ====================
    val devToolsTitle: String
    val devToolsDebug: String
    val devToolsShowSetup: String
    val devToolsShowSetupSummary: String
    val devToolsResetSettings: String
    val devToolsResetSettingsSummary: String
    val devToolsResetSettingsDone: String
    val devToolsStorageInfo: String
    val devToolsDataDir: String
    val devToolsJournalCount: String
    val devToolsNoteCount: String
    val devToolsTodoCount: String
    val devToolsVersionInfo: String
    val devToolsAppVersion: String
    val devToolsBuildType: String
    val devToolsPlatform: String
    val devToolsDevMode: String
    val devToolsDisableDevMode: String
    val devToolsDisableDevModeSummary: String
    val devToolsDevModeDisabled: String
    
    // ==================== 引导页 ====================
    val setupWelcome: String
    val setupWelcomeSubtitle: String
    val setupFeatureJournal: String
    val setupFeatureJournalDesc: String
    val setupFeatureTodo: String
    val setupFeatureTodoDesc: String
    val setupFeatureNotes: String
    val setupFeatureNotesDesc: String
    val setupStart: String
    val setupSelectStorage: String
    val setupStorageHint: String
    val setupStorageLocation: String
    val setupNotSelected: String
    val setupSelectDir: String
    val setupUseDefault: String
    val setupDirNotSupported: String
    val setupBack: String
    val setupNext: String
    val setupConfirm: String
    val setupDataSaveTo: String
    val setupDirStructure: String
    val setupDirJournals: String
    val setupDirNotes: String
    val setupDirTodos: String
    val setupDirConfig: String
    val setupComplete: String
    val setupSelectDirError: String
    val setupInitError: String
    
    // ==================== 通用 ====================
    val back: String
    val cancel: String
    val confirm: String
    val delete: String
    val edit: String
    val save: String
    val loading: String
    val add: String
    val untitled: String
    
    // 对话框
    val dialogDeleteTitle: String
    val dialogDeleteConfirm: String
    /** "确定要删除「{name}」吗？此操作无法撤销。" */
    fun dialogDeleteMessage(name: String): String

    // ==================== 日期格式 ====================
    /** 格式化带参数的字符串 */
    fun format(template: String, vararg args: Any): String = 
        template.format(*args)
    
    /** "待完成 (n)" */
    fun todoPendingCount(count: Int): String
    
    /** "已完成 (n)" */
    fun todoCompletedCount(count: Int): String
    
    /** "n 篇" */
    fun countPieces(count: Int): String
    
    /** "n 条" */
    fun countItems(count: Int): String
    
    /** "再点 n 次激活开发者模式" */
    fun devModeClicksRemaining(count: Int): String
    
    // ==================== 日期格式化 ====================
    /** 格式化日期，如 "2025年12月22日" 或 "December 22, 2025" */
    fun formatDate(year: Int, month: Int, day: Int): String
    
    /** 获取星期几文本 */
    fun getDayOfWeek(dayOfWeek: Int): String
}
