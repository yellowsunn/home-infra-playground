package com.yellowsunn.example.adapter.out.http

import com.yellowsunn.example.adapter.out.http.converter.ProductPageHttpConverter
import com.yellowsunn.example.adapter.out.http.dto.ProductPageHttpResponse
import com.yellowsunn.example.application.port.out.ProductPort
import com.yellowsunn.example.application.port.out.dto.PageCommand
import com.yellowsunn.example.common.model.Page
import com.yellowsunn.example.domain.Product
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono

@Component
class ProductHttpClient(
    @Qualifier("dummyJson") private val webClient: WebClient,
) : ProductPort {
    override fun findProducts(pageCommand: PageCommand): Mono<Page<Product>> {
        val uri = UriComponentsBuilder.newInstance()
            .path("/products")
            .queryParam("skip", pageCommand.getSkip())
            .queryParam("limit", pageCommand.size)
            .build()
            .toUriString()

        return webClient.get()
            .uri(uri)
            .retrieve()
            .bodyToMono<ProductPageHttpResponse>()
            .map {
                Page(
                    contents = ProductPageHttpConverter.CONVERTER.convertToProducts(it.products),
                    page = it.calcPage(pageCommand.size),
                    size = pageCommand.size,
                    numberOfElements = it.getNumberOfElements(),
                    totalPages = it.calcTotalPages(pageCommand.size),
                    totalElements = it.total,
                )
            }
    }
}
