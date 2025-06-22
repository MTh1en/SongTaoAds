package com.capstone.ads.model.json;

import com.capstone.ads.model.json.orderhistory.AttributeSelection;
import com.capstone.ads.model.json.orderhistory.SizeSelection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerChoiceHistories {
    String productTypeName;
    String calculateFormula;
    Long totalAmount;
    List<AttributeSelection> attributeSelections;
    List<SizeSelection> sizeSelections;
}
