package top.astrasolis.jotter.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import okio.Path
import top.astrasolis.jotter.data.AppContainer
import top.astrasolis.jotter.i18n.strings
import top.astrasolis.jotter.ui.theme.AppTheme
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CircularProgressIndicator
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 首次启动设置页面
 * 引导用户选择数据存储位置
 */
@Composable
fun SetupScreen(
    onSetupComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    var currentStep by remember { mutableStateOf(SetupStep.WELCOME) }
    var selectedPath by remember { mutableStateOf<Path?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    // 提前获取字符串（用于非 @Composable 上下文）
    val selectDirErrorMsg = strings.setupSelectDirError
    val initErrorMsg = strings.setupInitError
    
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(AppTheme.spacing.xxl),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // 步骤指示器
            StepIndicator(
                currentStep = currentStep,
                modifier = Modifier.padding(bottom = AppTheme.spacing.xxl),
            )
            
            // 内容区域
            AnimatedVisibility(
                visible = currentStep == SetupStep.WELCOME,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                WelcomeContent(
                    onNext = { currentStep = SetupStep.STORAGE },
                )
            }
            
            AnimatedVisibility(
                visible = currentStep == SetupStep.STORAGE,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                StorageContent(
                    selectedPath = selectedPath,
                    isLoading = isLoading,
                    errorMessage = errorMessage,
                    onPickDirectory = {
                        scope.launch {
                            isLoading = true
                            errorMessage = null
                            try {
                                val picker = AppContainer.directoryPicker
                                if (picker.isSupported()) {
                                    val path = picker.pickDirectory()
                                    if (path != null) {
                                        selectedPath = path
                                    }
                                } else {
                                    // 使用默认目录
                                    selectedPath = AppContainer.fileSystem.getDefaultDataDir()
                                }
                            } catch (e: Exception) {
                                errorMessage = selectDirErrorMsg + ": ${e.message}"
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    onUseDefault = {
                        selectedPath = AppContainer.fileSystem.getDefaultDataDir()
                    },
                    onNext = {
                        selectedPath?.let {
                            currentStep = SetupStep.CONFIRM
                        }
                    },
                    onBack = { currentStep = SetupStep.WELCOME },
                )
            }
            
            AnimatedVisibility(
                visible = currentStep == SetupStep.CONFIRM,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                ConfirmContent(
                    selectedPath = selectedPath,
                    isLoading = isLoading,
                    errorMessage = errorMessage,
                    onConfirm = {
                        scope.launch {
                            isLoading = true
                            errorMessage = null
                            try {
                                val path = selectedPath ?: AppContainer.fileSystem.getDefaultDataDir()
                                
                                // 初始化数据目录
                                AppContainer.dataDirectoryManager.initializeDataDirectory(path)
                                
                                // 保存设置
                                AppContainer.settingsRepository.updateDataPath(path)
                                AppContainer.settingsRepository.markFirstLaunchComplete()
                                
                                // 完成设置
                                onSetupComplete()
                            } catch (e: Exception) {
                                errorMessage = initErrorMsg + ": ${e.message}"
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    onBack = { currentStep = SetupStep.STORAGE },
                )
            }
        }
    }
}

/**
 * 设置步骤枚举
 */
private enum class SetupStep {
    WELCOME,
    STORAGE,
    CONFIRM,
}

/**
 * 步骤指示器
 */
@Composable
private fun StepIndicator(
    currentStep: SetupStep,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SetupStep.entries.forEachIndexed { index, step ->
            val isActive = step == currentStep
            val isPast = step.ordinal < currentStep.ordinal
            
            // 圆点
            Box(
                modifier = Modifier.size(12.dp),
                contentAlignment = Alignment.Center,
            ) {
                if (isPast) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = MiuixTheme.colorScheme.primary,
                    )
                } else {
                    androidx.compose.foundation.Canvas(modifier = Modifier.size(12.dp)) {
                        drawCircle(
                            color = if (isActive) 
                                androidx.compose.ui.graphics.Color(0xFF1976D2) 
                            else 
                                androidx.compose.ui.graphics.Color.Gray,
                        )
                    }
                }
            }
            
            // 连接线（最后一个不显示）
            if (index < SetupStep.entries.size - 1) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(2.dp)
                        .padding(horizontal = 4.dp),
                ) {
                    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                        drawRect(
                            color = if (isPast) 
                                androidx.compose.ui.graphics.Color(0xFF1976D2) 
                            else 
                                androidx.compose.ui.graphics.Color.Gray,
                        )
                    }
                }
            }
        }
    }
}

/**
 * 欢迎内容
 */
@Composable
private fun WelcomeContent(
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Default.Create,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MiuixTheme.colorScheme.primary,
        )
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.xl))
        
        Text(
            text = strings.setupWelcome,
            style = MiuixTheme.textStyles.title1,
            color = MiuixTheme.colorScheme.onBackground,
        )
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.md))
        
        Text(
            text = strings.setupWelcomeSubtitle,
            style = MiuixTheme.textStyles.body1,
            color = MiuixTheme.colorScheme.onBackgroundVariant,
            textAlign = TextAlign.Center,
        )
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.lg))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(AppTheme.spacing.lg),
            ) {
                FeatureItem("📝", strings.setupFeatureJournal, strings.setupFeatureJournalDesc)
                FeatureItem("✅", strings.setupFeatureTodo, strings.setupFeatureTodoDesc)
                FeatureItem("📒", strings.setupFeatureNotes, strings.setupFeatureNotesDesc)
            }
        }
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.xxl))
        
        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = strings.setupStart)
        }
    }
}

@Composable
private fun FeatureItem(
    emoji: String,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = AppTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = emoji,
            style = MiuixTheme.textStyles.title3,
        )
        
        Spacer(modifier = Modifier.width(AppTheme.spacing.md))
        
        Column {
            Text(
                text = title,
                style = MiuixTheme.textStyles.subtitle,
                color = MiuixTheme.colorScheme.onBackground,
            )
            Text(
                text = description,
                style = MiuixTheme.textStyles.body2,
                color = MiuixTheme.colorScheme.onBackgroundVariant,
            )
        }
    }
}

/**
 * 存储位置选择内容
 */
@Composable
private fun StorageContent(
    selectedPath: Path?,
    isLoading: Boolean,
    errorMessage: String?,
    onPickDirectory: () -> Unit,
    onUseDefault: () -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Default.FolderOpen,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MiuixTheme.colorScheme.primary,
        )
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.lg))
        
        Text(
            text = strings.setupSelectStorage,
            style = MiuixTheme.textStyles.title2,
            color = MiuixTheme.colorScheme.onBackground,
        )
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.sm))
        
        Text(
            text = strings.setupStorageHint,
            style = MiuixTheme.textStyles.body1,
            color = MiuixTheme.colorScheme.onBackgroundVariant,
            textAlign = TextAlign.Center,
        )
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.xl))
        
        // 选中的路径显示
        Card(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(AppTheme.spacing.lg),
            ) {
                Text(
                    text = strings.setupStorageLocation,
                    style = MiuixTheme.textStyles.subtitle,
                    color = MiuixTheme.colorScheme.onBackgroundVariant,
                )
                
                Spacer(modifier = Modifier.height(AppTheme.spacing.sm))
                
                Text(
                    text = selectedPath?.toString() ?: strings.setupNotSelected,
                    style = MiuixTheme.textStyles.body1,
                    color = if (selectedPath != null) 
                        MiuixTheme.colorScheme.onBackground 
                    else 
                        MiuixTheme.colorScheme.onBackgroundVariant,
                )
            }
        }
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.lg))
        
        // 错误信息
        errorMessage?.let { error ->
            Text(
                text = error,
                style = MiuixTheme.textStyles.body2,
                color = MiuixTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = AppTheme.spacing.md),
            )
        }
        
        // 按钮组
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
            ) {
                // 判断是否支持目录选择
                if (AppContainer.directoryPicker.isSupported()) {
                    Button(
                        onClick = onPickDirectory,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(text = strings.setupSelectDir)
                    }
                    
                    TextButton(
                        text = strings.setupUseDefault,
                        onClick = onUseDefault,
                        modifier = Modifier.fillMaxWidth(),
                    )
                } else {
                    Button(
                        onClick = onUseDefault,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(text = strings.setupUseDefault)
                    }
                    
                    Text(
                        text = strings.setupDirNotSupported,
                        style = MiuixTheme.textStyles.body2,
                        color = MiuixTheme.colorScheme.onBackgroundVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.xxl))
        
        // 导航按钮
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            TextButton(
                text = strings.setupBack,
                onClick = onBack,
            )
            
            Button(
                onClick = onNext,
                enabled = selectedPath != null,
            ) {
                Text(text = strings.setupNext)
            }
        }
    }
}

/**
 * 确认内容
 */
@Composable
private fun ConfirmContent(
    selectedPath: Path?,
    isLoading: Boolean,
    errorMessage: String?,
    onConfirm: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MiuixTheme.colorScheme.primary,
        )
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.lg))
        
        Text(
            text = strings.setupConfirm,
            style = MiuixTheme.textStyles.title2,
            color = MiuixTheme.colorScheme.onBackground,
        )
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.xl))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(AppTheme.spacing.lg),
            ) {
                Text(
                    text = strings.setupDataSaveTo,
                    style = MiuixTheme.textStyles.subtitle,
                    color = MiuixTheme.colorScheme.onBackgroundVariant,
                )
                
                Spacer(modifier = Modifier.height(AppTheme.spacing.sm))
                
                Text(
                    text = selectedPath?.toString() ?: "",
                    style = MiuixTheme.textStyles.body1,
                    color = MiuixTheme.colorScheme.onBackground,
                )
                
                Spacer(modifier = Modifier.height(AppTheme.spacing.lg))
                
                Text(
                    text = strings.setupDirStructure,
                    style = MiuixTheme.textStyles.subtitle,
                    color = MiuixTheme.colorScheme.onBackgroundVariant,
                )
                
                Spacer(modifier = Modifier.height(AppTheme.spacing.sm))
                
                Column {
                    DirectoryItem("📁 journals/", strings.setupDirJournals)
                    DirectoryItem("📁 notes/", strings.setupDirNotes)
                    DirectoryItem("📁 todos/", strings.setupDirTodos)
                    DirectoryItem("📁 config/", strings.setupDirConfig)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.lg))
        
        // 错误信息
        errorMessage?.let { error ->
            Text(
                text = error,
                style = MiuixTheme.textStyles.body2,
                color = MiuixTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = AppTheme.spacing.md),
            )
        }
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.xl))
        
        // 导航按钮
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                TextButton(
                    text = strings.setupBack,
                    onClick = onBack,
                )
                
                Button(
                    onClick = onConfirm,
                ) {
                    Text(text = strings.setupComplete)
                }
            }
        }
    }
}

@Composable
private fun DirectoryItem(
    name: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Text(
            text = name,
            style = MiuixTheme.textStyles.body1,
            color = MiuixTheme.colorScheme.onBackground,
            modifier = Modifier.width(120.dp),
        )
        Text(
            text = description,
            style = MiuixTheme.textStyles.body2,
            color = MiuixTheme.colorScheme.onBackgroundVariant,
        )
    }
}
