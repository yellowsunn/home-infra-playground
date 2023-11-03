package com.yellowsunn.example.adapter.`in`.rest

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class SampleLogController {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @GetMapping("/api/logs/warn")
    fun warnLog() {
        logger.warn("This is warning log.")
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @GetMapping("/api/logs/error")
    fun errorLog() {
        try {
            throw IllegalArgumentException("This is error log.")
        } catch (e: Exception) {
            logger.error("Unknown exception. message={}", e.message, e)
            throw e
        }
    }
}
