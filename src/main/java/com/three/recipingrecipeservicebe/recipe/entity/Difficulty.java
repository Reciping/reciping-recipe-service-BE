package com.three.recipingrecipeservicebe.recipe.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum Difficulty implements EnumWithLabel {

    ALL("전체"),
    ANYONE("아무나"),
    BEGINNER("초급"),
    INTERMEDIATE("중급"),
    ADVANCED("고급"),
    MASTER("신의경지");

    private final String label;

    @Override
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static Difficulty fromLabel(String label) {
        return Arrays.stream(Difficulty.values())
                .filter(d -> d.label.equals(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown label: " + label));
    }
}
