package top.astrasolis.jotter.platform

import okio.Path

/**
 * WasmJs (Web) 平台目录选择器
 * Web 平台不支持本地目录选择
 */
actual class DirectoryPicker actual constructor() {
    
    actual suspend fun pickDirectory(initialPath: Path?): Path? {
        return null
    }
    
    actual fun isSupported(): Boolean = false
}
