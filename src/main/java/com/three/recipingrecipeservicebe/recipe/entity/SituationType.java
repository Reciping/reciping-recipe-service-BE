package com.three.recipingrecipeservicebe.recipe.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SituationType implements EnumWithLabel {

    ALL("전체"),
    DAILY("일상"),
    QUICK("초스피드"),
    GUEST("손님접대"),
    DRINK("술안주"),
    DIET("다이어트"),
    LUNCHBOX("도시락"),
    NUTRITION("영양식"),
    SNACK("간식"),
    LATE_NIGHT("야식"),
    STYLING("푸드스타일링"),
    HANGOVER("해장"),
    HOLIDAY("명절"),
    BABY("이유식"),
    ETC("기타");

    private final String label;

    @Override
    public String getLabel() {
        return label;
    }
}
