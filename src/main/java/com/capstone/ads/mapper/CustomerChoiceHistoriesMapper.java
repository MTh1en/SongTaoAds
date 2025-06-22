package com.capstone.ads.mapper;

import com.capstone.ads.model.CustomerChoiceDetails;
import com.capstone.ads.model.CustomerChoiceSizes;
import com.capstone.ads.model.CustomerChoices;
import com.capstone.ads.model.json.CustomerChoiceHistories;
import com.capstone.ads.model.json.orderhistory.AttributeSelection;
import com.capstone.ads.model.json.orderhistory.SizeSelection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerChoiceHistoriesMapper {
    @Mapping(target = "productTypeName", source = "productTypes.name")
    @Mapping(target = "calculateFormula", source = "productTypes.calculateFormula")
    CustomerChoiceHistories toProductTypeInCustomerChoiceHistories(CustomerChoices customerChoices);

    @Mapping(target = "attribute", source = "attributeValues.attributes.name")
    @Mapping(target = "value", source = "attributeValues.name")
    @Mapping(target = "unit", source = "attributeValues.unit")
    @Mapping(target = "materialPrice", source = "attributeValues.materialPrice")
    @Mapping(target = "unitPrice", source = "attributeValues.unitPrice")
    @Mapping(target = "calculateFormula", source = "attributeValues.attributes.calculateFormula")
    @Mapping(target = "isMultiplier", source = "attributeValues.isMultiplier")
    AttributeSelection toAttributeSelection(CustomerChoiceDetails customerChoiceDetails);

    @Mapping(target = "size", source = "sizes.name")
    @Mapping(target = "value", source = "sizeValue")
    SizeSelection toSizeSelection(CustomerChoiceSizes detail);
}
