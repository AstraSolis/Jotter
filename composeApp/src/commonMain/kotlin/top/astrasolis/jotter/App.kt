package top.astrasolis.jotter

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.astrasolis.jotter.data.AppContainer
import top.astrasolis.jotter.i18n.Language
import top.astrasolis.jotter.i18n.StringsProvider
import top.astrasolis.jotter.i18n.strings
import top.astrasolis.jotter.ui.navigation.NavigationRoute
import top.astrasolis.jotter.ui.screens.HomeScreen
import top.astrasolis.jotter.ui.screens.JournalScreen
import top.astrasolis.jotter.ui.screens.NotesScreen
import top.astrasolis.jotter.ui.screens.SettingsScreen
import top.astrasolis.jotter.ui.screens.SetupScreen
import top.astrasolis.jotter.ui.screens.TodoScreen
import top.astrasolis.jotter.ui.theme.JotterTheme
import top.yukonga.miuix.kmp.basic.FloatingNavigationBar
import top.yukonga.miuix.kmp.basic.FloatingNavigationBarMode
import top.yukonga.miuix.kmp.basic.NavigationItem
import top.yukonga.miuix.kmp.basic.Scaffold

/**
 * 应用状态
 */
private enum class AppState {
    LOADING,      // 加载中
    SETUP,        // 首次启动设置
    MAIN,         // 主界面
}

/**
 * Jotter 应用主入口
 * 使用 FloatingNavigationBar 进行导航
 */
@Composable
fun App() {
    // 应用语言状态
    var currentLanguage by remember { 
        mutableStateOf(AppContainer.settingsRepository.getLanguage()) 
    }
    
    StringsProvider(language = currentLanguage) {
        JotterTheme {
            var appState by remember { mutableStateOf(AppState.LOADING) }
            
            // 检查应用状态
            LaunchedEffect(Unit) {
                appState = if (AppContainer.isFirstLaunch()) {
                    AppState.SETUP
                } else {
                    // 初始化数据目录
                    AppContainer.initialize()
                    AppState.MAIN
                }
            }
            
            when (appState) {
                AppState.LOADING -> {
                    // 加载中，可以显示 splash screen
                    // 目前留空，因为检查很快
                }
                
                AppState.SETUP -> {
                    SetupScreen(
                        onSetupComplete = {
                            appState = AppState.MAIN
                        }
                    )
                }
                
                AppState.MAIN -> {
                    MainContent(
                        onShowSetupScreen = {
                            appState = AppState.SETUP
                        },
                        onLanguageChange = { language ->
                            currentLanguage = language
                        },
                    )
                }
            }
        }
    }
}

/**
 * 主内容区域
 */
@Composable
private fun MainContent(
    onShowSetupScreen: () -> Unit = {},
    onLanguageChange: (Language) -> Unit = {},
) {
    // 导航项列表 - 使用国际化字符串
    val navigationItems = listOf(
        NavigationItem(strings.navHome, Icons.Default.Home),
        NavigationItem(strings.navJournal, Icons.Default.DateRange),
        NavigationItem(strings.navTodo, Icons.Default.CheckCircle),
        NavigationItem(strings.navNotes, Icons.Outlined.Edit),
        NavigationItem(strings.navSettings, Icons.Default.Settings),
    )
    
    // 路由映射
    val routes = remember {
        listOf(
            NavigationRoute.HOME,
            NavigationRoute.JOURNAL,
            NavigationRoute.TODO,
            NavigationRoute.NOTES,
            NavigationRoute.SETTINGS,
        )
    }
    
    var selectedIndex by remember { mutableIntStateOf(0) }
    
    // 是否显示底部导航栏（设置二级菜单时隐藏）
    var showBottomBar by remember { mutableStateOf(true) }
    
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        // 根据窗口宽度计算外边距：小屏幕边距小使图标更紧凑
        val horizontalPadding = when {
            maxWidth < 400.dp -> 8.dp   // 小屏幕更紧凑
            maxWidth < 600.dp -> 16.dp
            maxWidth < 800.dp -> 48.dp
            else -> 80.dp
        }
        
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                if (showBottomBar) {
                    FloatingNavigationBar(
                        items = navigationItems,
                        selected = selectedIndex,
                        onClick = { index -> selectedIndex = index },
                        mode = FloatingNavigationBarMode.IconOnly,
                        horizontalOutSidePadding = horizontalPadding,
                    )
                }
            },
        ) { innerPadding ->
            // 根据当前选中索引显示对应页面
            when (routes[selectedIndex]) {
                NavigationRoute.HOME -> HomeScreen(
                    innerPadding = innerPadding,
                    onNavigate = { route -> 
                        selectedIndex = routes.indexOf(route)
                    },
                )
                NavigationRoute.JOURNAL -> JournalScreen(
                    innerPadding = innerPadding,
                )
                NavigationRoute.TODO -> TodoScreen(
                    innerPadding = innerPadding,
                )
                NavigationRoute.NOTES -> NotesScreen(
                    innerPadding = innerPadding,
                )
                NavigationRoute.SETTINGS -> SettingsScreen(
                    innerPadding = innerPadding,
                    onSubPageChange = { inSubPage ->
                        showBottomBar = !inSubPage
                    },
                    onShowSetupScreen = onShowSetupScreen,
                    onLanguageChange = onLanguageChange,
                )
            }
        }
    }
}

