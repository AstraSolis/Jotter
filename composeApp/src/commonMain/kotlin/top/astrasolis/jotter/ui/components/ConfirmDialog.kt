package top.astrasolis.jotter.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import top.astrasolis.jotter.i18n.strings
import top.astrasolis.jotter.ui.theme.AppTheme
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.extra.SuperDialog

/**
 * 确认对话框组件
 * 基于 Miuix SuperDialog 封装，用于需要用户确认的操作
 */
@Composable
fun ConfirmDialog(
    show: MutableState<Boolean>,
    title: String,
    message: String,
    confirmText: String? = null,
    cancelText: String? = null,
    onConfirm: () -> Unit,
    onCancel: () -> Unit = {},
    isDangerous: Boolean = false,
) {
    val actualConfirmText = confirmText ?: strings.confirm
    val actualCancelText = cancelText ?: strings.cancel
    
    SuperDialog(
        title = title,
        summary = message,
        show = show,
        onDismissRequest = {
            show.value = false
            onCancel()
        },
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // 取消按钮
            TextButton(
                text = actualCancelText,
                onClick = {
                    show.value = false
                    onCancel()
                },
                modifier = Modifier.weight(1f),
            )
            
            Spacer(modifier = Modifier.width(AppTheme.spacing.md))
            
            // 确认按钮
            Button(
                onClick = {
                    show.value = false
                    onConfirm()
                },
                modifier = Modifier.weight(1f),
                colors = if (isDangerous) {
                    ButtonDefaults.buttonColors()
                } else {
                    ButtonDefaults.buttonColors()
                },
            ) {
                Text(text = actualConfirmText)
            }
        }
    }
}

/**
 * 删除确认对话框
 * 预设为危险操作样式
 */
@Composable
fun DeleteConfirmDialog(
    show: MutableState<Boolean>,
    itemName: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit = {},
) {
    ConfirmDialog(
        show = show,
        title = strings.dialogDeleteTitle,
        message = strings.dialogDeleteMessage(itemName),
        confirmText = strings.dialogDeleteConfirm,
        cancelText = strings.cancel,
        onConfirm = onConfirm,
        onCancel = onCancel,
        isDangerous = true,
    )
}
