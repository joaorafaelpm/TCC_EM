package com.pendezzapizza.pendezzapizza_api.domain.service;

import com.pendezzapizza.pendezzapizza_api.domain.model.Permission;
import com.pendezzapizza.pendezzapizza_api.domain.repository.PermissionRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class PermissionService {

    private final PermissionRepository permissionRepository;

    @Cacheable("permissions")
    public Page<Permission> findAll (Pageable pageable) {
        return permissionRepository.findAll(pageable);
    }

    public Permission findById (UUID permissionId) {
        return permissionRepository.findByIdOrThrowException(permissionId);
    }

    @Cacheable(value = "permissionsName", key = "{#pageable.pageNumber, #pageable.pageSize, #permissionName}")
    public Page<Permission> findAllByName(String permissionName, Pageable pageable) {
        return permissionRepository.findByName(permissionName, pageable);
    }

    @Cacheable("permissionsLastUpdateDate")
    public OffsetDateTime getLastUpdateDate () {
        return permissionRepository.getLastUpdateDate();
    }
    @Cacheable(value = "permissionsLastUpdateDateById" , key = "#id")
    public OffsetDateTime getLastUpdateDateById (UUID permissionId) {
        return permissionRepository.getLastUpdateDateById(permissionId);
    }

}
