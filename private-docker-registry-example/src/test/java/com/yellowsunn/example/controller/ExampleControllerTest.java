//package com.yellowsunn.example.controller;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(ExampleController.class)
//class ExampleControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    void helloTest() throws Exception {
//        // given
//        var request = MockMvcRequestBuilders.get("/")
//                .accept(MediaType.APPLICATION_JSON);
//
//        // when
//        MvcResult result = mockMvc.perform(request)
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // then
//        assertThat(result.getResponse().getContentAsString()).startsWith("hello world");
//    }
//}
