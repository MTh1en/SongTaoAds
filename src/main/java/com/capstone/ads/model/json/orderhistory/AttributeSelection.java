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
    Double materialPrice;
    Double unitPrice;
    String calculateFormula;
    Double subTotal;
}
