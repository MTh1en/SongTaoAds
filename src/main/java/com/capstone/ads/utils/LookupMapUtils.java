package com.capstone.ads.utils;

import com.capstone.ads.model.CustomerChoices;
import com.capstone.ads.model.ProductTypeSizes;
import com.capstone.ads.model.enums.DimensionType;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class LookupMapUtils {

    public static Map<String, Map<DimensionType, ProductTypeSizes>> mapProductTypeSizesByDimensionAndSize(CustomerChoices customerChoices) {
        Map<String, Map<DimensionType, ProductTypeSizes>> productTypeSizesLookup = new HashMap<>();
        List<ProductTypeSizes> productTypeSizesForProductType = customerChoices.getProductTypes().getProductTypeSizes();

        for (ProductTypeSizes pts : productTypeSizesForProductType) {
            if (pts.getSizes() != null && pts.getDimensionType() != null) {
                productTypeSizesLookup
                        .computeIfAbsent(pts.getSizes().getId(), k -> new HashMap<>())
                        .put(pts.getDimensionType(), pts);
            }
        }
        return productTypeSizesLookup;
    }
}
