package top.astrasolis.jotter.data

/**
 * 一言数据模型
 */
data class Quote(
    val content: String,
    val source: String? = null,
)

/**
 * 一言数据提供者接口
 * 预留接口，后期可接入 API
 */
interface QuoteProvider {
    /**
     * 获取今日一言
     */
    fun getDailyQuote(): Quote
}

/**
 * 本地一言提供者（默认实现）
 * 使用简单的随机选择，后期可接入网络 API
 */
class LocalQuoteProvider : QuoteProvider {
    
    private val quotes = listOf(
        Quote("生活不是等待风暴过去，而是学会在雨中跳舞。"),
        Quote("每一个不曾起舞的日子，都是对生命的辜负。", "尼采"),
        Quote("种一棵树最好的时间是十年前，其次是现在。"),
        Quote("不要等待机会，而要创造机会。"),
        Quote("今天的努力是明天的收获。"),
        Quote("把每一天当作生命中最后一天去生活。"),
        Quote("唯一的限制是你自己的想象力。"),
        Quote("行动是治愈恐惧的良药。"),
        Quote("成功不是终点，失败也不是致命的。"),
        Quote("简单是复杂的最高境界。"),
    )
    
    override fun getDailyQuote(): Quote {
        // 使用简单的索引选择（后期可以基于日期）
        return quotes.random()
    }
}
