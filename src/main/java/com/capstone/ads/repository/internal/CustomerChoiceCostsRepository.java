package com.capstone.ads.repository.internal;

import com.capstone.ads.model.CustomerChoiceCosts;
import com.capstone.ads.model.CustomerChoices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CustomerChoiceCostsRepository extends JpaRepository<CustomerChoiceCosts, String> {
    List<CustomerChoiceCosts> findByCustomerChoices_Id(String id);

    @Transactional
    @Modifying
    @Query("delete from CustomerChoiceCosts c where c.customerChoices = ?1")
    void deleteByCustomerChoices(CustomerChoices customerChoices);
}