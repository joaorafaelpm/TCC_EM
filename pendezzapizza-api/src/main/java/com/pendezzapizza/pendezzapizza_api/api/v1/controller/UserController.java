package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.UserModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler.UserDisassembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.UserModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.PasswordDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.UserDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.UserWithPasswordDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller.UserControllerOpenApi;
import com.pendezzapizza.pendezzapizza_api.core.security.CheckSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.model.User;
import com.pendezzapizza.pendezzapizza_api.domain.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path ="/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UserController implements UserControllerOpenApi {

    private final UserService userService;
    private final UserModelAssembler userModelAssembler;
    private final UserDisassembler userDisassembler;

    @CheckSecurity.UsersGroupsPermissions.CanConsult
    @GetMapping
    public CollectionModel<UserModel> findAll() {
        return userModelAssembler.toCollectionModel(userService.findAll());
    }

    @CheckSecurity.UsersGroupsPermissions.CanConsult
    @GetMapping("/{userId}")
    public UserModel findById(@PathVariable UUID userId) {
        User user = userService.findById(userId);
        return userModelAssembler.toModel(user);
    }

    @CheckSecurity.UsersGroupsPermissions.CanEdit
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserModel add(@RequestBody @Valid UserWithPasswordDTO userWithPasswordDTO) {
        User user = userDisassembler.userWithPasswordDTOToUser(userWithPasswordDTO);
        userService.save(user);
        return userModelAssembler.toModel(user);
    }

    @CheckSecurity.UsersGroupsPermissions.CanUpdateUser
    @PutMapping("/{userId}")
    public UserModel save(@PathVariable UUID userId, @RequestBody @Valid UserDTO userDTO) {
        User existingUser = userService.findById(userId);
        userDisassembler.updateUserFromDto(userDTO, existingUser);
        userService.save(existingUser);
        return userModelAssembler.toModel(existingUser);
    }

    @CheckSecurity.UsersGroupsPermissions.CanChangeOwnPassword
    @PutMapping("/{userId}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> updatePassword(@PathVariable UUID userId, @RequestBody @Valid PasswordDTO passwordDTO) {
        userService.changePassword(userId, passwordDTO.getCurrentPassword(), passwordDTO.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.UsersGroupsPermissions.CanUpdateUser
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> remove(@PathVariable UUID userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

}