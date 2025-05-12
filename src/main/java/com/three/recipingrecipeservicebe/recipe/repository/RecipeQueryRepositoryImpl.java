package com.three.recipingrecipeservicebe.recipe.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EnumPath;
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
    public List<Recipe> findPagedByCreatedAtDesc(Pageable pageable) {
        return queryFactory.selectFrom(recipe)
                .where(recipe.deletedAt.isNull())
                .orderBy(recipe.createdAt.desc())
                .offset((long) pageable.getPageNumber() * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public Page<Recipe> searchByCondition(RecipeSearchConditionRequestDto cond, Pageable pageable) {
        List<Recipe> results = queryFactory
                .selectFrom(recipe)
                .where(
                        matchIfNotAll(recipe.dishType, cond.getDish(), DishType.ALL),
                        matchIfNotAll(recipe.ingredientType, cond.getIngredient(), IngredientType.ALL),
                        matchIfNotAll(recipe.situationType, cond.getSituation(), SituationType.ALL),
                        matchIfNotAll(recipe.methodType, cond.getMethod(), MethodType.ALL)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(recipe.count())
                .from(recipe)
                .where(
                        matchIfNotAll(recipe.dishType, cond.getDish(), DishType.ALL),
                        matchIfNotAll(recipe.ingredientType, cond.getIngredient(), IngredientType.ALL),
                        matchIfNotAll(recipe.situationType, cond.getSituation(), SituationType.ALL),
                        matchIfNotAll(recipe.methodType, cond.getMethod(), MethodType.ALL)
                )
                .fetchOne();

        return new PageImpl<>(results, pageable, total == null ? 0 : total);
    }

    private <T extends Enum<T>> BooleanExpression matchIfNotAll(EnumPath<T> field, T value, T allValue) {
        return (value != null && !value.equals(allValue)) ? field.eq(value) : null;
    }
}