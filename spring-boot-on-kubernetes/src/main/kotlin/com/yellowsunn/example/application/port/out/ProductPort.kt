package com.yellowsunn.example.application.port.out

import com.yellowsunn.example.application.port.out.dto.PageCommand
import com.yellowsunn.example.common.model.Page
import com.yellowsunn.example.domain.Product
import reactor.core.publisher.Mono

interface ProductPort {

    fun findProducts(pageCommand: PageCommand): Mono<Page<Product>>
}
