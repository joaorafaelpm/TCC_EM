package com.pendezzapizza.pendezzapizza_api.domain.service;

import com.pendezzapizza.pendezzapizza_api.core.cache.CacheInvalidatorUtil;
import com.pendezzapizza.pendezzapizza_api.domain.exception.EntityInUseException;
import com.pendezzapizza.pendezzapizza_api.domain.model.Group;
import com.pendezzapizza.pendezzapizza_api.domain.repository.GroupRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository ;

    private final PermissionService permissionService;
    private final UserService userService;
    private CacheInvalidatorUtil cacheInvalidatorUtil;

    @Cacheable("groups")
    public Page<Group> findAll (Pageable pageable) {
        return groupRepository.findAll(pageable);
    }

    @Cacheable(value = "group", key = "#id")
    public Group findById (UUID id ) {
        return groupRepository.findByIdOrThrowException(id);
    }

    @Cacheable("groupsLastUpdate")
    public OffsetDateTime getLastUpdateDate() {
        return groupRepository.getLastGroupUpdateDate();
    }

    @Cacheable(value = "groupsLastUpdateById", key = "#id")
    public OffsetDateTime getLastUpdateDateById(UUID groupId) {
        return groupRepository.getLastGroupUpdateDateById(groupId);
    }

    @Caching(evict = {
            @CacheEvict(value = "groups",            allEntries = true),
            @CacheEvict(value = "group",             key = "#group.id"),
            @CacheEvict(value = "groupsLastUpdate",  allEntries = true),
            @CacheEvict(value = "groupsLastUpdateById", key = "#group.id")
    })
    @Transactional
    public Group save (Group group) {
        cacheInvalidatorUtil.publishCacheInvalidation("groups" , "group" , "groupsLastUpdate" , "groupsLastUpdateById");

        return groupRepository.save(group);
    }

    @Caching(evict = {
            @CacheEvict(value = "groups",            allEntries = true),
            @CacheEvict(value = "groups",             key = "#group.id"),
            @CacheEvict(value = "groupsLastUpdate",  allEntries = true),
            @CacheEvict(value = "groupsLastUpdateById", key = "#group.id")
    })
    @Transactional
    public void deleteById (UUID id) {
        try {
            cacheInvalidatorUtil.publishCacheInvalidation("groups" , "group" , "groupsLastUpdate" , "groupsLastUpdateById");

            groupRepository.delete(findById(id));
            groupRepository.flush();
        }catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(id);
        }
    }

    @Caching(evict = {
            @CacheEvict(value = "groups",            allEntries = true),
            @CacheEvict(value = "groups",             key = "#group.id"),
            @CacheEvict(value = "groupsLastUpdate",  allEntries = true),
            @CacheEvict(value = "groupsLastUpdateById", key = "#group.id")
    })
    @Transactional
    public void associatePermission (UUID groupId , UUID permissionId) {
        cacheInvalidatorUtil.publishCacheInvalidation("groups" , "group" , "groupsLastUpdate" , "groupsLastUpdateById");

        Group group = findById(groupId);
        group.associatePermission(permissionService.findById(permissionId));
    }
    @Caching(evict = {
            @CacheEvict(value = "groups",            allEntries = true),
            @CacheEvict(value = "groups",             key = "#group.id"),
            @CacheEvict(value = "groupsLastUpdate",  allEntries = true),
            @CacheEvict(value = "groupsLastUpdateById", key = "#group.id")
    })
    @Transactional
    public void disassociatePermission (UUID groupId , UUID permissionId) {
        cacheInvalidatorUtil.publishCacheInvalidation("groups" , "group" , "groupsLastUpdate" , "groupsLastUpdateById");

        Group group = findById(groupId);
        group.disassociatePermission(permissionService.findById(permissionId));
    }

    @Caching(evict = {
            @CacheEvict(value = "groups",            allEntries = true),
            @CacheEvict(value = "groups",             key = "#group.id"),
            @CacheEvict(value = "groupsLastUpdate",  allEntries = true),
            @CacheEvict(value = "groupsLastUpdateById", key = "#group.id")
    })
    @Transactional
    public void associateGroup (UUID userId , UUID groupId) {
        cacheInvalidatorUtil.publishCacheInvalidation("groups" , "group" , "groupsLastUpdate" , "groupsLastUpdateById");

        userService.findById(userId).associate(findById(groupId));
    }
    @Caching(evict = {
            @CacheEvict(value = "groups",            allEntries = true),
            @CacheEvict(value = "groups",             key = "#group.id"),
            @CacheEvict(value = "groupsLastUpdate",  allEntries = true),
            @CacheEvict(value = "groupsLastUpdateById", key = "#group.id")
    })
    @Transactional
    public void disassociateGroup (UUID userId , UUID groupId) {
        cacheInvalidatorUtil.publishCacheInvalidation("groups" , "group" , "groupsLastUpdate" , "groupsLastUpdateById");

        userService.findById(userId).dissociate(findById(groupId));
    }
}