package top.astrasolis.jotter.platform

import okio.Path

/**
 * Android 平台目录选择器实现
 * 注意：Android 上需要使用 SAF (Storage Access Framework)
 * 当前实现为简化版，后续可以扩展为完整的 SAF 实现
 */
actual class DirectoryPicker actual constructor() {
    
    actual suspend fun pickDirectory(initialPath: Path?): Path? {
        // TODO: 实现 SAF 目录选择
        // 当前返回 null，使用默认目录
        return null
    }
    
    actual fun isSupported(): Boolean {
        // Android 上支持目录选择，但需要 Activity 上下文
        // 简化实现暂时返回 false，使用默认目录
        return false
    }
}
