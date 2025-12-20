package top.astrasolis.jotter.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import top.astrasolis.jotter.ui.theme.LocalSpacing
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import top.yukonga.miuix.kmp.utils.overScrollVertical

/**
 * 屏幕滚动列表容器
 */
@Composable
fun ScreenLazyColumn(
    scrollBehavior: ScrollBehavior,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
    bottomPadding: Dp = 0.dp,
    topPadding: Dp = 0.dp,
    content: LazyListScope.() -> Unit,
) {
    val listState = rememberLazyListState()
    
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .overScrollVertical()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        state = listState,
        contentPadding = PaddingValues(
            top = innerPadding.calculateTopPadding() + topPadding,
            bottom = innerPadding.calculateBottomPadding() + bottomPadding,
        ),
        overscrollEffect = null,
        content = content,
    )
}

/**
 * 合并内边距值
 */
@Composable
fun combinePaddingValues(
    localPadding: PaddingValues,
    mainPadding: PaddingValues,
): PaddingValues {
    return PaddingValues(
        top = localPadding.calculateTopPadding(),
        bottom = localPadding.calculateBottomPadding() + mainPadding.calculateBottomPadding() + LocalSpacing.current.md,
        start = localPadding.calculateStartPadding(LayoutDirection.Ltr),
        end = localPadding.calculateEndPadding(LayoutDirection.Ltr),
    )
}
