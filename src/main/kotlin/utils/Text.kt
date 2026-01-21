package utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

object Text {
    fun green(msg: String) = Component.text(msg, NamedTextColor.GREEN)
    fun blue(msg: String) = Component.text(msg, NamedTextColor.BLUE)
    fun red(msg: String) = Component.text(msg, NamedTextColor.RED)
    fun yellow(msg: String) = Component.text(msg, NamedTextColor.YELLOW)
    fun gray(msg: String) = Component.text(msg, NamedTextColor.GRAY)
    fun purple(msg: String) = Component.text(msg, NamedTextColor.LIGHT_PURPLE)
    fun darkPurple(msg: String) = Component.text(msg, NamedTextColor.DARK_PURPLE)
    fun aqua(msg: String) = Component.text(msg, NamedTextColor.AQUA)
    fun white(msg: String) = Component.text(msg, NamedTextColor.WHITE)
}