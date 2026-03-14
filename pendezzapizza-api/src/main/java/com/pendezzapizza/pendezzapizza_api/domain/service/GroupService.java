package com.pendezzapizza.pendezzapizza_api.domain.service;

import com.pendezzapizza.pendezzapizza_api.core.cache.CacheInvalidatorUtil;
import com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.action.GroupsActionCacheEvict;
import com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.action.PermissionsActionCacheEvict;
import com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.action.UsersActionCacheEvict;
import com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.save.GroupsSaveCacheEvict;
import com.pendezzapizza.pendezzapizza_api.domain.exception.EntityInUseException;
import com.pendezzapizza.pendezzapizza_api.domain.model.Group;
import com.pendezzapizza.pendezzapizza_api.domain.repository.GroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional(readOnly = true )
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

    @Cacheable(value = "group", key = "#groupId")
    public Group findById (UUID groupId ) {
        return groupRepository.findByIdOrThrowException(groupId);
    }

    @Cacheable(value = "userGroup", key = "#userId")
    public Set<Group> findGroupsByUserId (UUID userId) {
        return userService.findById(userId).getGroups();

    }

    @Cacheable("groupsLastUpdate")
    public OffsetDateTime getLastUpdateDate() {
        return groupRepository.getLastGroupUpdateDate();
    }

    @Cacheable(value = "groupsLastUpdateById", key = "#groupId")
    public OffsetDateTime getLastUpdateDateById(UUID groupId) {
        return groupRepository.getLastGroupUpdateDateById(groupId);
    }

    @GroupsSaveCacheEvict
    @Transactional
    public Group save (Group group) {
        return groupRepository.save(group);
    }

    @GroupsActionCacheEvict
    @Transactional
    public void deleteById (UUID groupId) {
        try {
            groupRepository.delete(findById(groupId));
            groupRepository.flush();
        }catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(groupId);
        }
    }

    @PermissionsActionCacheEvict
    @GroupsActionCacheEvict
    @Transactional
    public void associatePermission (UUID groupId , UUID permissionId) {
        Group group = findById(groupId);
        group.associatePermission(permissionService.findById(permissionId));
    }
    @PermissionsActionCacheEvict
    @GroupsActionCacheEvict
    @Transactional
    public void disassociatePermission (UUID groupId , UUID permissionId) {
        Group group = findById(groupId);
        group.disassociatePermission(permissionService.findById(permissionId));
    }

    @UsersActionCacheEvict
    @Transactional
    public void associateGroup (UUID userId , UUID groupId) {
        userService.findByIdGroupLazy(userId).associate(findById(groupId));
    }
    @UsersActionCacheEvict
    @Transactional
    public void disassociateGroup (UUID userId , UUID groupId) {
        userService.findByIdGroupLazy(userId).dissociate(findById(groupId));
    }
}