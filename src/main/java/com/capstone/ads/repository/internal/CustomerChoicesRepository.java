package com.capstone.ads.repository.internal;

import com.capstone.ads.model.CustomerChoices;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerChoicesRepository extends JpaRepository<CustomerChoices, String> {
    Optional<CustomerChoices> findByUsers_IdOrderByUpdatedAtDesc(String id);

    @EntityGraph(attributePaths = {
            "customerChoiceDetails.attributeValues.attributes", // Load chi tiết, giá trị thuộc tính, và thuộc tính của nó
            "customerChoiceSizes.sizes", // Load kích thước
            "productTypes.attributes", // Load thuộc tính của loại sản phẩm
            "productTypes.costTypes" // Nếu CostTypes cũng được load cùng ProductTypes
    })
    @NonNull
    Optional<CustomerChoices> findById(@NonNull String id);
}