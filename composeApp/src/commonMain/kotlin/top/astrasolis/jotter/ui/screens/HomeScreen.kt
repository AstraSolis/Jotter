package top.astrasolis.jotter.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.astrasolis.jotter.ui.components.DateDisplayWidget
import top.astrasolis.jotter.ui.components.QuoteDisplayWidget
import top.astrasolis.jotter.ui.components.TodayTodoCard
import top.astrasolis.jotter.data.Todo
import top.astrasolis.jotter.ui.navigation.NavigationRoute
import top.astrasolis.jotter.ui.theme.AppTheme
import top.astrasolis.jotter.utils.DateUtils
import top.astrasolis.jotter.utils.TimeUtils
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

// 响应式布局断点
private val WIDE_LAYOUT_BREAKPOINT = 800.dp
// 左右留白
private val HORIZONTAL_PADDING = 48.dp

/**
 * 仪表盘首页
 * 布局：
 * - 左上角：日期
 * - 中央：一言（大字居中）
 * - 底部：今日待办卡片（窄屏）
 * - 右侧：今日待办卡片（宽屏）
 */
@OptIn(ExperimentalUuidApi::class)
@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    onNavigate: (NavigationRoute) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    // 动态获取当前日期
    val today = remember { DateUtils.today() }
    val dateText = remember(today) { DateUtils.formatDateChinese(today) }
    val dayOfWeekText = remember(today) { DateUtils.getDayOfWeekChinese(today) }
    
    // 一言
    val quote = remember { "每一个不曾起舞的日子，\n都是对生命的辜负。" }
    val quoteSource = remember { "尼采" }
    
    // 今日待办
    val currentTime = remember { TimeUtils.now() }
    var todayTodos by remember {
        mutableStateOf(
            listOf(
                Todo(id = Uuid.random().toString(), title = "完成项目文档", completed = false, createdAt = currentTime, updatedAt = currentTime),
                Todo(id = Uuid.random().toString(), title = "购买日用品", completed = false, createdAt = currentTime, updatedAt = currentTime),
                Todo(id = Uuid.random().toString(), title = "锻炼30分钟", completed = true, createdAt = currentTime, updatedAt = currentTime),
                Todo(id = Uuid.random().toString(), title = "阅读技术书籍", completed = false, createdAt = currentTime, updatedAt = currentTime),
                Todo(id = Uuid.random().toString(), title = "整理房间", completed = false, createdAt = currentTime, updatedAt = currentTime),
            )
        )
    }
    
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        val isWideLayout = maxWidth >= WIDE_LAYOUT_BREAKPOINT
        
        if (isWideLayout) {
            // 宽屏布局：左侧主内容 + 右侧今日待办
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = HORIZONTAL_PADDING, vertical = AppTheme.spacing.xxl),
            ) {
                // 左侧主内容区
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                ) {
                    MainContentWide(
                        dateText = dateText,
                        dayOfWeekText = dayOfWeekText,
                        quote = quote,
                        quoteSource = quoteSource,
                    )
                }
                
                Spacer(modifier = Modifier.width(AppTheme.spacing.xxl))
                
                // 右侧今日待办 - 居中显示
                Box(
                    modifier = Modifier
                        .width(300.dp)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center,
                ) {
                    TodayTodoCard(
                        todos = todayTodos,
                        onTodoClick = { onNavigate(NavigationRoute.TODO) },
                        onToggle = { id ->
                            todayTodos = todayTodos.map {
                                if (it.id == id) it.copy(completed = !it.completed) else it
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        } else {
            // 窄屏/移动端布局：使用 Column + verticalScroll
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = AppTheme.spacing.lg),
            ) {
                // 左上角日期
                DateDisplayWidget(
                    date = dateText,
                    dayOfWeek = dayOfWeekText,
                )
                
                Spacer(modifier = Modifier.height(48.dp))
                
                // 中央一言
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    QuoteDisplayWidget(
                        quote = quote,
                        quoteSource = quoteSource,
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // 今日待办卡片
                TodayTodoCard(
                    todos = todayTodos,
                    onTodoClick = { onNavigate(NavigationRoute.TODO) },
                    onToggle = { id ->
                        todayTodos = todayTodos.map {
                            if (it.id == id) it.copy(completed = !it.completed) else it
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                
                // 底部留白给 FloatingNavigationBar
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

/**
 * 宽屏主内容区
 */
@Composable
private fun MainContentWide(
    dateText: String,
    dayOfWeekText: String,
    quote: String,
    quoteSource: String?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        // 左上角日期
        DateDisplayWidget(
            date = dateText,
            dayOfWeek = dayOfWeekText,
            modifier = Modifier.align(Alignment.TopStart),
        )
        
        // 中央一言（大字居中）
        QuoteDisplayWidget(
            quote = quote,
            quoteSource = quoteSource,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}
