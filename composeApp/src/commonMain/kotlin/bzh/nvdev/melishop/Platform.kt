package bzh.nvdev.melishop

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform