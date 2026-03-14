package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.UserModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler.UserDisassembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.DTO.PasswordDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.DTO.UserDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.DTO.UserWithPasswordDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.UserModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.User;
import com.pendezzapizza.pendezzapizza_api.domain.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    private UserModelAssembler userModelAssembler;
    private UserDisassembler userDisassembler;

    @GetMapping
    public CollectionModel<UserModel> findAll() {
        return userModelAssembler.toCollection(userService.findAll());
    }

    @GetMapping("/{userId}")
    public UserModel findById(@PathVariable UUID userId) {
        User user = userService.findById(userId);
        return userModelAssembler.toModel(user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserModel save(@RequestBody @Valid UserWithPasswordDTO userPasswordDTO) {
        User user = userDisassembler.userWithPasswordDTOToUser(userPasswordDTO);
        userService.save(user);
        return userModelAssembler.toModel(user);
    }

    @PutMapping("/{id}")
    public UserModel update(@PathVariable UUID id, @RequestBody @Valid UserDTO userDTO) {
        User existingUser = userService.findById(id);
        userDisassembler.updateUserFromDto(userDTO, existingUser);
        userService.save(existingUser);
        return userModelAssembler.toModel(existingUser);
    }

    @PutMapping("/{id}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePassword(@PathVariable UUID id, @RequestBody @Valid PasswordDTO passwordDTO) {
        User userWithOldPassword = userService.findById(id);
        userService.updatePassword(
                userWithOldPassword,
                passwordDTO.getCurrentPassword(),
                passwordDTO.getNewPassword()
        );
    }
}
