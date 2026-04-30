package com.pendezzapizza.pendezzapizza_api.domain.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pendezzapizza.pendezzapizza_api.domain.model.PaymentMethod;

import java.time.OffsetDateTime;
import java.util.UUID;

@Repository
public interface PaymentMethodRepository extends CustomJPARepository<PaymentMethod, UUID> {

    @Query("select max(updateDate) from PaymentMethod")
    OffsetDateTime getLastUpdateDate();

    @Query("select max(updateDate) from PaymentMethod where id = :paymentMethodId")
    OffsetDateTime getLastUpdateDateById(UUID paymentMethodId);
}
