package com.pendezzapizza.pendezzapizza_api.domain.repository;

import com.pendezzapizza.pendezzapizza_api.domain.model.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.UUID;


@Repository
public interface PermissionRepository extends CustomJPARepository<Permission, UUID> {

    @Override
    Page<Permission> findAll (Pageable pageable);

    @Query("select max(p.updateDate) from Permission p")
    OffsetDateTime getLastUpdateDate();

    @Query("select max(p.updateDate) from Permission p where p.id = :permissionId")
    OffsetDateTime getLastUpdateDateById(UUID permissionId);


}
