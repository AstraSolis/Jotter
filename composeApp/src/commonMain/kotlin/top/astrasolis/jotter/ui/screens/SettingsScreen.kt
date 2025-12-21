package top.astrasolis.jotter.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Storage
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import top.astrasolis.jotter.data.AppContainer
import top.astrasolis.jotter.ui.components.JotterCard
import top.astrasolis.jotter.ui.theme.AppTheme
import top.astrasolis.jotter.ui.theme.pressScale
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text
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
 * 双层导航结构
 */
@Composable
fun SettingsScreen(
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    var currentRoute by remember { mutableStateOf(SettingsRoute.MAIN) }
    
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
        SettingsGroup(title = "界面设置") {
            SettingsNavItem(
                icon = Icons.Default.Palette,
                title = "外观",
                onClick = { onNavigate(SettingsRoute.APPEARANCE) },
            )
        }
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.lg))
        
        // 数据管理分组
        SettingsGroup(title = "数据管理") {
            SettingsNavItem(
                icon = Icons.Default.Storage,
                title = "存储",
                onClick = { onNavigate(SettingsRoute.DATA) },
            )
        }
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.lg))
        
        // 更多分组
        SettingsGroup(title = "更多") {
            SettingsNavItem(
                icon = Icons.Default.Info,
                title = "关于",
                trailingText = "1.0.0",
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
        SettingsGroup(title = "主题") {
            SettingsOptionItem(
                title = "浅色模式",
                selected = false,
                onClick = { /* TODO */ },
            )
            SettingsOptionItem(
                title = "深色模式",
                selected = false,
                onClick = { /* TODO */ },
            )
            SettingsOptionItem(
                title = "跟随系统",
                selected = true,
                onClick = { /* TODO */ },
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
        SettingsGroup(title = "存储位置") {
            SettingsDetailItem(
                icon = Icons.Default.FolderOpen,
                title = "数据目录",
                subtitle = currentDataPath,
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
        SettingsGroup(title = "备份与恢复") {
            SettingsDetailItem(
                title = "导出数据",
                subtitle = "将数据导出为压缩包",
                onClick = { /* TODO */ },
            )
            SettingsDetailItem(
                title = "导入数据",
                subtitle = "从压缩包恢复数据",
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
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.md))
        
        // 应用信息
        SettingsGroup(title = "应用信息") {
            SettingsInfoItem(title = "应用名称", value = "Jotter")
            SettingsInfoItem(title = "版本", value = "1.0.0")
            SettingsInfoItem(title = "构建类型", value = "Release")
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
 * 设置分组
 */
@Composable
private fun SettingsGroup(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // 分组标题
        Text(
            text = title,
            style = MiuixTheme.textStyles.body2,
            color = MiuixTheme.colorScheme.onBackgroundVariant,
            modifier = Modifier.padding(
                horizontal = AppTheme.spacing.screenH + AppTheme.spacing.md,
                vertical = AppTheme.spacing.sm,
            ),
        )
        
        // 卡片内容
        JotterCard {
            Column(modifier = Modifier.fillMaxWidth()) {
                content()
            }
        }
    }
}

/**
 * 设置导航项（带箭头，用于主页面）
 */
@Composable
private fun SettingsNavItem(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    trailingText: String? = null,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                horizontal = AppTheme.spacing.lg,
                vertical = AppTheme.spacing.md + AppTheme.spacing.xs,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(22.dp),
            tint = MiuixTheme.colorScheme.onBackground,
        )
        
        Spacer(modifier = Modifier.width(AppTheme.spacing.lg))
        
        Text(
            text = title,
            style = MiuixTheme.textStyles.body1,
            color = MiuixTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f),
        )
        
        if (trailingText != null) {
            Text(
                text = trailingText,
                style = MiuixTheme.textStyles.body2,
                color = MiuixTheme.colorScheme.onBackgroundVariant,
            )
            Spacer(modifier = Modifier.width(AppTheme.spacing.xs))
        }
        
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MiuixTheme.colorScheme.onBackgroundVariant,
        )
    }
}

/**
 * 设置详情项（带副标题，用于子页面）
 */
@Composable
private fun SettingsDetailItem(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick) 
                else Modifier
            )
            .padding(
                horizontal = AppTheme.spacing.lg,
                vertical = AppTheme.spacing.md,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(22.dp),
                tint = MiuixTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.width(AppTheme.spacing.lg))
        }
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MiuixTheme.textStyles.body1,
                color = MiuixTheme.colorScheme.onBackground,
            )
            Text(
                text = subtitle,
                style = MiuixTheme.textStyles.body2,
                color = MiuixTheme.colorScheme.onBackgroundVariant,
                maxLines = 1,
            )
        }
        
        if (onClick != null) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MiuixTheme.colorScheme.onBackgroundVariant,
            )
        }
    }
}

/**
 * 设置选项项（用于单选）
 */
@Composable
private fun SettingsOptionItem(
    title: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                horizontal = AppTheme.spacing.lg,
                vertical = AppTheme.spacing.md,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MiuixTheme.textStyles.body1,
            color = if (selected) MiuixTheme.colorScheme.primary 
                   else MiuixTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f),
        )
        
        if (selected) {
            Text(
                text = "✓",
                style = MiuixTheme.textStyles.body1,
                color = MiuixTheme.colorScheme.primary,
            )
        }
    }
}

/**
 * 设置信息项（纯展示）
 */
@Composable
private fun SettingsInfoItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = AppTheme.spacing.lg,
                vertical = AppTheme.spacing.md,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MiuixTheme.textStyles.body1,
            color = MiuixTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f),
        )
        
        Text(
            text = value,
            style = MiuixTheme.textStyles.body2,
            color = MiuixTheme.colorScheme.onBackgroundVariant,
        )
    }
}
