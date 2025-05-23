package com.three.recipingrecipeservicebe.admin.mapper;

import com.three.recipingrecipeservicebe.admin.dto.AdminRecipeDto;
import com.three.recipingrecipeservicebe.admin.entity.AdminRecipe;
import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        builder = @Builder(disableBuilder = true),
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminRecipeMapper {

    AdminRecipeDto toAdminDto(AdminRecipe recipe);

}
