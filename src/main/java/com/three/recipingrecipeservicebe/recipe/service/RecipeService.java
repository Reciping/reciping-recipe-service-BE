package com.three.recipingrecipeservicebe.recipe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "RecipeService")
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RecipeService {

}
