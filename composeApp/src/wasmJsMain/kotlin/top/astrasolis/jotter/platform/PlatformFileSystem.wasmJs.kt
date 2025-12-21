package top.astrasolis.jotter.platform

import okio.Path
import okio.Path.Companion.toPath as okioToPath

/**
 * WasmJs (Web) 平台文件系统实现
 * 与 JS 版本相同，使用内存存储
 */
actual class PlatformFileSystem actual constructor() {
    private val memoryStorage = mutableMapOf<String, String>()
    private val directories = mutableSetOf<String>()
    
    actual fun getAppConfigDir(): Path {
        return "/jotter/config".okioToPath()
    }
    
    actual fun getDefaultDataDir(): Path {
        return "/jotter/data".okioToPath()
    }
    
    actual fun exists(path: Path): Boolean {
        val pathStr = path.toString()
        return memoryStorage.containsKey(pathStr) || directories.contains(pathStr)
    }
    
    actual fun isDirectory(path: Path): Boolean {
        return directories.contains(path.toString())
    }
    
    actual fun createDirectories(path: Path) {
        var current = path
        while (current.toString() != "/" && current.toString().isNotEmpty()) {
            directories.add(current.toString())
            current = current.parent ?: break
        }
    }
    
    actual fun readText(path: Path): String? {
        return memoryStorage[path.toString()]
    }
    
    actual fun writeText(path: Path, content: String) {
        path.parent?.let { createDirectories(it) }
        memoryStorage[path.toString()] = content
    }
    
    actual fun delete(path: Path): Boolean {
        val pathStr = path.toString()
        val wasFile = memoryStorage.remove(pathStr) != null
        val wasDir = directories.remove(pathStr)
        return wasFile || wasDir
    }
    
    actual fun deleteRecursively(path: Path): Boolean {
        val pathStr = path.toString()
        val keysToRemove = memoryStorage.keys.filter { it.startsWith(pathStr) }
        keysToRemove.forEach { memoryStorage.remove(it) }
        
        val dirsToRemove = directories.filter { it.startsWith(pathStr) }
        dirsToRemove.forEach { directories.remove(it) }
        
        return keysToRemove.isNotEmpty() || dirsToRemove.isNotEmpty()
    }
    
    actual fun list(path: Path): List<Path> {
        val pathStr = path.toString()
        val prefix = if (pathStr.endsWith("/")) pathStr else "$pathStr/"
        
        val children = mutableSetOf<String>()
        
        memoryStorage.keys
            .filter { it.startsWith(prefix) }
            .forEach { key ->
                val relativePath = key.removePrefix(prefix)
                val firstSegment = relativePath.split("/").first()
                children.add("$prefix$firstSegment")
            }
        
        directories
            .filter { it.startsWith(prefix) && it != pathStr }
            .forEach { dir ->
                val relativePath = dir.removePrefix(prefix)
                val firstSegment = relativePath.split("/").first()
                children.add("$prefix$firstSegment")
            }
        
        return children.map { it.okioToPath() }
    }
    
    actual fun copy(source: Path, destination: Path) {
        val content = memoryStorage[source.toString()] ?: return
        writeText(destination, content)
    }
    
    actual fun copyDirectory(source: Path, destination: Path) {
        val sourceStr = source.toString()
        val destStr = destination.toString()
        
        memoryStorage.keys.filter { it.startsWith(sourceStr) }.forEach { key ->
            val newKey = key.replaceFirst(sourceStr, destStr)
            memoryStorage[newKey] = memoryStorage[key]!!
        }
        
        directories.filter { it.startsWith(sourceStr) }.forEach { dir ->
            val newDir = dir.replaceFirst(sourceStr, destStr)
            directories.add(newDir)
        }
    }
    
    actual fun move(source: Path, destination: Path) {
        copyDirectory(source, destination)
        deleteRecursively(source)
    }
}

actual fun String.toPath(): Path = this.okioToPath()
