package com.yellowsunn.example.adapter.`in`.rest

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SampleLogControllerTest {

    private val sut = SampleLogController()

    @Test
    fun warnLog() {
        // when
        val throwable = Assertions.catchThrowable { sut.warnLog() }

        // then
        assertThat(throwable).isNull()
    }

    @Test
    fun errorLog() {
        // when
        val throwable = Assertions.catchThrowable { sut.errorLog() }

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException::class.java)
    }
}
