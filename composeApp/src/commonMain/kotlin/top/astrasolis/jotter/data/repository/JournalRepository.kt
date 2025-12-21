package top.astrasolis.jotter.data.repository

import kotlinx.datetime.LocalDate
import okio.Path
import top.astrasolis.jotter.data.model.Journal
import top.astrasolis.jotter.data.model.JournalMetadata
import top.astrasolis.jotter.platform.PlatformFileSystem
import top.astrasolis.jotter.utils.TimeUtils

/**
 * 日记仓库
 * 日记以 Markdown 格式存储，按年/月/日期组织
 */
class JournalRepository(
    private val fileSystem: PlatformFileSystem,
    private val getDataPath: () -> Path,
) {
    private val journalsDir: Path
        get() = getDataPath() / "journals"
    
    /**
     * 获取日记文件路径
     * 格式: journals/2025/12/2025-12-21.md
     */
    private fun journalPath(date: LocalDate): Path {
        val year = date.year.toString()
        val month = date.monthNumber.toString().padStart(2, '0')
        val fileName = "$date.md"
        return journalsDir / year / month / fileName
    }
    
    /**
     * 获取指定日期的日记
     */
    fun getJournal(date: LocalDate): Journal? {
        val path = journalPath(date)
        val content = fileSystem.readText(path) ?: return null
        return parseMarkdown(date, content)
    }
    
    /**
     * 保存日记
     */
    fun saveJournal(journal: Journal) {
        val path = journalPath(journal.date)
        val content = toMarkdown(journal)
        fileSystem.writeText(path, content)
    }
    
    /**
     * 删除日记
     */
    fun deleteJournal(date: LocalDate): Boolean {
        val path = journalPath(date)
        return fileSystem.delete(path)
    }
    
    /**
     * 获取指定年月的日记列表
     */
    fun listJournals(year: Int, month: Int): List<JournalMetadata> {
        val monthDir = journalsDir / year.toString() / month.toString().padStart(2, '0')
        
        if (!fileSystem.exists(monthDir)) return emptyList()
        
        return fileSystem.list(monthDir)
            .filter { it.name.endsWith(".md") }
            .mapNotNull { path ->
                val dateStr = path.name.removeSuffix(".md")
                try {
                    val date = LocalDate.parse(dateStr)
                    val content = fileSystem.readText(path) ?: return@mapNotNull null
                    val journal = parseMarkdown(date, content)
                    journal?.let {
                        JournalMetadata(
                            date = it.date,
                            title = it.title,
                            mood = it.mood,
                            weather = it.weather,
                            updatedAt = it.updatedAt,
                        )
                    }
                } catch (e: Exception) {
                    null
                }
            }
            .sortedByDescending { it.date }
    }
    
    /**
     * 获取所有年份
     */
    fun getYears(): List<Int> {
        if (!fileSystem.exists(journalsDir)) return emptyList()
        
        return fileSystem.list(journalsDir)
            .mapNotNull { path ->
                path.name.toIntOrNull()
            }
            .sortedDescending()
    }
    
    /**
     * 搜索日记
     * 简单的全文搜索
     */
    fun searchJournals(query: String): List<JournalMetadata> {
        val results = mutableListOf<JournalMetadata>()
        
        getYears().forEach { year ->
            (1..12).forEach { month ->
                listJournals(year, month)
                    .filter { metadata ->
                        val journal = getJournal(metadata.date)
                        journal?.let {
                            it.title.contains(query, ignoreCase = true) ||
                            it.content.contains(query, ignoreCase = true)
                        } ?: false
                    }
                    .let { results.addAll(it) }
            }
        }
        
        return results.sortedByDescending { it.date }
    }
    
    /**
     * 检查指定日期是否有日记
     */
    fun hasJournal(date: LocalDate): Boolean {
        return fileSystem.exists(journalPath(date))
    }
    
    /**
     * 获取今日日记
     */
    fun getTodayJournal(): Journal? {
        return getJournal(TimeUtils.today())
    }
    
    /**
     * 创建或更新今日日记
     */
    fun saveTodayJournal(title: String, content: String, mood: String? = null, weather: String? = null) {
        val currentTime = TimeUtils.now()
        val today = TimeUtils.today()
        
        val existing = getJournal(today)
        val journal = Journal(
            date = today,
            title = title,
            content = content,
            mood = mood,
            weather = weather,
            createdAt = existing?.createdAt ?: currentTime,
            updatedAt = currentTime,
        )
        saveJournal(journal)
    }
    
    /**
     * 将 Journal 转换为 Markdown
     */
    private fun toMarkdown(journal: Journal): String {
        val sb = StringBuilder()
        
        // YAML Front Matter
        sb.appendLine("---")
        sb.appendLine("date: ${journal.date}")
        sb.appendLine("createdAt: ${journal.createdAt}")
        sb.appendLine("updatedAt: ${journal.updatedAt}")
        journal.mood?.let { sb.appendLine("mood: $it") }
        journal.weather?.let { sb.appendLine("weather: $it") }
        sb.appendLine("---")
        sb.appendLine()
        
        // 标题
        sb.appendLine("# ${journal.title}")
        sb.appendLine()
        
        // 内容
        sb.append(journal.content)
        
        return sb.toString()
    }
    
    /**
     * 解析 Markdown 为 Journal
     */
    private fun parseMarkdown(date: LocalDate, content: String): Journal? {
        val lines = content.lines()
        
        // 解析 Front Matter
        var inFrontMatter = false
        var frontMatterEnd = 0
        val frontMatter = mutableMapOf<String, String>()
        
        for ((index, line) in lines.withIndex()) {
            when {
                line.trim() == "---" && !inFrontMatter -> {
                    inFrontMatter = true
                }
                line.trim() == "---" && inFrontMatter -> {
                    frontMatterEnd = index + 1
                    break
                }
                inFrontMatter && line.contains(":") -> {
                    val (key, value) = line.split(":", limit = 2)
                    frontMatter[key.trim()] = value.trim()
                }
            }
        }
        
        // 解析标题和内容
        val bodyLines = lines.drop(frontMatterEnd).dropWhile { it.isBlank() }
        val title = bodyLines.firstOrNull()?.removePrefix("#")?.trim() ?: "无标题"
        val bodyContent = bodyLines.drop(1).dropWhile { it.isBlank() }.joinToString("\n")
        
        val currentTime = TimeUtils.now()
        
        return Journal(
            date = date,
            title = title,
            content = bodyContent,
            mood = frontMatter["mood"],
            weather = frontMatter["weather"],
            createdAt = frontMatter["createdAt"]?.toLongOrNull() ?: currentTime,
            updatedAt = frontMatter["updatedAt"]?.toLongOrNull() ?: currentTime,
        )
    }
}
