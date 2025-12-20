package top.astrasolis.jotter.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.astrasolis.jotter.ui.theme.AppTheme
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 设置页面
 * 预留页面，后期添加设置项
 */
@Composable
fun SettingsScreen(
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        // 页面标题
        Text(
            text = "设置",
            style = MiuixTheme.textStyles.title2,
            color = MiuixTheme.colorScheme.onBackground,
            modifier = Modifier.padding(
                horizontal = AppTheme.spacing.screenH,
                vertical = AppTheme.spacing.lg,
            ),
        )
        
        // 内容区域（居中）
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MiuixTheme.colorScheme.onBackgroundVariant,
            )
            
            Text(
                text = "设置功能即将推出",
                style = MiuixTheme.textStyles.body1,
                color = MiuixTheme.colorScheme.onBackgroundVariant,
                modifier = Modifier.padding(top = AppTheme.spacing.lg),
            )
        }
    }
}



