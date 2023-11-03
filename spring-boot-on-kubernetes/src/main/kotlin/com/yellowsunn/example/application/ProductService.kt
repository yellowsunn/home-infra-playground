package com.yellowsunn.example.application

import com.yellowsunn.example.application.port.`in`.ProductUseCase
import com.yellowsunn.example.application.port.`in`.converter.ProductCommandPageConverter
import com.yellowsunn.example.application.port.`in`.dto.ProductPageCommandResult
import com.yellowsunn.example.application.port.out.ProductPort
import com.yellowsunn.example.application.port.out.dto.PageCommand
import com.yellowsunn.example.common.model.Page
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ProductService(
    private val productPort: ProductPort,
) : ProductUseCase {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun getProducts(page: Int, size: Int): Mono<Page<ProductPageCommandResult>> {
        logger.info("Get products. page={}, size={}", page, size)

        return productPort.findProducts(
            PageCommand(page = page, size = size),
        ).map {
            ProductCommandPageConverter.CONVERTER.convertToCommandResult(it)
        }
    }
}
