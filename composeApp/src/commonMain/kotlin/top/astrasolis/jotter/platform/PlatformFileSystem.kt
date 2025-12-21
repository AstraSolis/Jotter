package top.astrasolis.jotter.platform

import okio.Path

/**
 * 跨平台文件系统接口
 * 提供统一的文件操作 API
 */
expect class PlatformFileSystem() {
    /**
     * 获取应用私有配置目录
     * 用于存储 app_state.json 等应用级配置
     */
    fun getAppConfigDir(): Path
    
    /**
     * 获取默认数据目录
     * 当用户未选择存储位置时使用
     */
    fun getDefaultDataDir(): Path
    
    /**
     * 检查路径是否存在
     */
    fun exists(path: Path): Boolean
    
    /**
     * 检查路径是否是目录
     */
    fun isDirectory(path: Path): Boolean
    
    /**
     * 创建目录（包括父目录）
     */
    fun createDirectories(path: Path)
    
    /**
     * 读取文件文本内容
     * @return 文件内容，如果文件不存在返回 null
     */
    fun readText(path: Path): String?
    
    /**
     * 写入文本到文件
     * 如果父目录不存在会自动创建
     */
    fun writeText(path: Path, content: String)
    
    /**
     * 删除文件或空目录
     * @return 是否删除成功
     */
    fun delete(path: Path): Boolean
    
    /**
     * 递归删除目录及其内容
     * @return 是否删除成功
     */
    fun deleteRecursively(path: Path): Boolean
    
    /**
     * 列出目录内容
     * @return 子路径列表，如果不是目录返回空列表
     */
    fun list(path: Path): List<Path>
    
    /**
     * 复制文件
     */
    fun copy(source: Path, destination: Path)
    
    /**
     * 递归复制目录
     */
    fun copyDirectory(source: Path, destination: Path)
    
    /**
     * 移动文件或目录
     */
    fun move(source: Path, destination: Path)
}

/**
 * 将字符串路径转换为 okio Path
 */
expect fun String.toPath(): Path
