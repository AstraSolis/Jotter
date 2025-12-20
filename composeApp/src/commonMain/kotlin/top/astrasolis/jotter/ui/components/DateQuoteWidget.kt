package top.astrasolis.jotter.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import top.astrasolis.jotter.ui.theme.AppTheme
import top.astrasolis.jotter.ui.theme.fadeIn
import top.astrasolis.jotter.ui.theme.rememberContentFadeIn
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 一言展示组件
 * 大字居中显示，带淡入动画效果
 */
@Composable
fun QuoteDisplayWidget(
    quote: String,
    quoteSource: String? = null,
    modifier: Modifier = Modifier,
) {
    // 内容变化时的淡入动画
    val fadeAlpha = rememberContentFadeIn(key = quote)
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fadeIn(fadeAlpha),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // 主要一言文字 - 大字居中
        Text(
            text = quote,
            style = MiuixTheme.textStyles.headline1.copy(
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
            ),
            color = MiuixTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        
        // 来源（如有）
        if (quoteSource != null) {
            Spacer(modifier = Modifier.height(AppTheme.spacing.lg))
            Text(
                text = "—— $quoteSource",
                style = MiuixTheme.textStyles.body1,
                color = MiuixTheme.colorScheme.onBackgroundVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

/**
 * 日期展示组件
 * 放大字体，左上角显示
 */
@Composable
fun DateDisplayWidget(
    date: String,
    dayOfWeek: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = "$date $dayOfWeek",
            style = MiuixTheme.textStyles.title3,
            color = MiuixTheme.colorScheme.onBackgroundVariant,
        )
    }
}
