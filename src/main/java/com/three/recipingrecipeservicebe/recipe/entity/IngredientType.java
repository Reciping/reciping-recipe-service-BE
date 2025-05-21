package com.three.recipingrecipeservicebe.recipe.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

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


    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static IngredientType fromLabel(String label) {
        return Arrays.stream(IngredientType.values())
                .filter(e -> e.label.equals(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown DishType label: " + label));
    }
}
