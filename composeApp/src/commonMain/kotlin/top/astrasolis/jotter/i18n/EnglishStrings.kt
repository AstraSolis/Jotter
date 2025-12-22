package top.astrasolis.jotter.i18n

/**
 * 英文字符串资源
 */
object EnglishStrings : Strings {
    // ==================== Navigation ====================
    override val navHome = "Home"
    override val navJournal = "Journal"
    override val navTodo = "Todo"
    override val navNotes = "Notes"
    override val navSettings = "Settings"
    
    // ==================== Home ====================
    override val homeQuoteDefault = "Every day not spent dancing\nis a betrayal of life."
    override val homeQuoteSource = "Nietzsche"
    override val homeTodayTodo = "Today's Todo"
    override val homeTodayTodoEmpty = "No todos for today"
    override val homeViewAll = "View All"
    
    // ==================== Todo ====================
    override val todoTitle = "Todo"
    override val todoAdd = "Add Todo"
    override val todoNewItem = "New Todo"
    override val todoPending = "Pending"
    override val todoCompleted = "Completed"
    override val todoEmpty = "No todos yet"
    override val todoEmptyHint = "Tap the button below to add a new task"
    override val todoCategoryWork = "Work"
    override val todoCategoryLife = "Life"
    override val todoCategoryHealth = "Health"
    override val todoCategoryStudy = "Study"
    
    // ==================== Journal ====================
    override val journalTitle = "Journal"
    override val journalAdd = "Add Entry"
    override val journalEmpty = "No journal entries yet"
    override val journalEmptyHint = "Tap the button below to record today"
    
    // ==================== Notes ====================
    override val notesTitle = "Notes"
    override val notesAdd = "Add Note"
    override val notesEmpty = "No notes yet"
    override val notesEmptyHint = "Tap the button below to capture your ideas"
    
    // ==================== Settings ====================
    override val settingsTitle = "Settings"
    override val settingsUiGroup = "Interface"
    override val settingsAppearance = "Appearance"
    override val settingsTheme = "Theme"
    override val settingsThemeMode = "Theme Mode"
    override val settingsThemeLight = "Light"
    override val settingsThemeDark = "Dark"
    override val settingsThemeSystem = "System"
    override val settingsLanguage = "Language"
    override val settingsDataGroup = "Data"
    override val settingsStorage = "Storage"
    override val settingsDataDir = "Data Directory"
    override val settingsBackupRestore = "Backup & Restore"
    override val settingsExportData = "Export Data"
    override val settingsExportDataSummary = "Export data as archive"
    override val settingsImportData = "Import Data"
    override val settingsImportDataSummary = "Restore data from archive"
    override val settingsMoreGroup = "More"
    override val settingsAbout = "About"
    override val settingsDeveloperTools = "Developer Tools"
    
    // ==================== About ====================
    override val aboutTitle = "About"
    override val aboutAppName = "Jotter"
    override val aboutDescription = "A simple and elegant cross-platform note-taking app for journals, todos, and notes."
    override val aboutProjectLinks = "Project Links"
    override val aboutGitHub = "GitHub"
    override val aboutDevModeActivated = "Developer mode activated"
    override val aboutDevModeHint = "Tap %d more times to activate developer mode"
    
    // ==================== Developer Tools ====================
    override val devToolsTitle = "Developer Tools"
    override val devToolsDebug = "Debug"
    override val devToolsShowSetup = "Show Setup Screen"
    override val devToolsShowSetupSummary = "Show the setup wizard"
    override val devToolsResetSettings = "Reset All Settings"
    override val devToolsResetSettingsSummary = "Restore settings to defaults"
    override val devToolsResetSettingsDone = "All settings have been reset"
    override val devToolsStorageInfo = "Storage Info"
    override val devToolsDataDir = "Data Directory"
    override val devToolsJournalCount = "Journals"
    override val devToolsNoteCount = "Notes"
    override val devToolsTodoCount = "Todos"
    override val devToolsVersionInfo = "Version Info"
    override val devToolsAppVersion = "App Version"
    override val devToolsBuildType = "Build Type"
    override val devToolsPlatform = "Platform"
    override val devToolsDevMode = "Developer Mode"
    override val devToolsDisableDevMode = "Disable Developer Mode"
    override val devToolsDisableDevModeSummary = "Hide developer tools"
    override val devToolsDevModeDisabled = "Developer mode disabled"
    
    // ==================== Setup ====================
    override val setupWelcome = "Welcome to Jotter"
    override val setupWelcomeSubtitle = "A simple app for journals, todos, and notes"
    override val setupFeatureJournal = "Journal"
    override val setupFeatureJournalDesc = "Record your daily thoughts"
    override val setupFeatureTodo = "Todo"
    override val setupFeatureTodoDesc = "Manage tasks and goals"
    override val setupFeatureNotes = "Notes"
    override val setupFeatureNotesDesc = "Capture ideas anytime"
    override val setupStart = "Get Started"
    override val setupSelectStorage = "Select Data Storage Location"
    override val setupStorageHint = "Your journals, notes, and todos will be saved here"
    override val setupStorageLocation = "Storage Location"
    override val setupNotSelected = "Not Selected"
    override val setupSelectDir = "Select Directory"
    override val setupUseDefault = "Use Default"
    override val setupDirNotSupported = "Custom directory selection is not supported on this platform"
    override val setupBack = "Back"
    override val setupNext = "Next"
    override val setupConfirm = "Confirm Setup"
    override val setupDataSaveTo = "Data will be saved to"
    override val setupDirStructure = "The following directories will be created:"
    override val setupDirJournals = "Journal files (Markdown)"
    override val setupDirNotes = "Note files (Markdown)"
    override val setupDirTodos = "Todo items (JSON)"
    override val setupDirConfig = "Config files (JSON)"
    override val setupComplete = "Complete Setup"
    override val setupSelectDirError = "Error selecting directory"
    override val setupInitError = "Initialization failed"
    
    // ==================== Common ====================
    override val back = "Back"
    override val cancel = "Cancel"
    override val confirm = "Confirm"
    override val delete = "Delete"
    override val edit = "Edit"
    override val save = "Save"
    override val loading = "Loading..."
    override val add = "Add"
    override val untitled = "Untitled"
    
    // Dialog
    override val dialogDeleteTitle = "Confirm Delete"
    override val dialogDeleteConfirm = "Delete"
    override fun dialogDeleteMessage(name: String) = "Are you sure you want to delete \"$name\"? This cannot be undone."
    
    // ==================== Formatting ====================
    override fun todoPendingCount(count: Int) = "Pending ($count)"
    override fun todoCompletedCount(count: Int) = "Completed ($count)"
    override fun countPieces(count: Int) = "$count"
    override fun countItems(count: Int) = "$count"
    override fun devModeClicksRemaining(count: Int) = "Tap $count more times to activate developer mode"
    
    // ==================== Date Formatting ====================
    private val monthNames = arrayOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )
    
    override fun formatDate(year: Int, month: Int, day: Int): String {
        val monthName = monthNames.getOrElse(month - 1) { "Unknown" }
        return "$monthName $day, $year"
    }
    
    override fun getDayOfWeek(dayOfWeek: Int) = when (dayOfWeek) {
        1 -> "Monday"
        2 -> "Tuesday"
        3 -> "Wednesday"
        4 -> "Thursday"
        5 -> "Friday"
        6 -> "Saturday"
        7 -> "Sunday"
        else -> ""
    }
}
