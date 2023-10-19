package com.yellowsunn.example.adapter.`in`.graphql

import com.yellowsunn.example.adapter.`in`.graphql.converter.ProductPageConverter
import com.yellowsunn.example.adapter.`in`.graphql.dto.ProductPageResponse
import com.yellowsunn.example.application.port.`in`.ProductUseCase
import com.yellowsunn.example.common.model.Page
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Mono

@Controller
class ProductController(
    private val productUseCase: ProductUseCase,
) {

    companion object {
        private const val DEFAULT_PAGE_SIZE = 10
    }

    @QueryMapping
    fun findProducts(
        @Argument page: Int?,
        @Argument size: Int?,
    ): Mono<Page<ProductPageResponse>> {
        return productUseCase.getProducts(
            page = page ?: 1,
            size = size ?: DEFAULT_PAGE_SIZE,
        ).map {
            ProductPageConverter.CONVERTER.convertToResponse(it)
        }
    }
}
