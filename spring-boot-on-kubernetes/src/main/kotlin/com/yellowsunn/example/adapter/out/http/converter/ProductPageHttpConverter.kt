package com.yellowsunn.example.adapter.out.http.converter

import com.yellowsunn.example.adapter.out.http.dto.ProductPageHttpResponse
import com.yellowsunn.example.domain.Product
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers

@Mapper
fun interface ProductPageHttpConverter {

    companion object {
        val CONVERTER: ProductPageHttpConverter = Mappers.getMapper(ProductPageHttpConverter::class.java)
    }

    @Mapping(source = "products", target = ".")
    fun convertToProducts(products: List<ProductPageHttpResponse.Product>): List<Product>
}
