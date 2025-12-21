package top.astrasolis.jotter.platform

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.Path
import java.io.File
import javax.swing.JFileChooser

/**
 * JVM (Desktop) 平台目录选择器实现
 * 使用 Swing JFileChooser
 */
actual class DirectoryPicker actual constructor() {
    
    actual suspend fun pickDirectory(initialPath: Path?): Path? = withContext(Dispatchers.IO) {
        val fileChooser = JFileChooser().apply {
            fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            dialogTitle = "选择数据存储位置"
            approveButtonText = "选择"
            
            // 设置初始目录
            initialPath?.let { path ->
                currentDirectory = File(path.toString())
            }
        }
        
        val result = fileChooser.showOpenDialog(null)
        
        if (result == JFileChooser.APPROVE_OPTION) {
            fileChooser.selectedFile.absolutePath.toPath()
        } else {
            null
        }
    }
    
    actual fun isSupported(): Boolean = true
}
