package top.astrasolis.jotter.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 间距 Token
 */
data class Spacing(
    val none: Dp = 0.dp,
    val xxs: Dp = 2.dp,
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 12.dp,
    val lg: Dp = 16.dp,
    val xl: Dp = 20.dp,
    val xxl: Dp = 24.dp,
    val xxxl: Dp = 32.dp,
    val gutter: Dp = 16.dp,
    val screenH: Dp = 12.dp,
    val screenV: Dp = 12.dp,
)

/**
 * 阴影高度 Token
 */
data class Elevation(
    val none: Dp = 0.dp,
    val xs: Dp = 2.dp,
    val sm: Dp = 4.dp,
    val md: Dp = 8.dp,
    val lg: Dp = 12.dp,
    val xl: Dp = 16.dp,
)

/**
 * 圆角 Token
 */
data class Radii(
    val none: Dp = 0.dp,
    val sm: Dp = 4.dp,
    val md: Dp = 8.dp,
    val lg: Dp = 12.dp,
    val xl: Dp = 16.dp,
    val xxl: Dp = 24.dp,
    val pill: Dp = 999.dp,
)

val LocalSpacing = staticCompositionLocalOf { Spacing() }
val LocalRadii = staticCompositionLocalOf { Radii() }
val LocalElevation = staticCompositionLocalOf { Elevation() }

/**
 * 应用主题访问器
 */
object AppTheme {
    val spacing: Spacing
        @Composable get() = LocalSpacing.current

    val radii: Radii
        @Composable get() = LocalRadii.current
    
    val elevation: Elevation
        @Composable get() = LocalElevation.current
}
