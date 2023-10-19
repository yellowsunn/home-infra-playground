package com.yellowsunn.example.application.port.`in`

import com.yellowsunn.example.application.port.`in`.dto.ProductPageCommandResult
import com.yellowsunn.example.common.model.Page
import reactor.core.publisher.Mono

interface ProductUseCase {
    fun getProducts(page: Int, size: Int): Mono<Page<ProductPageCommandResult>>
}
