package com.yellowsunn.example.adapter.out.http

import com.yellowsunn.example.application.port.out.dto.PageCommand
import com.yellowsunn.example.common.model.Page
import com.yellowsunn.example.domain.Product
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.reactive.function.client.WebClient
import reactor.test.StepVerifier

class ProductHttpClientTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var sut: ProductHttpClient

    @BeforeEach
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val webClient: WebClient = WebClient.builder()
            .baseUrl(mockWebServer.url("").toString())
            .defaultHeaders { headers ->
                headers[HttpHeaders.ACCEPT] = APPLICATION_JSON_VALUE
                headers[CONTENT_TYPE] = APPLICATION_JSON_VALUE
            }
            .build()

        sut = ProductHttpClient(webClient = webClient)
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `find product page`() {
        // given
        val productsJson: String = ClassPathResource("mock/json/products.json")
            .getContentAsString(Charsets.UTF_8)

        mockWebServer.enqueue(
            MockResponse()
                .setHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .setBody(productsJson),
        )

        // when
        val stepVerifier = sut.findProducts(PageCommand(page = 2, size = 10))
            .`as` { StepVerifier.create(it) }

        // then
        stepVerifier
            .expectNextMatches { it: Page<Product> ->
                assertThat(it.contents).hasSize(10)
                assertThat(it.page).isEqualTo(2)
                assertThat(it.totalElements).isEqualTo(100)
                assertThat(it.totalPages).isEqualTo(10)
                true
            }
            .verifyComplete()
    }
}
