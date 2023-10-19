package com.yellowsunn.example.adapter.out.http.dto

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.setExp
import com.navercorp.fixturemonkey.kotlin.sizeExp
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ProductPageHttpResponseTest {

    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    @Test
    fun `calculate page`() {
        // given
        val skip = 50
        val size = 10
        val response = fixtureMonkey.giveMeBuilder<ProductPageHttpResponse>()
            .setExp(ProductPageHttpResponse::skip, skip)
            .sample()

        // when
        val page: Int = response.calcPage(size)

        // then
        assertThat(page).isEqualTo(6)
    }

    @Test
    fun getNumberOfElements() {
        // given
        val productSize = 8
        val response = fixtureMonkey.giveMeBuilder<ProductPageHttpResponse>()
            .sizeExp(ProductPageHttpResponse::products, productSize)
            .sample()

        // when
        val numberOfElements: Long = response.getNumberOfElements()

        // then
        assertThat(numberOfElements).isEqualTo(8L)
    }

    @Test
    fun `calculate total pages`() {
        // given
        val size = 10
        val total = 128L
        val response: ProductPageHttpResponse = fixtureMonkey.giveMeBuilder<ProductPageHttpResponse>()
            .setExp(ProductPageHttpResponse::total, total)
            .sample()

        // when
        val totalPages: Int = response.calcTotalPages(size)

        // then
        assertThat(totalPages).isEqualTo(13)
    }
}
