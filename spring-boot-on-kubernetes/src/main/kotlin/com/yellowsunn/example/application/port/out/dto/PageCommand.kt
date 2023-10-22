package com.yellowsunn.example.application.port.out.dto

data class PageCommand(
    val page: Int,
    val size: Int,
) {
    fun getSkip(): Long {
        return page.toLong() * size
    }
}
