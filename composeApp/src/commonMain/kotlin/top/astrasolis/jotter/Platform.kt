package top.astrasolis.jotter

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform