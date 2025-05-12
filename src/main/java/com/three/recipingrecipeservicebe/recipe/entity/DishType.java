package com.three.recipingrecipeservicebe.recipe.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public enum DishType implements EnumWithLabel {

    ALL("전체"),
    BANCHAN("밑반찬"),
    MAIN_DISH("메인반찬"),
    SOUP("국/탕"),
    STEW("찌개"),
    DESSERT("디저트"),
    NOODLE_DUMPLING("면/만두"),
    RICE_PORRIDGE("밥/죽/떡"),
    FUSION("퓨전"),
    KIMCHI_SAUCE("김치/젓갈/장류"),
    SEASONING("양념/소스/잼"),
    WESTERN("양식"),
    SALAD("샐러드"),
    SOUP_WESTERN("스프"),
    BREAD("빵"),
    SNACK("과자"),
    BEVERAGE("차/음료/술"),
    ETC("기타");

    private final String label;

    @Override
    public String getLabel() {
        return label;
    }
}
