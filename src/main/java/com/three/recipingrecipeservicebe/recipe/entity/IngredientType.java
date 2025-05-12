package com.three.recipingrecipeservicebe.recipe.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IngredientType implements EnumWithLabel {

    ALL("전체"),
    BEEF("소고기"),
    PORK("돼지고기"),
    CHICKEN("닭고기"),
    MEAT("육류"),
    VEGETABLE("채소류"),
    SEAFOOD("해물류"),
    EGG_DAIRY("달걀/유제품"),
    PROCESSED("가공식품류"),
    RICE("쌀"),
    FLOUR("밀가루"),
    DRIED("건어물류"),
    MUSHROOM("버섯류"),
    FRUIT("과일류"),
    BEAN_NUT("콩/견과류"),
    GRAIN("곡류"),
    ETC("기타");

    private final String label;

    @Override
    public String getLabel() {
        return label;
    }
}
