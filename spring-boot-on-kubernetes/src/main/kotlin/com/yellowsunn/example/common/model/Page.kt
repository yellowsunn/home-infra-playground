package com.yellowsunn.example.common.model

data class Page<T>(
    val contents: List<T>,
    val page: Int,
    val size: Int,
    val numberOfElements: Long,
    val totalPages: Int,
    val totalElements: Long,
)
