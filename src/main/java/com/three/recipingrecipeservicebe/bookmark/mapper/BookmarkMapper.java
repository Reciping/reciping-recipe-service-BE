package com.three.recipingrecipeservicebe.bookmark.mapper;

import com.three.recipingrecipeservicebe.bookmark.dto.BookmarkResponseDto;
import com.three.recipingrecipeservicebe.bookmark.entity.RecipeBookmarkDocument;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookmarkMapper {

    BookmarkResponseDto toResponseDto(RecipeBookmarkDocument document);

}
