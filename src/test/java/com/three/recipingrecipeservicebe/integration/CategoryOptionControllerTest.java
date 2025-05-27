package com.three.recipingrecipeservicebe.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryOptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/v1/recipes/category-options - 분류 옵션 출력")
    void shouldReturnAllCategoryOptions() throws Exception {
        mockMvc.perform(get("/api/v1/recipes/category-options"))
                .andDo(print())  // 👈 응답 내용 출력
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dishType").isArray())
                .andExpect(jsonPath("$.methodType").isArray())
                .andExpect(jsonPath("$.ingredientType").isArray())
                .andExpect(jsonPath("$.situationType").isArray())
                .andExpect(jsonPath("$.dishType[0].value").exists())
                .andExpect(jsonPath("$.dishType[0].label").exists());
    }
}
