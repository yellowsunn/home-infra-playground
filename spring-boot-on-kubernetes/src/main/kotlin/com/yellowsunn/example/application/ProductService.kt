package com.yellowsunn.example.application

import com.yellowsunn.example.application.port.`in`.ProductUseCase
import com.yellowsunn.example.application.port.`in`.converter.ProductCommandPageConverter
import com.yellowsunn.example.application.port.`in`.dto.ProductPageCommandResult
import com.yellowsunn.example.application.port.out.ProductPort
import com.yellowsunn.example.application.port.out.dto.PageCommand
import com.yellowsunn.example.common.model.Page
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ProductService(
    private val productPort: ProductPort,
) : ProductUseCase {
    override fun getProducts(page: Int, size: Int): Mono<Page<ProductPageCommandResult>> {
        return productPort.findProducts(
            PageCommand(page = page, size = size),
        ).map {
            ProductCommandPageConverter.CONVERTER.convertToCommandResult(it)
        }
    }
}
