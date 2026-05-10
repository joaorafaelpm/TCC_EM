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
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(path ="/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UserController implements UserControllerOpenApi {

    private final UserService userService;
    private final UserModelAssembler userModelAssembler;
    private final UserDisassembler userDisassembler;

    @CheckSecurity.UsersGroupsPermissions.CanConsult
    @GetMapping
    public ResponseEntity<Page<UserModel>> findAll(@RequestParam(required = false) String userName , Pageable pageable , ServletWebRequest request) {
        Page<User> users ;
        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
        String eTag = "0";
        OffsetDateTime lastUpdateDate = userService.getLastUpdateDate();
        if (lastUpdateDate != null) {
            eTag = String.valueOf(lastUpdateDate.toEpochSecond());
        }

        if (request.checkNotModified(eTag)) {
            return null;
        }

        if (userName == null) {
            users = userService.findAll(pageable);
        }
        else {
            users = userService.findAllByName(userName, pageable);
        }

        Page<UserModel> usersModel = users.map(userModelAssembler::toModel);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(usersModel);
    }

    @CheckSecurity.UsersGroupsPermissions.CanConsult
    @GetMapping("/{userId}")
    public ResponseEntity<UserModel> findById(@PathVariable UUID userId , ServletWebRequest request) {
        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
        String eTag = "0";
        OffsetDateTime lastUpdateDate = userService.getLastUpdateDateById(userId);
        if (lastUpdateDate != null) {
            eTag = String.valueOf(lastUpdateDate.toEpochSecond());
        }

        if (request.checkNotModified(eTag)) {
            return null;
        }

        User user = userService.findById(userId);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(userModelAssembler.toModel(user));
    }

    @CheckSecurity.UsersGroupsPermissions.CanEdit
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserModel add(@RequestBody @Valid UserWithPasswordDTO userWithPasswordDTO) {
        User user = userDisassembler.userWithPasswordDTOToUser(userWithPasswordDTO);
        userService.save(user);
        return userModelAssembler.toModel(user);
    }

    @PostMapping("/register")
    public void register(@RequestBody @Valid UserWithPasswordDTO userWithPasswordDTO,
                         HttpServletResponse response) throws IOException {
        User user = userDisassembler.userWithPasswordDTOToUser(userWithPasswordDTO);
        userService.save(user);
        response.sendRedirect("/oauth2/iniciar-login");
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