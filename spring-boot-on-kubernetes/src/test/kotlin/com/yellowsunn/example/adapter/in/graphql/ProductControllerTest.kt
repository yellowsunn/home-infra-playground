package com.yellowsunn.example.adapter.`in`.graphql

import com.navercorp.fixturemonkey.kotlin.giveMe
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.setExp
import com.yellowsunn.example.adapter.`in`.graphql.dto.ProductPageResponse
import com.yellowsunn.example.application.port.`in`.ProductUseCase
import com.yellowsunn.example.application.port.`in`.dto.ProductPageCommandResult
import com.yellowsunn.example.common.model.Page
import com.yellowsunn.example.fixtureMonkey
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.anyInt
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.core.ParameterizedTypeReference
import org.springframework.core.io.ClassPathResource
import org.springframework.graphql.test.tester.GraphQlTester
import reactor.kotlin.core.publisher.toMono

@ExtendWith(MockKExtension::class)
@SpringBootTest
@AutoConfigureGraphQlTester
class ProductControllerTest {

    @Autowired
    private lateinit var graphQlTester: GraphQlTester

    @MockBean
    private lateinit var productUseCase: ProductUseCase

    @Test
    fun findProducts() {
        // given
        val findProductQuery = ClassPathResource("mock/graphql/findProductsQuery.graphql")
        val commandResult = fixtureMonkey.giveMeBuilder<Page<ProductPageCommandResult>>()
            .setExp(Page<ProductPageCommandResult>::contents, fixtureMonkey.giveMe<ProductPageCommandResult>(5))
            .sample()
        given(productUseCase.getProducts(page = anyInt(), size = anyInt()))
            .willReturn(commandResult.toMono())

        // when
        // then
        graphQlTester.document(findProductQuery.getContentAsString(Charsets.UTF_8))
            .execute()
            .path("findProducts").entity(object : ParameterizedTypeReference<Page<ProductPageResponse>>() {
            })
            .satisfies { it: Page<ProductPageResponse> ->
                assertThat(it.contents).hasSize(commandResult.contents.size)
                assertThat(it.page).isEqualTo(commandResult.page)
                assertThat(it.size).isEqualTo(commandResult.size)
            }
    }
}
