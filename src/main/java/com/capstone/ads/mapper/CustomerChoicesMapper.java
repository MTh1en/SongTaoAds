package com.capstone.ads.mapper;

import com.capstone.ads.dto.customer_choice.CustomerChoicesDTO;
import com.capstone.ads.model.CustomerChoices;
import com.capstone.ads.model.ProductTypes;
import com.capstone.ads.model.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerChoicesMapper {
    @Mapping(target = "userId", source = "users.id")
    @Mapping(target = "productTypeId", source = "productTypes.id")
    CustomerChoicesDTO toDTO(CustomerChoices customerChoices);

    @Mapping(target = "users", expression = "java(mapUsers(userId))")
    @Mapping(target = "productTypes", expression = "java(mapProductType(productTypeId))")
    CustomerChoices toEntity(String userId, String productTypeId);

    default Users mapUsers(String userId) {
        if (userId == null) return null;
        Users users = new Users();
        users.setId(userId);
        return users;
    }

    default ProductTypes mapProductType(String productTypeId) {
        if (productTypeId == null) return null;
        ProductTypes productTypes = new ProductTypes();
        productTypes.setId(productTypeId);
        return productTypes;
    }
}
