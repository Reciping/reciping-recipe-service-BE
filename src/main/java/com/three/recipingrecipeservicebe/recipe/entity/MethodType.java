package com.three.recipingrecipeservicebe.recipe.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum MethodType implements EnumWithLabel{

    ALL("전체"),
    STIR_FRY("볶음"),
    BOIL("끓이기"),
    PAN_FRY("부침"),
    BRAISE("조림"),
    SEASONED_MIX("무침"),
    MIX("비빔"),
    STEAM("찜"),
    PICKLE("절임"),
    DEEP_FRY("튀김"),
    SIMMER("삶기"),
    GRILL("굽기"),
    BLANCH("데치기"),
    RAW("회"),
    ETC("기타");

    private final String label;

    @Override
    public String getLabel() {
        return label;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static MethodType fromLabel(String label) {
        return Arrays.stream(MethodType.values())
                .filter(e -> e.label.equals(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown DishType label: " + label));
    }
}
