package top.astrasolis.jotter.i18n

/**
 * 中文字符串资源
 */
object ChineseStrings : Strings {
    // ==================== 导航 ====================
    override val navHome = "首页"
    override val navJournal = "日记"
    override val navTodo = "待办"
    override val navNotes = "笔记"
    override val navSettings = "设置"
    
    // ==================== 首页 ====================
    override val homeQuoteDefault = "每一个不曾起舞的日子，\n都是对生命的辜负。"
    override val homeQuoteSource = "尼采"
    override val homeTodayTodo = "今日待办"
    override val homeTodayTodoEmpty = "今天没有待办事项"
    override val homeViewAll = "查看全部"
    
    // ==================== 待办 ====================
    override val todoTitle = "待办事项"
    override val todoAdd = "添加待办"
    override val todoNewItem = "新待办事项"
    override val todoPending = "待完成"
    override val todoCompleted = "已完成"
    override val todoEmpty = "还没有待办事项"
    override val todoEmptyHint = "点击右下角按钮添加新任务"
    override val todoCategoryWork = "工作"
    override val todoCategoryLife = "生活"
    override val todoCategoryHealth = "健康"
    override val todoCategoryStudy = "学习"
    // 编辑对话框
    override val todoAddTitle = "添加待办"
    override val todoEditTitle = "编辑待办"
    override val todoTitleLabel = "待办内容"
    override val todoDescLabel = "备注（可选）"
    override val todoReminderLabel = "提醒时间"
    override val todoNoReminder = "无提醒"
    override val todoSetReminder = "设置提醒"
    override val todoClearReminder = "清除提醒"
    // 显示
    override val todoCompletedAtPrefix = "完成于"
    override val todoReminderPrefix = "提醒"
    override val todoOverdue = "已逾期"
    
    // ==================== 日记 ====================
    override val journalTitle = "日记"
    override val journalAdd = "添加日记"
    override val journalEmpty = "还没有日记"
    override val journalEmptyHint = "点击右下角按钮记录今天"
    
    // ==================== 笔记 ====================
    override val notesTitle = "笔记"
    override val notesAdd = "添加笔记"
    override val notesEmpty = "还没有笔记"
    override val notesEmptyHint = "点击右下角按钮记录灵感"
    
    // ==================== 设置 ====================
    override val settingsTitle = "设置"
    override val settingsUiGroup = "界面设置"
    override val settingsAppearance = "外观"
    override val settingsTheme = "主题"
    override val settingsThemeMode = "主题模式"
    override val settingsThemeLight = "浅色模式"
    override val settingsThemeDark = "深色模式"
    override val settingsThemeSystem = "跟随系统"
    override val settingsLanguage = "语言"
    override val settingsDataGroup = "数据管理"
    override val settingsStorage = "存储"
    override val settingsDataDir = "数据目录"
    override val settingsBackupRestore = "备份与恢复"
    override val settingsExportData = "导出数据"
    override val settingsExportDataSummary = "将数据导出为压缩包"
    override val settingsImportData = "导入数据"
    override val settingsImportDataSummary = "从压缩包恢复数据"
    override val settingsMoreGroup = "更多"
    override val settingsAbout = "关于"
    override val settingsDeveloperTools = "开发者工具"
    
    // ==================== 关于页面 ====================
    override val aboutTitle = "关于"
    override val aboutAppName = "Jotter"
    override val aboutDescription = "一个简洁优雅的跨平台笔记应用，支持日记、待办和笔记管理。"
    override val aboutProjectLinks = "项目链接"
    override val aboutGitHub = "GitHub"
    override val aboutDevModeActivated = "开发者模式已激活"
    override val aboutDevModeHint = "再点 %d 次激活开发者模式"
    
    // ==================== 开发者工具 ====================
    override val devToolsTitle = "开发者工具"
    override val devToolsDebug = "调试功能"
    override val devToolsShowSetup = "显示首次启动引导页"
    override val devToolsShowSetupSummary = "立即显示引导页流程"
    override val devToolsResetSettings = "重置所有设置"
    override val devToolsResetSettingsSummary = "将设置恢复为默认值"
    override val devToolsResetSettingsDone = "已重置所有设置"
    override val devToolsStorageInfo = "存储信息"
    override val devToolsDataDir = "数据目录"
    override val devToolsJournalCount = "日记数量"
    override val devToolsNoteCount = "笔记数量"
    override val devToolsTodoCount = "待办数量"
    override val devToolsVersionInfo = "版本信息"
    override val devToolsAppVersion = "应用版本"
    override val devToolsBuildType = "构建类型"
    override val devToolsPlatform = "平台"
    override val devToolsDevMode = "开发者模式"
    override val devToolsDisableDevMode = "关闭开发者模式"
    override val devToolsDisableDevModeSummary = "隐藏开发者工具入口"
    override val devToolsDevModeDisabled = "开发者模式已关闭"
    
    // ==================== 引导页 ====================
    override val setupWelcome = "欢迎使用 Jotter"
    override val setupWelcomeSubtitle = "一个简洁的日记、待办和笔记应用"
    override val setupFeatureJournal = "日记"
    override val setupFeatureJournalDesc = "记录每一天的所思所想"
    override val setupFeatureTodo = "待办"
    override val setupFeatureTodoDesc = "管理日常任务和目标"
    override val setupFeatureNotes = "笔记"
    override val setupFeatureNotesDesc = "随时随地记录灵感"
    override val setupStart = "开始设置"
    override val setupSelectStorage = "选择数据存储位置"
    override val setupStorageHint = "您的日记、笔记和待办事项将保存在此位置"
    override val setupStorageLocation = "存储位置"
    override val setupNotSelected = "未选择"
    override val setupSelectDir = "选择目录"
    override val setupUseDefault = "使用默认位置"
    override val setupDirNotSupported = "当前平台不支持自定义目录选择"
    override val setupBack = "返回"
    override val setupNext = "下一步"
    override val setupConfirm = "确认设置"
    override val setupDataSaveTo = "数据将保存至"
    override val setupDirStructure = "将创建以下目录结构："
    override val setupDirJournals = "日记文件 (Markdown)"
    override val setupDirNotes = "笔记文件 (Markdown)"
    override val setupDirTodos = "待办事项 (JSON)"
    override val setupDirConfig = "配置文件 (JSON)"
    override val setupComplete = "完成设置"
    override val setupSelectDirError = "选择目录时发生错误"
    override val setupInitError = "初始化失败"
    
    // ==================== 通用 ====================
    override val back = "返回"
    override val cancel = "取消"
    override val confirm = "确定"
    override val delete = "删除"
    override val edit = "编辑"
    override val save = "保存"
    override val loading = "加载中..."
    override val add = "添加"
    override val untitled = "无标题"
    
    // 对话框
    override val dialogDeleteTitle = "删除确认"
    override val dialogDeleteConfirm = "删除"
    override fun dialogDeleteMessage(name: String) = "确定要删除「$name」吗？此操作无法撤销。"
    
    // ==================== 格式化方法 ====================
    override fun todoPendingCount(count: Int) = "待完成 ($count)"
    override fun todoCompletedCount(count: Int) = "已完成 ($count)"
    override fun countPieces(count: Int) = "$count 篇"
    override fun countItems(count: Int) = "$count 条"
    override fun devModeClicksRemaining(count: Int) = "再点 $count 次激活开发者模式"
    
    // ==================== 日期格式化 ====================
    override fun formatDate(year: Int, month: Int, day: Int) = "${year}年${month}月${day}日"
    
    override fun formatYearMonth(year: Int, month: Int) = "${year}年${month}月"
    
    override fun getDayOfWeek(dayOfWeek: Int) = when (dayOfWeek) {
        1 -> "星期一"
        2 -> "星期二"
        3 -> "星期三"
        4 -> "星期四"
        5 -> "星期五"
        6 -> "星期六"
        7 -> "星期日"
        else -> ""
    }
    
    override fun getDayOfWeekShort(dayOfWeek: Int) = when (dayOfWeek) {
        1 -> "一"
        2 -> "二"
        3 -> "三"
        4 -> "四"
        5 -> "五"
        6 -> "六"
        7 -> "日"
        else -> ""
    }
}
