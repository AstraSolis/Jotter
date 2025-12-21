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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
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
import top.astrasolis.jotter.ui.components.PlatformBackHandler
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
    DEVELOPER,
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
    onShowSetupScreen: () -> Unit = {},
) {
    var currentRoute by remember { mutableStateOf(SettingsRoute.MAIN) }
    
    // Toast 状态管理
    var toastMessage by remember { mutableStateOf<String?>(null) }
    
    // 处理系统返回键（仅在二级菜单时拦截）
    PlatformBackHandler(enabled = currentRoute != SettingsRoute.MAIN) {
        currentRoute = SettingsRoute.MAIN
    }
    
    // 当路由变化时通知外层
    LaunchedEffect(currentRoute) {
        onSubPageChange(currentRoute != SettingsRoute.MAIN)
    }
    
    // Toast 自动消失
    LaunchedEffect(toastMessage) {
        if (toastMessage != null) {
            kotlinx.coroutines.delay(2000)
            toastMessage = null
        }
    }
    
    androidx.compose.foundation.layout.Box(modifier = modifier.fillMaxSize()) {
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
            modifier = Modifier.fillMaxSize(),
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
                    onShowToast = { toastMessage = it },
                )
                SettingsRoute.DEVELOPER -> DeveloperToolsPage(
                    innerPadding = innerPadding,
                    onBack = { currentRoute = SettingsRoute.MAIN },
                    onShowSetupScreen = onShowSetupScreen,
                    onShowToast = { toastMessage = it },
                )
            }
        }
        
        // Toast 显示
        toastMessage?.let { message ->
            androidx.compose.animation.AnimatedVisibility(
                visible = true,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 100.dp),
            ) {
                Card(
                    modifier = Modifier.padding(horizontal = AppTheme.spacing.screenH),
                ) {
                    Text(
                        text = message,
                        style = MiuixTheme.textStyles.body2,
                        color = MiuixTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(
                            horizontal = AppTheme.spacing.xl,
                            vertical = AppTheme.spacing.md,
                        ),
                    )
                }
            }
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
    // 检查开发者模式状态
    val isDeveloperModeEnabled = remember { 
        AppContainer.settingsRepository.isDeveloperModeEnabled() 
    }
    
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
            
            // 开发者工具（仅在启用时显示）
            if (isDeveloperModeEnabled) {
                SuperArrow(
                    title = "开发者工具",
                    summary = null,
                    leftAction = {
                        Row {
                            Icon(
                                imageVector = Icons.Default.Code,
                                contentDescription = null,
                                modifier = Modifier.size(22.dp),
                                tint = MiuixTheme.colorScheme.onSurface,
                            )
                            Spacer(modifier = Modifier.width(AppTheme.spacing.lg))
                        }
                    },
                    onClick = { onNavigate(SettingsRoute.DEVELOPER) },
                )
            }
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
    onShowToast: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    
    // 点击计数器用于激活开发者模式
    var clickCount by remember { mutableIntStateOf(0) }
    var devModeActivated by remember { mutableStateOf(false) }
    
    // 检查是否已经是开发者模式
    val isAlreadyDeveloper = remember { 
        AppContainer.settingsRepository.isDeveloperModeEnabled() 
    }
    
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
        
        // 应用介绍卡片（点击激活开发者模式）
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.screenH),
        ) {
            top.yukonga.miuix.kmp.basic.BasicComponent(
                title = "Jotter",
                summary = "一个简洁优雅的跨平台笔记应用，支持日记、待办和笔记管理。",
                onClick = if (!isAlreadyDeveloper && !devModeActivated) {
                    {
                        clickCount++
                        if (clickCount >= 5) {
                            // 激活开发者模式
                            scope.launch {
                                AppContainer.settingsRepository.setDeveloperModeEnabled(true)
                                devModeActivated = true
                                onShowToast("开发者模式已激活")
                            }
                        } else if (clickCount >= 3) {
                            // 显示剩余次数提示
                            onShowToast("再点 ${5 - clickCount} 次激活开发者模式")
                        }
                    }
                } else null,
            )
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

/**
 * 开发者工具页面
 */
@Composable
private fun DeveloperToolsPage(
    innerPadding: PaddingValues,
    onBack: () -> Unit,
    onShowSetupScreen: () -> Unit,
    onShowToast: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    
    // 存储信息
    val storageInfo = remember { AppContainer.settingsRepository.getStorageInfo() }
    
    // 操作状态
    var showResetConfirm by remember { mutableStateOf(false) }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState()),
    ) {
        // 返回标题栏
        SettingsTopBar(title = "开发者工具", onBack = onBack)
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.md))
        
        // 调试功能
        SmallTitle(text = "调试功能")
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.screenH),
        ) {
            SuperArrow(
                title = "显示首次启动引导页",
                summary = "立即显示引导页流程",
                leftAction = {
                    Row {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(22.dp),
                            tint = MiuixTheme.colorScheme.onSurface,
                        )
                        Spacer(modifier = Modifier.width(AppTheme.spacing.lg))
                    }
                },
                onClick = {
                    onShowSetupScreen()
                },
            )
            
            SuperArrow(
                title = "重置所有设置",
                summary = "将设置恢复为默认值",
                leftAction = {
                    Row {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(22.dp),
                            tint = MiuixTheme.colorScheme.onSurface,
                        )
                        Spacer(modifier = Modifier.width(AppTheme.spacing.lg))
                    }
                },
                onClick = {
                    showResetConfirm = true
                },
            )
        }
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.lg))
        
        // 存储信息
        SmallTitle(text = "存储信息")
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.screenH),
        ) {
            Column(
                modifier = Modifier.padding(AppTheme.spacing.lg),
            ) {
                StorageInfoItem("数据目录", storageInfo.dataPath)
                Spacer(modifier = Modifier.height(AppTheme.spacing.sm))
                StorageInfoItem("日记数量", "${storageInfo.journalCount} 篇")
                Spacer(modifier = Modifier.height(AppTheme.spacing.sm))
                StorageInfoItem("笔记数量", "${storageInfo.noteCount} 篇")
                Spacer(modifier = Modifier.height(AppTheme.spacing.sm))
                StorageInfoItem("待办数量", "${storageInfo.todoCount} 条")
            }
        }
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.lg))
        
        // 版本信息
        SmallTitle(text = "版本信息")
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.screenH),
        ) {
            Column(
                modifier = Modifier.padding(AppTheme.spacing.lg),
            ) {
                StorageInfoItem("应用版本", "1.0.0")
                Spacer(modifier = Modifier.height(AppTheme.spacing.sm))
                StorageInfoItem("构建类型", "Debug")
                Spacer(modifier = Modifier.height(AppTheme.spacing.sm))
                StorageInfoItem("平台", getPlatformName())
            }
        }
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.lg))
        
        // 关闭开发者模式
        SmallTitle(text = "开发者模式")
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.screenH),
        ) {
            SuperArrow(
                title = "关闭开发者模式",
                summary = "隐藏开发者工具入口",
                leftAction = {
                    Row {
                        Icon(
                            imageVector = Icons.Default.Build,
                            contentDescription = null,
                            modifier = Modifier.size(22.dp),
                            tint = MiuixTheme.colorScheme.onSurface,
                        )
                        Spacer(modifier = Modifier.width(AppTheme.spacing.lg))
                    }
                },
                onClick = {
                    scope.launch {
                        AppContainer.settingsRepository.setDeveloperModeEnabled(false)
                        onShowToast("开发者模式已关闭")
                        onBack()
                    }
                },
            )
        }
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.xxl))
        
        // 重置确认对话框状态处理
        if (showResetConfirm) {
            // 简单处理：直接执行重置
            LaunchedEffect(Unit) {
                AppContainer.settingsRepository.resetAllSettings()
                onShowToast("已重置所有设置")
                showResetConfirm = false
            }
        }
    }
}

/**
 * 存储信息项
 */
@Composable
private fun StorageInfoItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MiuixTheme.textStyles.body1,
            color = MiuixTheme.colorScheme.onBackgroundVariant,
        )
        Text(
            text = value,
            style = MiuixTheme.textStyles.body1,
            color = MiuixTheme.colorScheme.onBackground,
        )
    }
}

/**
 * 获取平台名称
 */
private fun getPlatformName(): String {
    return "Kotlin Multiplatform"
}
