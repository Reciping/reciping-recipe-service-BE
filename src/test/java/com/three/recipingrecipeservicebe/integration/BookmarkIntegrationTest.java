package com.three.recipingrecipeservicebe.integration;

import com.three.recipingrecipeservicebe.bookmark.entity.RecipeBookmarkDocument;
import com.three.recipingrecipeservicebe.bookmark.repository.RecipeBookmarkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.data.mongodb.uri=mongodb://localhost:27017/recipe_bookmark"
})
public class BookmarkIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecipeBookmarkRepository recipeBookmarkRepository;

    @BeforeEach
    void setUp() {
        recipeBookmarkRepository.deleteAll();
    }

    @Test
    @DisplayName("READ 사용자의 즐겨찾기 목록을 페이징 정렬 기준으로 정확히 조회")
    void getBookmarksByUserIdWithPaging() throws Exception {
        Long userId = 1L;

        List<RecipeBookmarkDocument> bookmarks = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            bookmarks.add(
                    RecipeBookmarkDocument.builder()
                            .userId(userId)
                            .recipeId(101L + i)
                            .build()
            );
        }

        recipeBookmarkRepository.saveAll(bookmarks);

        // 🧪 첫 페이지 (page = 0)
        mockMvc.perform(get("/api/v1/bookmarks/user/{userId}", userId)
                        .param("page", "0")
                        .param("size", "3")
                        .param("sort", "createdAt,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.totalElements").value(6));

        // 🧪 두 번째 페이지 (page = 1)
        mockMvc.perform(get("/api/v1/bookmarks/user/{userId}", userId)
                        .param("page", "1")
                        .param("size", "3")
                        .param("sort", "createdAt,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3));
    }

    @Test
    @DisplayName("UPDATE 즐겨찾기 토글 요청 시 상태 변경")
    void toggleBookmark() throws Exception {
        Long userId = 1L;
        Long recipeId = 100L;

        String jsonBody = """
        {
            "userId": 1,
            "recipeId": 100
        }
        """;

        // 즐겨찾기 등록
        mockMvc.perform(post("/api/v1/bookmarks/toggle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // 즐겨찾기 해제
        mockMvc.perform(post("/api/v1/bookmarks/toggle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

}
