package top.astrasolis.jotter.utils

/**
 * 声明 JavaScript Date.now() 函数
 */
private external object JsDate {
    fun now(): Double
}

/**
 * WasmJS 平台获取当前时间
 */
actual fun currentTimeMillis(): Long = JsDate.now().toLong()
