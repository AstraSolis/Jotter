package top.astrasolis.jotter.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * Jotter 应用主题包装器
 *
 * @param colorTheme 颜色主题（默认使用现代灵动主题）
 * @param darkTheme 是否使用暗色模式
 * @param spacing 间距配置
 * @param radii 圆角配置
 * @param elevation 阴影高度配置
 * @param content 内容
 */
@Composable
fun JotterTheme(
    colorTheme: AppColorTheme = AppColorTheme.ModernFluent,
    darkTheme: Boolean = isSystemInDarkTheme(),
    spacing: Spacing = Spacing(),
    radii: Radii = Radii(),
    elevation: Elevation = Elevation(),
    content: @Composable () -> Unit,
) {
    val colors = remember(colorTheme, darkTheme) {
        colorSchemeForTheme(colorTheme, darkTheme)
    }

    CompositionLocalProvider(
        LocalSpacing provides spacing,
        LocalRadii provides radii,
        LocalElevation provides elevation,
    ) {
        MiuixTheme(
            colors = colors,
        ) {
            content()
        }
    }
}

