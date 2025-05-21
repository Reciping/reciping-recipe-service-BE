package com.three.recipingrecipeservicebe.recipe.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

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

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SituationType fromLabel(String label) {
        return Arrays.stream(SituationType.values())
                .filter(e -> e.label.equals(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown DishType label: " + label));
    }
}
