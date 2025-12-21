package top.astrasolis.jotter.utils

import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

/**
 * iOS 平台获取当前时间
 */
actual fun currentTimeMillis(): Long = (NSDate().timeIntervalSince1970 * 1000).toLong()
