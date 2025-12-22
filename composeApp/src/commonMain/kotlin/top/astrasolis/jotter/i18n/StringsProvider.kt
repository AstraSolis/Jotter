package top.astrasolis.jotter.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * 字符串资源的 CompositionLocal
 * 默认为中文
 */
val LocalStrings = staticCompositionLocalOf<Strings> { ChineseStrings }

/**
 * 字符串资源提供者
 * 在应用根层级包装，根据当前语言提供对应的字符串资源
 */
@Composable
fun StringsProvider(
    language: Language,
    content: @Composable () -> Unit,
) {
    val strings = when (language) {
        Language.CHINESE -> ChineseStrings
        Language.ENGLISH -> EnglishStrings
    }
    CompositionLocalProvider(LocalStrings provides strings) {
        content()
    }
}

/**
 * 便捷访问当前字符串资源
 * 使用方式：strings.navHome
 */
val strings: Strings
    @Composable get() = LocalStrings.current
