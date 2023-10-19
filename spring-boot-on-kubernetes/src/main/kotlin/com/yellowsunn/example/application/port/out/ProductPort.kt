package com.yellowsunn.example.application.port.out

import com.yellowsunn.example.application.port.out.dto.PageRequest
import com.yellowsunn.example.common.model.Page
import com.yellowsunn.example.domain.Product
import reactor.core.publisher.Mono

interface ProductPort {

    fun findProducts(pageRequest: PageRequest): Mono<Page<Product>>
}
