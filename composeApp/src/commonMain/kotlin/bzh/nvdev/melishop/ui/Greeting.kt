package bzh.nvdev.melishop.ui

import bzh.nvdev.melishop.getPlatform

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}