package com.three.recipingrecipeservicebe.recipe.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum CookingTime implements EnumWithLabel {

    ALL("전체"),
    UNDER_5_MIN("5분 이내"),
    UNDER_10_MIN("10분 이내"),
    UNDER_15_MIN("15분 이내"),
    UNDER_20_MIN("20분 이내"),
    UNDER_30_MIN("30분 이내"),
    UNDER_60_MIN("60분 이내"),
    UNDER_90_MIN("90분 이내"),
    UNDER_120_MIN("2시간 이내"),
    OVER_120_MIN("2시간 이상");

    private final String label;

    @Override
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static CookingTime fromLabel(String label) {
        return Arrays.stream(CookingTime.values())
                .filter(ct -> ct.label.equals(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown label: " + label));
    }
}
