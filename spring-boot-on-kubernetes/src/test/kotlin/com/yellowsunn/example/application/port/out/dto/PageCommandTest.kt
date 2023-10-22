package com.yellowsunn.example.application.port.out.dto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PageCommandTest {

    @Test
    fun getSkip() {
        // given
        val page = 5
        val size = 10
        val pageCommand = PageCommand(page = page, size = size)

        // when
        val skip = pageCommand.getSkip()

        // then
        assertThat(skip).isEqualTo(50L)
    }
}
