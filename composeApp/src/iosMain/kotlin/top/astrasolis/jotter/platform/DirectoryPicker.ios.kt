package top.astrasolis.jotter.platform

import okio.Path

/**
 * iOS 平台目录选择器实现
 * iOS 通常不允许用户选择任意目录，使用应用沙盒目录
 */
actual class DirectoryPicker actual constructor() {
    
    actual suspend fun pickDirectory(initialPath: Path?): Path? {
        // iOS 不支持任意目录选择，返回 null 使用默认目录
        return null
    }
    
    actual fun isSupported(): Boolean {
        // iOS 不支持自定义目录选择
        return false
    }
}
