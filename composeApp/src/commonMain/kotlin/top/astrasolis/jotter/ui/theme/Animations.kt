package top.astrasolis.jotter.ui.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.launch

/**
 * 应用动画配置
 */
object AppAnimations {
    // 动画时长
    const val SHORT_DURATION = 150
    const val MEDIUM_DURATION = 300
    const val LONG_DURATION = 500
    const val FADE_IN_DURATION = 600
    
    // 按压缩放比例
    const val PRESS_SCALE = 0.92f
    
    // 弹簧动画配置
    val pressSpring = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium,
    )
    
    val gentleSpring = spring<Float>(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessLow,
    )
}

/**
 * 按压缩放效果修饰符
 * 点击时产生明显的缩放动画
 */
fun Modifier.pressScale(
    enabled: Boolean = true,
    scale: Float = AppAnimations.PRESS_SCALE,
    onClick: () -> Unit = {},
): Modifier = composed {
    if (!enabled) return@composed this
    
    val scaleAnim = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()
    
    this
        .graphicsLayer {
            scaleX = scaleAnim.value
            scaleY = scaleAnim.value
        }
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = {
                    scope.launch {
                        scaleAnim.animateTo(scale, AppAnimations.pressSpring)
                    }
                    val released = tryAwaitRelease()
                    scope.launch {
                        scaleAnim.animateTo(1f, AppAnimations.pressSpring)
                    }
                    if (released) {
                        onClick()
                    }
                },
            )
        }
}

/**
 * 淡入效果
 * 用于文字和内容的入场动画
 */
@Composable
fun rememberFadeInAlpha(
    visible: Boolean,
    duration: Int = AppAnimations.FADE_IN_DURATION,
): Float {
    val alpha = remember { Animatable(0f) }
    
    LaunchedEffect(visible) {
        if (visible) {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = duration),
            )
        } else {
            alpha.snapTo(0f)
        }
    }
    
    return alpha.value
}

/**
 * 内容切换时的淡入效果
 */
@Composable
fun rememberContentFadeIn(
    key: Any?,
    duration: Int = AppAnimations.FADE_IN_DURATION,
): Float {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(key) {
        visible = false
        kotlinx.coroutines.delay(50)
        visible = true
    }
    
    return rememberFadeInAlpha(visible, duration)
}

/**
 * 淡入修饰符
 */
fun Modifier.fadeIn(alpha: Float): Modifier = this.graphicsLayer {
    this.alpha = alpha
}
