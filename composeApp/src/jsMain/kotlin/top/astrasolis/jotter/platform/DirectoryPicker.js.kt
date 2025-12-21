package top.astrasolis.jotter.platform

import okio.Path

/**
 * JS (Web) 平台目录选择器
 * Web 平台不支持本地目录选择
 */
actual class DirectoryPicker actual constructor() {
    
    actual suspend fun pickDirectory(initialPath: Path?): Path? {
        // Web 不支持目录选择
        return null
    }
    
    actual fun isSupported(): Boolean = false
}
