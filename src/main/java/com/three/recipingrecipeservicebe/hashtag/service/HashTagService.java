package com.three.recipingrecipeservicebe.hashtag.service;

import com.three.recipingrecipeservicebe.hashtag.entity.HashTag;
import com.three.recipingrecipeservicebe.hashtag.repository.HashTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HashTagService {
    private final HashTagRepository hashTagRepository;

    public void saveIfNotExists(List<String> tags) {
        for (String tag : tags) {
            hashTagRepository.findByName(tag)
                    .orElseGet(() -> hashTagRepository.save(
                            HashTag.builder().name(tag).build()));
        }
    }
}
