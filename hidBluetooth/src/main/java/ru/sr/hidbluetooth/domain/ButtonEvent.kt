package ru.sr.hidbluetooth.domain

enum class ButtonEvent(val code: Int) {
    ENTER(40),
    BACK(41),
    HOME(62),
    MUTE(65),
    VOLUME_DOWN(66),
    VOLUME_UP(67),
    RIGHT(79),
    LEFT(80),
    DOWN(81),
    UP(82)
}