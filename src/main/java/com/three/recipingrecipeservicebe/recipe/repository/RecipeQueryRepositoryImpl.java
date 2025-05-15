package com.three.recipingrecipeservicebe.recipe.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.three.recipingrecipeservicebe.recipe.dto.RecipeSearchConditionRequestDto;
import com.three.recipingrecipeservicebe.recipe.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.three.recipingrecipeservicebe.recipe.entity.QRecipe.recipe;

@Repository
@RequiredArgsConstructor
public class RecipeQueryRepositoryImpl implements RecipeQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Recipe> findPagedByCreatedAtDesc(Pageable pageable) {
        List<Recipe> content = queryFactory.selectFrom(recipe)
                .where(recipe.deletedAt.isNull())
                .orderBy(recipe.createdAt.desc())
                .offset(pageable.getOffset()) // 더 간결하고 정확한 offset 방식
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(recipe.count())
                .from(recipe)
                .where(recipe.deletedAt.isNull())
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }

    @Override
    public Page<Recipe> searchByCondition(RecipeSearchConditionRequestDto cond, Pageable pageable) {
        String cookingTimeLabel = (cond.getCookingTime() != null) ? cond.getCookingTime().getLabel() : null;
        String difficultyLabel = (cond.getDifficulty() != null) ? cond.getDifficulty().getLabel() : null;

        List<Recipe> results = queryFactory
                .selectFrom(recipe)
                .where(
                        matchIfNotAll(recipe.dishType, cond.getDishType(), DishType.ALL),
                        matchIfNotAll(recipe.ingredientType, cond.getIngredientType(), IngredientType.ALL),
                        matchIfNotAll(recipe.situationType, cond.getSituationType(), SituationType.ALL),
                        matchIfNotAll(recipe.methodType, cond.getMethodType(), MethodType.ALL),
                        matchStringIfNotAll(recipe.cookingTime, cookingTimeLabel, CookingTime.ALL.getLabel()),
                        matchStringIfNotAll(recipe.difficulty, difficultyLabel, Difficulty.ALL.getLabel())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(recipe.count())
                .from(recipe)
                .where(
                        matchIfNotAll(recipe.dishType, cond.getDishType(), DishType.ALL),
                        matchIfNotAll(recipe.ingredientType, cond.getIngredientType(), IngredientType.ALL),
                        matchIfNotAll(recipe.situationType, cond.getSituationType(), SituationType.ALL),
                        matchIfNotAll(recipe.methodType, cond.getMethodType(), MethodType.ALL),
                        matchStringIfNotAll(recipe.cookingTime, cookingTimeLabel, CookingTime.ALL.getLabel()),
                        matchStringIfNotAll(recipe.difficulty, difficultyLabel, Difficulty.ALL.getLabel())
                )
                .fetchOne();

        return new PageImpl<>(results, pageable, total != null ? total : 0);
    }

    private <T extends Enum<T>> BooleanExpression matchIfNotAll(EnumPath<T> field, T value, T allValue) {
        return (value != null && !value.equals(allValue)) ? field.eq(value) : null;
    }

    private BooleanExpression matchStringIfNotAll(StringPath field, String value, String allValue) {
        return (value != null && !value.equals(allValue)) ? field.eq(value) : null;
    }
}