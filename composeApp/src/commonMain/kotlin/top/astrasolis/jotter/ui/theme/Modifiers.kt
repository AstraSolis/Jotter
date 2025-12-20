package top.astrasolis.jotter.ui.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 屏幕内边距
 */
@Composable
fun Modifier.screenPadding(): Modifier {
    val s = AppTheme.spacing
    return this.padding(
        start = s.screenH,
        end = s.screenH,
        top = s.screenV,
        bottom = s.screenV,
    )
}

/**
 * 区块垂直间距
 */
@Composable
fun Modifier.sectionVSpacing(
    top: Boolean = true,
    bottom: Boolean = true,
): Modifier {
    val s = AppTheme.spacing
    return this.then(
        Modifier.padding(
            top = if (top) s.lg else 0.dp,
            bottom = if (bottom) s.lg else 0.dp,
        ),
    )
}

/**
 * 区块水平内边距
 */
@Composable
fun Modifier.sectionHPadding(
    start: Boolean = true,
    end: Boolean = true,
): Modifier {
    val s = AppTheme.spacing
    return this.then(
        Modifier.padding(
            start = if (start) s.gutter else 0.dp,
            end = if (end) s.gutter else 0.dp,
        ),
    )
}

/**
 * 顶部内边距
 */
@Composable
fun Modifier.topPadding(
    amount: Dp = AppTheme.spacing.sm,
): Modifier {
    return this.padding(top = amount)
}

/**
 * 水平内边距
 */
@Composable
fun Modifier.horizontalPadding(
    left: Dp = AppTheme.spacing.screenH,
    right: Dp = AppTheme.spacing.screenH,
): Modifier {
    return this.padding(start = left, end = right)
}
