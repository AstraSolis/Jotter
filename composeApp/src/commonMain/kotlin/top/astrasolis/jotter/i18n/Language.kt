package top.astrasolis.jotter.i18n

import kotlinx.serialization.Serializable

/**
 * 支持的语言枚举
 */
@Serializable
enum class Language(val code: String, val displayName: String) {
    /** 简体中文 */
    CHINESE("zh-CN", "简体中文"),
    /** 英文 */
    ENGLISH("en", "English");
    
    companion object {
        /**
         * 根据语言代码获取语言枚举
         * 如果未找到匹配项，返回中文作为默认值
         */
        fun fromCode(code: String): Language = 
            entries.find { it.code == code } ?: CHINESE
    }
}
