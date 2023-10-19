package com.yellowsunn.example.application.port.`in`.dto

import java.math.BigDecimal

data class ProductPageCommandResult(
    val id: Long,
    val title: String,
    val description: String,
    val price: BigDecimal,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Long,
    val brand: String,
    val category: String,
    val thumbnail: String,
    val images: List<String>,
)
