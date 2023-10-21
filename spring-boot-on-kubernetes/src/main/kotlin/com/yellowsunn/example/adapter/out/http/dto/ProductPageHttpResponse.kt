package com.yellowsunn.example.adapter.out.http.dto

import java.math.BigDecimal
import kotlin.math.ceil

class ProductPageHttpResponse(
    val products: List<Product>,
    val total: Long,
    val skip: Long,
) {
    data class Product(
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

    fun calcPage(size: Int): Int {
        return (skip / size).toInt()
    }

    fun getNumberOfElements(): Long {
        return products.size.toLong()
    }

    fun calcTotalPages(size: Int): Int {
        return ceil(total.toDouble() / size).toInt()
    }
}
