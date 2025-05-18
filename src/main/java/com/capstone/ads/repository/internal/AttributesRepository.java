package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Attributes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttributesRepository extends JpaRepository<Attributes, String> {
    List<Attributes> findByProductType_Id(String id);

}