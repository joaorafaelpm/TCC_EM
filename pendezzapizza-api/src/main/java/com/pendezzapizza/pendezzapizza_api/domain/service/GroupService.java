package com.pendezzapizza.pendezzapizza_api.domain.service;

import com.pendezzapizza.pendezzapizza_api.domain.exception.EntityInUseException;
import com.pendezzapizza.pendezzapizza_api.domain.model.Group;
import com.pendezzapizza.pendezzapizza_api.domain.repository.GroupRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository ;

    private final PermissionService permissionService;
    private final UserService userService;

    public List<Group> findAll () {
        return groupRepository.findAll();
    }

    public Group findById (UUID id ) {
        return groupRepository.findByIdOrThrowException(id);
    }

    @Transactional
    public Group save (Group group) {
        return groupRepository.save(group);
    }

    @Transactional
    public void deleteById (UUID id) {
        try {
            groupRepository.delete(findById(id));
            groupRepository.flush();
        }catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(id);
        }
    }

    @Transactional
    public void associatePermission (UUID groupId , UUID permissionId) {
        Group group = findById(groupId);
        group.associatePermission(permissionService.findById(permissionId));
    }
    @Transactional
    public void disassociatePermission (UUID groupId , UUID permissionId) {
        Group group = findById(groupId);
        group.disassociatePermission(permissionService.findById(permissionId));
    }

    @Transactional
    public void associateGroup (UUID userId , UUID groupId) {
        userService.findById(userId).associate(findById(groupId));
    }
    @Transactional
    public void disassociateGroup (UUID userId , UUID groupId) {
        userService.findById(userId).dissociate(findById(groupId));
    }
}