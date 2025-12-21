package top.astrasolis.jotter.platform

import okio.Path

/**
 * 跨平台目录选择器
 * 允许用户选择数据存储目录
 */
expect class DirectoryPicker() {
    /**
     * 打开目录选择对话框
     * @param initialPath 初始路径，可能被平台忽略
     * @return 选中的目录路径，如果用户取消返回 null
     */
    suspend fun pickDirectory(initialPath: Path? = null): Path?
    
    /**
     * 检查当前平台是否支持目录选择
     */
    fun isSupported(): Boolean
}
