//package com.three.recipingrecipeservicebe.repsitory;
//
//import com.three.recipingrecipeservicebe.global.config.JpaConfig;
//import com.three.recipingrecipeservicebe.recipe.entity.Recipe;
//import com.three.recipingrecipeservicebe.recipe.repository.RecipeRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.context.annotation.Import;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.util.List;
//import java.util.stream.IntStream;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//@Import(JpaConfig.class)
//public class RecipeRepositoryTest {
//
//    @Autowired
//    private RecipeRepository recipeRepository;
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @BeforeEach
//    void setUp() {
//        // Given: 제목01~제목20 더미 레시피 20개 저장
//        List<Recipe> recipes = IntStream.rangeClosed(1, 20)
//                .mapToObj(i -> Recipe.builder()
//                        .userId(1L)
//                        .title("제목" + i)
//                        .content("내용입니다." + i)
//                        .isDeleted(false)
//                        .build())
//                .toList();
//
//        recipeRepository.saveAll(recipes);
//    }
//
//    @Test
//    @DisplayName("CREATE 단일 레시피 저장")
//    void saveRecipe() {
//        Recipe recipe = Recipe.builder()
//                .userId(1L)
//                .title("테스트 제목")
//                .content("테스트 내용")
//                .build();
//
//        Recipe saved = recipeRepository.save(recipe);
//
//        assertThat(saved.getId()).isNotNull();
//        assertThat(saved.getTitle()).isEqualTo("테스트 제목");
//    }
//
//    @Test
//    @DisplayName("READ 최신순 12개 페이징 - 첫 페이지")
//    void getLatestPage1List() {
//        Pageable pageable = PageRequest.of(0, 12);
//        List<Recipe> results = recipeRepository.findPagedByCreatedAtDesc(pageable);
//        results.forEach(recipe ->
//                System.out.printf("title: %s, createdAt: %s%n", recipe.getTitle(), recipe.getCreatedAt())
//        );
//        assertThat(results).hasSize(12);
//        assertThat(results.get(0).getTitle()).isEqualTo("제목20");
//        assertThat(results.get(11).getTitle()).isEqualTo("제목9");
//    }
//
//    @Test
//    @DisplayName("READ 최신순 12개 페이징 - 두 번째 페이지")
//    void getLatestPage2List() {
//        Pageable pageable = PageRequest.of(1, 12);
//        List<Recipe> results = recipeRepository.findPagedByCreatedAtDesc(pageable);
//        assertThat(results).hasSize(8);
//        assertThat(results.get(0).getTitle()).isEqualTo("제목8");
//        assertThat(results.get(7).getTitle()).isEqualTo("제목1");
//    }
//
//    @Test
//    @DisplayName("DELETE Soft delete 테스트")
//    void softDelete() {
//        // given
//        Recipe saved = recipeRepository.save(Recipe.builder()
//                .userId(1L)
//                .title("삭제 테스트용 제목")
//                .content("내용")
//                .build());
//
//        Long id = saved.getId();
//
//        // when
//        recipeRepository.deleteById(id);
//        entityManager.flush();
//        entityManager.clear();
//
//        // then
//        // 삭제된 row는 기본 findById로 조회되지 않음
//        assertThat(recipeRepository.findById(id)).isEmpty();
//
//        // 삭제된 row 직접 조회 (native 또는 커스텀 쿼리)
//        Recipe deleted = (Recipe) entityManager.getEntityManager()
//                .createNativeQuery("SELECT * FROM recipes WHERE id = :id", Recipe.class)
//                .setParameter("id", id)
//                .getSingleResult();
//
//        assertThat(deleted.getIsDeleted()).isTrue();
//        assertThat(deleted.getDeletedAt()).isNotNull();
//    }
//}
