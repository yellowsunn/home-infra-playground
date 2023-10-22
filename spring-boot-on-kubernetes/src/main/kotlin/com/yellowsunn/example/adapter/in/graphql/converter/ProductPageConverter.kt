package com.yellowsunn.example.adapter.`in`.graphql.converter

import com.yellowsunn.example.adapter.`in`.graphql.dto.ProductPageResponse
import com.yellowsunn.example.application.port.`in`.dto.ProductPageCommandResult
import com.yellowsunn.example.common.model.Page
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers

@Mapper
fun interface ProductPageConverter {

    companion object {
        val CONVERTER: ProductPageConverter = Mappers.getMapper(ProductPageConverter::class.java)
    }

    @Mapping(source = "productPage", target = ".")
    fun convertToResponse(productPage: Page<ProductPageCommandResult>): Page<ProductPageResponse>
}
