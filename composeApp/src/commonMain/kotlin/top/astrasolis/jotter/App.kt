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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.astrasolis.jotter.ui.navigation.NavigationRoute
import top.astrasolis.jotter.ui.screens.HomeScreen
import top.astrasolis.jotter.ui.screens.JournalScreen
import top.astrasolis.jotter.ui.screens.NotesScreen
import top.astrasolis.jotter.ui.screens.SettingsScreen
import top.astrasolis.jotter.ui.screens.TodoScreen
import top.astrasolis.jotter.ui.theme.JotterTheme
import top.yukonga.miuix.kmp.basic.FloatingNavigationBar
import top.yukonga.miuix.kmp.basic.FloatingNavigationBarMode
import top.yukonga.miuix.kmp.basic.NavigationItem
import top.yukonga.miuix.kmp.basic.Scaffold

/**
 * Jotter 应用主入口
 * 使用 FloatingNavigationBar 进行导航
 */
@Composable
fun App() {
    JotterTheme {
        // 导航项列表
        val navigationItems = remember {
            listOf(
                NavigationItem("首页", Icons.Default.Home),
                NavigationItem("日记", Icons.Default.DateRange),
                NavigationItem("待办", Icons.Default.CheckCircle),
                NavigationItem("笔记", Icons.Outlined.Edit),
                NavigationItem("设置", Icons.Default.Settings),
            )
        }
        
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
                    FloatingNavigationBar(
                        items = navigationItems,
                        selected = selectedIndex,
                        onClick = { index -> selectedIndex = index },
                        mode = FloatingNavigationBarMode.IconOnly,
                        horizontalOutSidePadding = horizontalPadding,
                    )
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
                    )
                }
            }
        }
    }
}



