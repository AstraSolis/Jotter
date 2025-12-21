package top.astrasolis.jotter.platform

import android.content.Context
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath as okioToPath
import okio.buffer
import okio.use

/**
 * Android 平台文件系统实现
 */
actual class PlatformFileSystem actual constructor() {
    private val fs = FileSystem.SYSTEM
    
    actual fun getAppConfigDir(): Path {
        // Android 上使用应用私有目录
        // 注意：实际使用时需要通过 ContextProvider 获取 Context
        val context = ContextProvider.context
        return context.filesDir.absolutePath.okioToPath() / "config"
    }
    
    actual fun getDefaultDataDir(): Path {
        // 默认使用应用私有文件目录
        val context = ContextProvider.context
        return context.filesDir.absolutePath.okioToPath() / "data"
    }
    
    actual fun exists(path: Path): Boolean {
        return fs.exists(path)
    }
    
    actual fun isDirectory(path: Path): Boolean {
        return fs.metadataOrNull(path)?.isDirectory == true
    }
    
    actual fun createDirectories(path: Path) {
        fs.createDirectories(path)
    }
    
    actual fun readText(path: Path): String? {
        if (!fs.exists(path)) return null
        return fs.source(path).buffer().use { it.readUtf8() }
    }
    
    actual fun writeText(path: Path, content: String) {
        path.parent?.let { parent ->
            if (!fs.exists(parent)) {
                fs.createDirectories(parent)
            }
        }
        fs.sink(path).buffer().use { it.writeUtf8(content) }
    }
    
    actual fun delete(path: Path): Boolean {
        return try {
            fs.delete(path)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    actual fun deleteRecursively(path: Path): Boolean {
        return try {
            fs.deleteRecursively(path)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    actual fun list(path: Path): List<Path> {
        return if (isDirectory(path)) {
            fs.list(path)
        } else {
            emptyList()
        }
    }
    
    actual fun copy(source: Path, destination: Path) {
        destination.parent?.let { parent ->
            if (!fs.exists(parent)) {
                fs.createDirectories(parent)
            }
        }
        fs.copy(source, destination)
    }
    
    actual fun copyDirectory(source: Path, destination: Path) {
        if (!isDirectory(source)) {
            copy(source, destination)
            return
        }
        
        createDirectories(destination)
        
        list(source).forEach { child ->
            val childName = child.name
            val destChild = destination / childName
            if (isDirectory(child)) {
                copyDirectory(child, destChild)
            } else {
                copy(child, destChild)
            }
        }
    }
    
    actual fun move(source: Path, destination: Path) {
        destination.parent?.let { parent ->
            if (!fs.exists(parent)) {
                fs.createDirectories(parent)
            }
        }
        fs.atomicMove(source, destination)
    }
}

actual fun String.toPath(): Path = this.okioToPath()

/**
 * 用于在 Android 平台获取 Context
 * 需要在 Application.onCreate 中初始化
 */
object ContextProvider {
    lateinit var context: Context
        private set
    
    fun init(context: Context) {
        this.context = context.applicationContext
    }
}
