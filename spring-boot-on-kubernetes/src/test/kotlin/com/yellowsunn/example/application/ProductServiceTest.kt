package com.yellowsunn.example.application

import com.navercorp.fixturemonkey.kotlin.giveMe
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.setExp
import com.yellowsunn.example.application.port.`in`.dto.ProductPageCommandResult
import com.yellowsunn.example.application.port.out.ProductPort
import com.yellowsunn.example.common.model.Page
import com.yellowsunn.example.domain.Product
import com.yellowsunn.example.fixtureMonkey
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import reactor.kotlin.core.publisher.toMono
import reactor.test.StepVerifier

class ProductServiceTest {

    private val productPort: ProductPort = mockk()

    private val sut: ProductService = ProductService(
        productPort = productPort,
    )

    @Test
    fun getProducts() {
        // given
        val product: List<Product> = fixtureMonkey.giveMe<Product>(5)
        val productPage: Page<Product> = fixtureMonkey.giveMeBuilder<Page<Product>>()
            .setExp(Page<Product>::contents, product)
            .sample()
        every { productPort.findProducts(any()) } returns productPage.toMono()

        // when
        val stepVerifier = sut.getProducts(page = 1, size = 10)
            .`as` { StepVerifier.create(it) }

        // then
        stepVerifier
            .assertNext { it: Page<ProductPageCommandResult> ->
                assertThat(it.contents).hasSameSizeAs(productPage.contents)
                assertThat(it.page).isEqualTo(productPage.page)
                assertThat(it.size).isEqualTo(productPage.size)
            }
            .verifyComplete()
    }
}
