package com.pendezzapizza.pendezzapizza_api.domain.repository;

import com.pendezzapizza.pendezzapizza_api.domain.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface PermissionRepository extends CustomJPARepository<Permission, UUID> {



}
