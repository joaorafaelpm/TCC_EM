package com.pendezzapizza.pendezzapizza_api.domain.repository;

import com.pendezzapizza.pendezzapizza_api.domain.model.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.UUID;



/**
 * Interface criada para fazer a ponte entre banco e entidade de <b>permissão</b>
 *
 * <p>Toda query sql a mais está implementada dentro da função com seu código sql explicado  </p>
 */
@Repository
public interface PermissionRepository extends CustomJPARepository<Permission, UUID> {

//    Sobrescrevo a função para retornar uma página
    @Override
    Page<Permission> findAll (Pageable pageable);

//    Pegar a última data de atualização
    @Query("select max(p.updateDate) from Permission p")
    OffsetDateTime getLastUpdateDate();

//    Pegar a última data de atualização por id
    @Query("select max(p.updateDate) from Permission p where p.id = :permissionId")
    OffsetDateTime getLastUpdateDateById(UUID permissionId);


}
