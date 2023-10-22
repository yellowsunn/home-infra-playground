package com.yellowsunn.example.adapter.`in`.graphql

import com.navercorp.fixturemonkey.kotlin.giveMe
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.setExp
import com.yellowsunn.example.adapter.`in`.graphql.dto.ProductPageResponse
import com.yellowsunn.example.application.port.`in`.ProductUseCase
import com.yellowsunn.example.application.port.`in`.dto.ProductPageCommandResult
import com.yellowsunn.example.common.model.Page
import com.yellowsunn.example.fixtureMonkey
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import reactor.kotlin.core.publisher.toMono
import reactor.test.StepVerifier

class ProductControllerTest {

    private val productUseCase: ProductUseCase = mockk()
    private val productController = ProductController(productUseCase = productUseCase)

    @ParameterizedTest(name = "상품 페이지 목록 조회")
    @CsvSource(
        value = [
            "0, 10, 0, 10",
            "-1, 0, 0, 1",
            "null, null, 0, 10",
        ],
        nullValues = ["null"],
    )
    fun findProducts(page: Int?, size: Int?, expectedPage: Int, expectedSize: Int) {
        // given
        val commandResult: Page<ProductPageCommandResult> =
            fixtureMonkey.giveMeBuilder<Page<ProductPageCommandResult>>()
                .setExp(Page<ProductPageCommandResult>::contents, fixtureMonkey.giveMe<ProductPageCommandResult>(5))
                .sample()
        every { productUseCase.getProducts(page = any(), size = any()) } returns commandResult.toMono()

        // when
        val stepVerifier = productController.findProducts(page = page, size = size)
            .`as` { StepVerifier.create(it) }

        // then
        stepVerifier
            .assertNext { it: Page<ProductPageResponse> ->
                verify(exactly = 1) { productUseCase.getProducts(page = expectedPage, size = expectedSize) }
                assertThat(it.contents).hasSameSizeAs(commandResult.contents)
                assertThat(it.page).isEqualTo(commandResult.page)
                assertThat(it.size).isEqualTo(commandResult.size)
            }
            .verifyComplete()
    }
}
