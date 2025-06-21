package com.capstone.ads.model.json.orderhistory;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttributeSelection {
    String attribute;
    String value;
    String unit;
    Long materialPrice;
    Long unitPrice;
    Boolean isMultiplier;
    String calculateFormula;
    Long subTotal;
}
