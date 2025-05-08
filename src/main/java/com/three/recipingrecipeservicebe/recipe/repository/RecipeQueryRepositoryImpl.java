package com.three.recipingrecipeservicebe.recipe.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.three.recipingrecipeservicebe.recipe.entity.Recipe;
import lombok.RequiredArgsConstructor;
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

}