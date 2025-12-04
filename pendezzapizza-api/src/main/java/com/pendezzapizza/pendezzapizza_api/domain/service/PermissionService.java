package com.pendezzapizza.pendezzapizza_api.domain.service;

import com.pendezzapizza.pendezzapizza_api.domain.model.Permission;
import com.pendezzapizza.pendezzapizza_api.domain.repository.PermissionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public List<Permission> findAll () {
        return permissionRepository.findAll();
    }

    public Permission findById (UUID id ) {
        return permissionRepository.findByIdOrThrowException(id);
    }

    public Permission save (Permission permission) {
        return permissionRepository.save(permission);
    }

}
