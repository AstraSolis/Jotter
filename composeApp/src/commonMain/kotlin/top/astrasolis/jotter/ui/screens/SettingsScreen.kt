package top.astrasolis.jotter.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Storage
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import top.astrasolis.jotter.data.AppContainer
import top.astrasolis.jotter.ui.theme.AppTheme
import top.astrasolis.jotter.ui.theme.pressScale
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.extra.SuperArrow
import top.yukonga.miuix.kmp.extra.SuperDropdown
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 设置页面路由
 */
private enum class SettingsRoute {
    MAIN,
    APPEARANCE,
    DATA,
    ABOUT,
}

/**
 * 设置页面
 * 双层导航结构，使用 miuix 原生组件
 */
@Composable
fun SettingsScreen(
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
    onSubPageChange: (Boolean) -> Unit = {},
) {
    var currentRoute by remember { mutableStateOf(SettingsRoute.MAIN) }
    
    // 当路由变化时通知外层
    LaunchedEffect(currentRoute) {
        onSubPageChange(currentRoute != SettingsRoute.MAIN)
    }
    
    AnimatedContent(
        targetState = currentRoute,
        transitionSpec = {
            if (targetState == SettingsRoute.MAIN) {
                // 返回主页面
                (slideInHorizontally { -it } + fadeIn()) togetherWith
                    (slideOutHorizontally { it } + fadeOut())
            } else {
                // 进入子页面
                (slideInHorizontally { it } + fadeIn()) togetherWith
                    (slideOutHorizontally { -it } + fadeOut())
            }
        },
        modifier = modifier.fillMaxSize(),
    ) { route ->
        when (route) {
            SettingsRoute.MAIN -> MainSettingsPage(
                innerPadding = innerPadding,
                onNavigate = { currentRoute = it },
            )
            SettingsRoute.APPEARANCE -> AppearanceSettingsPage(
                innerPadding = innerPadding,
                onBack = { currentRoute = SettingsRoute.MAIN },
            )
            SettingsRoute.DATA -> DataSettingsPage(
                innerPadding = innerPadding,
                onBack = { currentRoute = SettingsRoute.MAIN },
            )
            SettingsRoute.ABOUT -> AboutPage(
                innerPadding = innerPadding,
                onBack = { currentRoute = SettingsRoute.MAIN },
            )
        }
    }
}

/**
 * 设置主页面
 */
@Composable
private fun MainSettingsPage(
    innerPadding: PaddingValues,
    onNavigate: (SettingsRoute) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState()),
    ) {
        // 页面标题
        Text(
            text = "设置",
            style = MiuixTheme.textStyles.title1,
            color = MiuixTheme.colorScheme.onBackground,
            modifier = Modifier.padding(
                horizontal = AppTheme.spacing.screenH,
                vertical = AppTheme.spacing.xl,
            ),
        )
        
        // 界面设置分组
        SmallTitle(text = "界面设置")
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.screenH),
        ) {
            SuperArrow(
                title = "外观",
                summary = null,
                leftAction = {
                    Row {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = null,
                            modifier = Modifier.size(22.dp),
                            tint = MiuixTheme.colorScheme.onSurface,
                        )
                        Spacer(modifier = Modifier.width(AppTheme.spacing.lg))
                    }
                },
                onClick = { onNavigate(SettingsRoute.APPEARANCE) },
            )
        }
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.lg))
        
        // 数据管理分组
        SmallTitle(text = "数据管理")
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.screenH),
        ) {
            SuperArrow(
                title = "存储",
                summary = null,
                leftAction = {
                    Row {
                        Icon(
                            imageVector = Icons.Default.Storage,
                            contentDescription = null,
                            modifier = Modifier.size(22.dp),
                            tint = MiuixTheme.colorScheme.onSurface,
                        )
                        Spacer(modifier = Modifier.width(AppTheme.spacing.lg))
                    }
                },
                onClick = { onNavigate(SettingsRoute.DATA) },
            )
        }
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.lg))
        
        // 更多分组
        SmallTitle(text = "更多")
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.screenH),
        ) {
            SuperArrow(
                title = "关于",
                summary = "1.0.0",
                leftAction = {
                    Row {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            modifier = Modifier.size(22.dp),
                            tint = MiuixTheme.colorScheme.onSurface,
                        )
                        Spacer(modifier = Modifier.width(AppTheme.spacing.lg))
                    }
                },
                onClick = { onNavigate(SettingsRoute.ABOUT) },
            )
        }
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.xxl))
    }
}

/**
 * 外观设置页面
 */
@Composable
private fun AppearanceSettingsPage(
    innerPadding: PaddingValues,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // 主题选项
    val themeOptions = remember { listOf("浅色模式", "深色模式", "跟随系统") }
    var selectedThemeIndex by remember { mutableIntStateOf(2) } // 默认跟随系统
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState()),
    ) {
        // 返回标题栏
        SettingsTopBar(title = "外观", onBack = onBack)
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.md))
        
        // 主题选择
        SmallTitle(text = "主题")
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.screenH),
        ) {
            SuperDropdown(
                title = "主题模式",
                items = themeOptions,
                selectedIndex = selectedThemeIndex,
                onSelectedIndexChange = { index ->
                    selectedThemeIndex = index
                    // TODO: 保存主题设置
                },
            )
        }
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.xxl))
    }
}

/**
 * 数据设置页面
 */
@Composable
private fun DataSettingsPage(
    innerPadding: PaddingValues,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    var currentDataPath by remember { 
        mutableStateOf(AppContainer.settingsRepository.getDataPath().toString()) 
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState()),
    ) {
        // 返回标题栏
        SettingsTopBar(title = "存储", onBack = onBack)
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.md))
        
        // 存储位置
        SmallTitle(text = "存储位置")
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.screenH),
        ) {
            SuperArrow(
                title = "数据目录",
                summary = currentDataPath,
                leftAction = {
                    Icon(
                        imageVector = Icons.Default.FolderOpen,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp),
                        tint = MiuixTheme.colorScheme.onSurface,
                    )
                },
                onClick = {
                    scope.launch {
                        val picker = AppContainer.directoryPicker
                        if (picker.isSupported()) {
                            val newPath = picker.pickDirectory()
                            if (newPath != null) {
                                val oldPath = AppContainer.settingsRepository.getDataPath()
                                if (oldPath != newPath) {
                                    AppContainer.dataDirectoryManager.migrateData(oldPath, newPath)
                                }
                                AppContainer.settingsRepository.updateDataPath(newPath)
                                currentDataPath = newPath.toString()
                            }
                        }
                    }
                },
            )
        }
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.lg))
        
        // 备份与恢复
        SmallTitle(text = "备份与恢复")
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.screenH),
        ) {
            SuperArrow(
                title = "导出数据",
                summary = "将数据导出为压缩包",
                onClick = { /* TODO */ },
            )
            SuperArrow(
                title = "导入数据",
                summary = "从压缩包恢复数据",
                onClick = { /* TODO */ },
            )
        }
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.xxl))
    }
}

/**
 * 关于页面
 */
@Composable
private fun AboutPage(
    innerPadding: PaddingValues,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState()),
    ) {
        // 返回标题栏
        SettingsTopBar(title = "关于", onBack = onBack)
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.xl))
        
        // 应用图标区域（居中）
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // 应用图标占位（后续替换为实际图标）
            Card(
                modifier = Modifier.size(80.dp),
            ) {
                // TODO: 替换为应用图标
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(AppTheme.spacing.lg),
                    tint = MiuixTheme.colorScheme.primary,
                )
            }
            
            Spacer(modifier = Modifier.height(AppTheme.spacing.lg))
            
            // 应用名称
            Text(
                text = "Jotter",
                style = MiuixTheme.textStyles.title1,
                color = MiuixTheme.colorScheme.onBackground,
            )
            
            Spacer(modifier = Modifier.height(AppTheme.spacing.xs))
            
            // 版本号
            Text(
                text = "1.0.0",
                style = MiuixTheme.textStyles.body2,
                color = MiuixTheme.colorScheme.onBackgroundVariant,
            )
        }
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.xl))
        
        // 应用介绍卡片
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.screenH),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.spacing.lg),
            ) {
                Text(
                    text = "Jotter",
                    style = MiuixTheme.textStyles.title3,
                    color = MiuixTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(AppTheme.spacing.xs))
                Text(
                    text = "一个简洁优雅的跨平台笔记应用，支持日记、待办和笔记管理。",
                    style = MiuixTheme.textStyles.body2,
                    color = MiuixTheme.colorScheme.onBackgroundVariant,
                )
            }
        }
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.lg))
        
        // 项目链接
        SmallTitle(text = "项目链接")
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.screenH),
        ) {
            val uriHandler = LocalUriHandler.current
            SuperArrow(
                title = "GitHub",
                summary = "https://github.com/AstraSolis/Jotter",
                onClick = { 
                    uriHandler.openUri("https://github.com/AstraSolis/Jotter")
                },
            )
        }
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.xxl))
    }
}

// ==================== 通用组件 ====================

/**
 * 设置返回标题栏
 */
@Composable
private fun SettingsTopBar(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = AppTheme.spacing.screenH,
                vertical = AppTheme.spacing.lg,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 返回按钮
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "返回",
            modifier = Modifier
                .size(24.dp)
                .pressScale(onClick = onBack),
            tint = MiuixTheme.colorScheme.onBackground,
        )
        
        Spacer(modifier = Modifier.width(AppTheme.spacing.lg))
        
        // 标题
        Text(
            text = title,
            style = MiuixTheme.textStyles.title2,
            color = MiuixTheme.colorScheme.onBackground,
        )
    }
}
