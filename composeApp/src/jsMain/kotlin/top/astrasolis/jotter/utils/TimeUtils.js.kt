package top.astrasolis.jotter.utils

import kotlin.js.Date

/**
 * JS 平台获取当前时间
 */
actual fun currentTimeMillis(): Long = Date.now().toLong()
