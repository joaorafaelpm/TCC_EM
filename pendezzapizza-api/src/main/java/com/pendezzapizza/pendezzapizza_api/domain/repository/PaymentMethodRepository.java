package com.pendezzapizza.pendezzapizza_api.domain.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pendezzapizza.pendezzapizza_api.domain.model.PaymentMethod;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Interface criada para fazer a ponte entre banco e entidade de <b>forma de pagamento</b>
 *
 * <p>Toda query sql a mais está implementada dentro da função com seu código sql explicado  </p>
 */
@Repository
public interface PaymentMethodRepository extends CustomJPARepository<PaymentMethod, UUID> {

    //    Pegar a última data de atualização
    @Query("select max(updateDate) from PaymentMethod")
    OffsetDateTime getLastUpdateDate();

//    Pegar a última data de atualização por id
    @Query("select max(updateDate) from PaymentMethod where id = :paymentMethodId")
    OffsetDateTime getLastUpdateDateById(UUID paymentMethodId);
}
