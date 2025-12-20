package top.astrasolis.jotter.ui.theme

import androidx.compose.ui.graphics.Color
import top.yukonga.miuix.kmp.theme.darkColorScheme
import top.yukonga.miuix.kmp.theme.lightColorScheme

/**
 * 应用颜色主题枚举
 */
enum class AppColorTheme {
    ClassicMonochrome,
    OceanBlue,
    ForestGreen,
    SakuraPink,
    ModernFluent,
}

/**
 * 主题颜色配置
 */
private data class ThemeColors(
    val primary: Color,
    val onPrimary: Color,
    val primaryVariant: Color,
    val onPrimaryVariant: Color,
    val disabledPrimary: Color,
    val disabledOnPrimary: Color,
    val disabledPrimaryButton: Color,
    val disabledOnPrimaryButton: Color,
    val disabledPrimarySlider: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val tertiaryContainer: Color,
    val onTertiaryContainer: Color,
    val tertiaryContainerVariant: Color,
    val onBackgroundVariant: Color,
)

private data class ThemePalette(
    val light: ThemeColors,
    val dark: ThemeColors,
)

private fun ThemeColors.toLightScheme() = lightColorScheme(
    primary = primary,
    onPrimary = onPrimary,
    primaryVariant = primaryVariant,
    onPrimaryVariant = onPrimaryVariant,
    disabledPrimary = disabledPrimary,
    disabledOnPrimary = disabledOnPrimary,
    disabledPrimaryButton = disabledPrimaryButton,
    disabledOnPrimaryButton = disabledOnPrimaryButton,
    disabledPrimarySlider = disabledPrimarySlider,
    primaryContainer = primaryContainer,
    onPrimaryContainer = onPrimaryContainer,
    tertiaryContainer = tertiaryContainer,
    onTertiaryContainer = onTertiaryContainer,
    tertiaryContainerVariant = tertiaryContainerVariant,
    onBackgroundVariant = onBackgroundVariant,
)

private fun ThemeColors.toDarkScheme() = darkColorScheme(
    primary = primary,
    onPrimary = onPrimary,
    primaryVariant = primaryVariant,
    onPrimaryVariant = onPrimaryVariant,
    disabledPrimary = disabledPrimary,
    disabledOnPrimary = disabledOnPrimary,
    disabledPrimaryButton = disabledPrimaryButton,
    disabledOnPrimaryButton = disabledOnPrimaryButton,
    disabledPrimarySlider = disabledPrimarySlider,
    primaryContainer = primaryContainer,
    onPrimaryContainer = onPrimaryContainer,
    tertiaryContainer = tertiaryContainer,
    onTertiaryContainer = onTertiaryContainer,
    tertiaryContainerVariant = tertiaryContainerVariant,
    onBackgroundVariant = onBackgroundVariant,
)

private fun ThemePalette.toColorScheme(isDark: Boolean) =
    if (isDark) dark.toDarkScheme() else light.toLightScheme()

/**
 * 预定义主题调色板
 */
private val themePalettes = mapOf(
    // 经典黑白 - 简约优雅
    AppColorTheme.ClassicMonochrome to ThemePalette(
        light = ThemeColors(
            primary = Color(0xFF000000),
            onPrimary = Color.White,
            primaryVariant = Color(0xFF222222),
            onPrimaryVariant = Color(0xFFAAAAAA),
            disabledPrimary = Color(0xFFBDBDBD),
            disabledOnPrimary = Color(0xFFE0E0E0),
            disabledPrimaryButton = Color(0xFFBDBDBD),
            disabledOnPrimaryButton = Color(0xFFEEEEEE),
            disabledPrimarySlider = Color(0xFFDCDCDC),
            primaryContainer = Color(0xFFF0F0F0),
            onPrimaryContainer = Color(0xFF000000),
            tertiaryContainer = Color(0xFFF8F8F8),
            onTertiaryContainer = Color(0xFF000000),
            tertiaryContainerVariant = Color(0xFFF8F8F8),
            onBackgroundVariant = Color(0xFF000000),
        ),
        dark = ThemeColors(
            primary = Color.White,
            onPrimary = Color(0xFF000000),
            primaryVariant = Color(0xFFE0E0E0),
            onPrimaryVariant = Color(0xFF555555),
            disabledPrimary = Color(0xFF333333),
            disabledOnPrimary = Color(0xFF757575),
            disabledPrimaryButton = Color(0xFF333333),
            disabledOnPrimaryButton = Color(0xFF757575),
            disabledPrimarySlider = Color(0xFF444444),
            primaryContainer = Color(0xFF252525),
            onPrimaryContainer = Color.White,
            tertiaryContainer = Color(0xFF1C1C1C),
            onTertiaryContainer = Color.White,
            tertiaryContainerVariant = Color(0xFF303030),
            onBackgroundVariant = Color(0xFFE0E0E0),
        ),
    ),
    // 海洋蓝 - 清新冷静
    AppColorTheme.OceanBlue to ThemePalette(
        light = ThemeColors(
            primary = Color(0xFF67AFF6),
            onPrimary = Color(0xFFFFFFFF),
            primaryVariant = Color(0xFF8EC3FA),
            onPrimaryVariant = Color(0xFF0C2437),
            disabledPrimary = Color(0xFFCDE1F6),
            disabledOnPrimary = Color(0xFF5A6C80),
            disabledPrimaryButton = Color(0xFFBAD0ED),
            disabledOnPrimaryButton = Color(0xFFF3F6FC),
            disabledPrimarySlider = Color(0xFFD8E6F8),
            primaryContainer = Color(0xFFEFF4FA),
            onPrimaryContainer = Color(0xFF102036),
            tertiaryContainer = Color(0xFFE1EEFD),
            onTertiaryContainer = Color(0xFF2E6EDB),
            tertiaryContainerVariant = Color(0xFFD6E7FD),
            onBackgroundVariant = Color(0xFF3C6FDB),
        ),
        dark = ThemeColors(
            primary = Color(0xFF9ED4FF),
            onPrimary = Color(0xFF061220),
            primaryVariant = Color(0xFFCBE5FF),
            onPrimaryVariant = Color(0xFF03101B),
            disabledPrimary = Color(0xFF295072),
            disabledOnPrimary = Color(0xFF8AAED4),
            disabledPrimaryButton = Color(0xFF1F3D55),
            disabledOnPrimaryButton = Color(0xFF7CA4D0),
            disabledPrimarySlider = Color(0xFF2F4F6D),
            primaryContainer = Color(0xFF12243D),
            onPrimaryContainer = Color(0xFFD8EDFF),
            tertiaryContainer = Color(0xFF0C182A),
            onTertiaryContainer = Color(0xFF83B3FF),
            tertiaryContainerVariant = Color(0xFF152543),
            onBackgroundVariant = Color(0xFF7DB2FF),
        ),
    ),
    // 森林绿 - 自然清新
    AppColorTheme.ForestGreen to ThemePalette(
        light = ThemeColors(
            primary = Color(0xFF3FB53F),
            onPrimary = Color(0xFFF5FFF5),
            primaryVariant = Color(0xFF5DC35D),
            onPrimaryVariant = Color(0xFF0B2A0B),
            disabledPrimary = Color(0xFFC9E6C9),
            disabledOnPrimary = Color(0xFF5E805E),
            disabledPrimaryButton = Color(0xFFB4DAB4),
            disabledOnPrimaryButton = Color(0xFFF2FFF2),
            disabledPrimarySlider = Color(0xFFD6EDDA),
            primaryContainer = Color(0xFFF2FFF2),
            onPrimaryContainer = Color(0xFF0C230C),
            tertiaryContainer = Color(0xFFE1FFE1),
            onTertiaryContainer = Color(0xFF3B9F46),
            tertiaryContainerVariant = Color(0xFFD8F5DB),
            onBackgroundVariant = Color(0xFF46A846),
        ),
        dark = ThemeColors(
            primary = Color(0xFF7EE483),
            onPrimary = Color(0xFF061C08),
            primaryVariant = Color(0xFFADEFB0),
            onPrimaryVariant = Color(0xFF041106),
            disabledPrimary = Color(0xFF27432B),
            disabledOnPrimary = Color(0xFF8EC597),
            disabledPrimaryButton = Color(0xFF1E3522),
            disabledOnPrimaryButton = Color(0xFF7EB58A),
            disabledPrimarySlider = Color(0xFF2B4D32),
            primaryContainer = Color(0xFF142616),
            onPrimaryContainer = Color(0xFFCFFAD4),
            tertiaryContainer = Color(0xFF0F1E11),
            onTertiaryContainer = Color(0xFF95F69D),
            tertiaryContainerVariant = Color(0xFF1A2F1D),
            onBackgroundVariant = Color(0xFF9BFF9F),
        ),
    ),
    // 樱花粉 - 温柔甜美
    AppColorTheme.SakuraPink to ThemePalette(
        light = ThemeColors(
            primary = Color(0xFFFF82AB),
            onPrimary = Color(0xFFFFF7FA),
            primaryVariant = Color(0xFFEF8EB3),
            onPrimaryVariant = Color(0xFF462033),
            disabledPrimary = Color(0xFFFAD2DD),
            disabledOnPrimary = Color(0xFFC18495),
            disabledPrimaryButton = Color(0xFFF0C4D2),
            disabledOnPrimaryButton = Color(0xFFFFF0F5),
            disabledPrimarySlider = Color(0xFFFFE0EA),
            primaryContainer = Color(0xFFFFE7EF),
            onPrimaryContainer = Color(0xFF3A0F1F),
            tertiaryContainer = Color(0xFFFFD7E5),
            onTertiaryContainer = Color(0xFFE45E8E),
            tertiaryContainerVariant = Color(0xFFFFE4EE),
            onBackgroundVariant = Color(0xFFD45A8A),
        ),
        dark = ThemeColors(
            primary = Color(0xFFFFB1CE),
            onPrimary = Color(0xFF250915),
            primaryVariant = Color(0xFFFFD8E8),
            onPrimaryVariant = Color(0xFF12040A),
            disabledPrimary = Color(0xFF593246),
            disabledOnPrimary = Color(0xFFC799B1),
            disabledPrimaryButton = Color(0xFF472635),
            disabledOnPrimaryButton = Color(0xFFB87F9B),
            disabledPrimarySlider = Color(0xFF5B3145),
            primaryContainer = Color(0xFF2A1520),
            onPrimaryContainer = Color(0xFFFFDDEA),
            tertiaryContainer = Color(0xFF1F0F17),
            onTertiaryContainer = Color(0xFFFFAFDA),
            tertiaryContainerVariant = Color(0xFF2F1A25),
            onBackgroundVariant = Color(0xFFFF9AC4),
        ),
    ),
    // 现代灵动 - 柔和紫靛色调
    AppColorTheme.ModernFluent to ThemePalette(
        light = ThemeColors(
            primary = Color(0xFF6366F1),           // Indigo 主色
            onPrimary = Color.White,
            primaryVariant = Color(0xFF818CF8),    // 柔和紫
            onPrimaryVariant = Color(0xFF1E1B4B),
            disabledPrimary = Color(0xFFE0E7FF),
            disabledOnPrimary = Color(0xFF6B7280),
            disabledPrimaryButton = Color(0xFFC7D2FE),
            disabledOnPrimaryButton = Color(0xFFF5F5FF),
            disabledPrimarySlider = Color(0xFFE0E7FF),
            primaryContainer = Color(0xFFF5F5FF),  // 非常淡的紫色背景
            onPrimaryContainer = Color(0xFF312E81),
            tertiaryContainer = Color(0xFFEDE9FE),
            onTertiaryContainer = Color(0xFF5B21B6),
            tertiaryContainerVariant = Color(0xFFF5F3FF),
            onBackgroundVariant = Color(0xFF6B7280), // 柔和灰色
        ),
        dark = ThemeColors(
            primary = Color(0xFFA5B4FC),           // 柔和淡紫
            onPrimary = Color(0xFF1E1B4B),
            primaryVariant = Color(0xFFC7D2FE),
            onPrimaryVariant = Color(0xFF0F0D24),
            disabledPrimary = Color(0xFF3730A3),
            disabledOnPrimary = Color(0xFF9CA3AF),
            disabledPrimaryButton = Color(0xFF4338CA),
            disabledOnPrimaryButton = Color(0xFF9CA3AF),
            disabledPrimarySlider = Color(0xFF4338CA),
            primaryContainer = Color(0xFF1E1B4B),
            onPrimaryContainer = Color(0xFFE0E7FF),
            tertiaryContainer = Color(0xFF1C1917),
            onTertiaryContainer = Color(0xFFC4B5FD),
            tertiaryContainerVariant = Color(0xFF312E81),
            onBackgroundVariant = Color(0xFFA5B4FC),
        ),
    ),
)

private val defaultPalette = themePalettes.getValue(AppColorTheme.ClassicMonochrome)

/**
 * 根据主题和暗色模式获取配色方案
 */
fun colorSchemeForTheme(theme: AppColorTheme, isDark: Boolean) =
    (themePalettes[theme] ?: defaultPalette).toColorScheme(isDark)
