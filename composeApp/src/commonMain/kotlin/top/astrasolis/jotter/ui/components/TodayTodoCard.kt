package top.astrasolis.jotter.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextDecoration
import top.astrasolis.jotter.data.Todo
import top.astrasolis.jotter.i18n.strings
import top.astrasolis.jotter.ui.theme.AppTheme
import top.yukonga.miuix.kmp.basic.Checkbox
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

// 卡片圆角
private val CardShape = RoundedCornerShape(16)

/**
 * 今日待办卡片组件
 * 现代化设计：柔和阴影 + 圆角 + 项目交互动画
 */
@Composable
fun TodayTodoCard(
    todos: List<Todo>,
    onTodoClick: (String) -> Unit,
    onToggle: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            // 柔和阴影效果
            .shadow(
                elevation = AppTheme.elevation.md,
                shape = CardShape,
                ambientColor = MiuixTheme.colorScheme.primary.copy(alpha = 0.08f),
                spotColor = MiuixTheme.colorScheme.primary.copy(alpha = 0.12f),
            )
            .clip(CardShape)
            .background(MiuixTheme.colorScheme.surface)
            .padding(AppTheme.spacing.lg),
    ) {
        // 标题
        Text(
            text = strings.homeTodayTodo,
            style = MiuixTheme.textStyles.title3,
            color = MiuixTheme.colorScheme.onBackground,
        )
        
        Spacer(modifier = Modifier.height(AppTheme.spacing.md))
        
        if (todos.isEmpty()) {
            // 空状态
            Text(
                text = strings.homeTodayTodoEmpty,
                style = MiuixTheme.textStyles.body2,
                color = MiuixTheme.colorScheme.onBackgroundVariant,
                modifier = Modifier.padding(vertical = AppTheme.spacing.lg),
            )
        } else {
            // 待办项列表 - 移除 weight 修饰符以避免在可滚动父容器中高度为 0 的问题
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                todos.forEach { todo ->
                    TodayTodoItem(
                        todo = todo,
                        onClick = { onTodoClick(todo.id) },
                        onToggle = { onToggle(todo.id) },
                    )
                }
            }
        }
    }
}

/**
 * 单个待办项
 * 带勾选时的柔和动画效果
 */
// 待办项圆角
private val ItemShape = RoundedCornerShape(20)

@Composable
private fun TodayTodoItem(
    todo: Todo,
    onClick: () -> Unit,
    onToggle: () -> Unit,
) {
    // 悬停状态
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    
    // 勾选时的透明度和缩放动画
    val targetAlpha = if (todo.completed) 0.6f else 1f
    val animatedAlpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = spring(stiffness = 300f),
    )
    
    // 悬停背景色动画
    val hoverBackgroundColor by animateColorAsState(
        targetValue = if (isHovered) {
            MiuixTheme.colorScheme.primary.copy(alpha = 0.08f)
        } else {
            Color.Transparent
        },
        animationSpec = spring(stiffness = 400f),
    )
    
    // 文字颜色动画
    val textColor by animateColorAsState(
        targetValue = if (todo.completed) {
            MiuixTheme.colorScheme.onBackgroundVariant
        } else {
            MiuixTheme.colorScheme.onBackground
        },
        animationSpec = spring(stiffness = 300f),
    )
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(ItemShape)
            .hoverable(interactionSource = interactionSource)
            .background(hoverBackgroundColor)
            .graphicsLayer { alpha = animatedAlpha }
            .clickable(onClick = onClick)
            .padding(vertical = AppTheme.spacing.sm, horizontal = AppTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = todo.completed,
            onCheckedChange = { onToggle() },
        )
        
        Spacer(modifier = Modifier.width(AppTheme.spacing.sm))
        
        Text(
            text = todo.title,
            style = MiuixTheme.textStyles.body2,
            color = textColor,
            textDecoration = if (todo.completed) {
                TextDecoration.LineThrough
            } else {
                TextDecoration.None
            },
            modifier = Modifier.weight(1f),
        )
    }
}

