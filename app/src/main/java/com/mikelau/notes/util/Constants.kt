package com.mikelau.notes.util

const val BASE_URL = "https://demo5532971.mockable.io/"

fun getColorPriority(x: Int): String {
    return when(x) {
        1, 5, 9 -> "#f7cf52"
        2, 6, 10 -> "#ff7eb9"
        3, 7 -> "#529af7"
        4, 8 -> "#37c252"
        else -> "#f79d52"
    }
}