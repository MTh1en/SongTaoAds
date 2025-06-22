package com.capstone.ads.mapper;

import com.capstone.ads.dto.design_template.DesignTemplateCreateRequest;
import com.capstone.ads.dto.design_template.DesignTemplateDTO;
import com.capstone.ads.dto.design_template.DesignTemplateUpdateRequest;
import com.capstone.ads.model.DesignTemplates;
import com.capstone.ads.model.ProductTypes;
import com.capstone.ads.model.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DesignTemplatesMapper {
    @Mapping(target = "userId", source = "users.id")
    @Mapping(target = "productTypeId", source = "productTypes.id")
    DesignTemplateDTO toDTO(DesignTemplates designTemplates);

    @Mapping(target = "users", expression = "java(mapUsers(userId))")
    @Mapping(target = "productTypes", expression = "java(mapProductTypes(productTypeId))")
    DesignTemplates toEntity(DesignTemplateCreateRequest request, String userId, String productTypeId);

    void updateEntityFromRequest(DesignTemplateUpdateRequest request, @MappingTarget DesignTemplates designTemplates);

    default Users mapUsers(String userId) {
        if (userId == null) return null;
        Users users = new Users();
        users.setId(userId);
        return users;
    }

    default ProductTypes mapProductTypes(String productTypeId) {
        if (productTypeId == null) return null;
        ProductTypes productType = new ProductTypes();
        productType.setId(productTypeId);
        return productType;
    }
}
