package com.yellowsunn.example.application.port.`in`.converter

import com.yellowsunn.example.application.port.`in`.dto.ProductPageCommandResult
import com.yellowsunn.example.common.model.Page
import com.yellowsunn.example.domain.Product
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers

@Mapper
fun interface ProductCommandPageConverter {

    companion object {
        val CONVERTER: ProductCommandPageConverter = Mappers.getMapper(ProductCommandPageConverter::class.java)
    }

    @Mapping(source = "productPage", target = ".")
    fun convertToCommandResult(productPage: Page<Product>): Page<ProductPageCommandResult>
}
