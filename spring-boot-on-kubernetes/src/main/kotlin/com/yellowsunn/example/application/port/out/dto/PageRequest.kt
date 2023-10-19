package com.yellowsunn.example.application.port.out.dto

class PageRequest(
    val page: Int,
    val size: Int,
) {
    fun getSkip(): Long {
        return (page * size).toLong()
    }
}
