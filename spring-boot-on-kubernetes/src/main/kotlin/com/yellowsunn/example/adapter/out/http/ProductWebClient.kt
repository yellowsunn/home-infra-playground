package com.yellowsunn.example.adapter.out.http

import com.yellowsunn.example.adapter.out.http.converter.ProductPageHttpConverter
import com.yellowsunn.example.adapter.out.http.dto.ProductPageHttpResponse
import com.yellowsunn.example.application.port.out.ProductPort
import com.yellowsunn.example.application.port.out.dto.PageRequest
import com.yellowsunn.example.common.model.Page
import com.yellowsunn.example.domain.Product
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono

@Component
class ProductWebClient : ProductPort {

    private val webClient = WebClient.builder()
        .baseUrl("https://dummyjson.com")
        .build()

    override fun findProducts(pageRequest: PageRequest): Mono<Page<Product>> {
        val uri = UriComponentsBuilder.newInstance()
            .path("/products")
            .queryParam("skip", pageRequest.getSkip())
            .queryParam("limit", pageRequest.size)
            .build()
            .toUriString()

        return webClient.get()
            .uri(uri)
            .retrieve()
            .bodyToMono<ProductPageHttpResponse>()
            .map {
                Page(
                    contents = ProductPageHttpConverter.CONVERTER.convertToProducts(it.products),
                    page = it.getPage(pageRequest.size) + 1,
                    size = pageRequest.size,
                    numberOfElements = it.getNumberOfElements(),
                    totalPages = it.getTotalPages(pageRequest.size),
                    totalElements = it.total,
                )
            }
    }
}
