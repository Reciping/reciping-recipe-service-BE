package com.three.recipingrecipeservicebe.hashtag.repository;

import com.three.recipingrecipeservicebe.hashtag.entity.HashTag;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface HashTagRepository extends MongoRepository<HashTag, String> {

    Optional<HashTag> findByName(String name);

    List<HashTag> findAllByNameIn(Collection<String> names);
}
