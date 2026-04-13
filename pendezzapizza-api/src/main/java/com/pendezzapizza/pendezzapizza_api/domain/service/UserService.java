package com.pendezzapizza.pendezzapizza_api.domain.service;

import com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.action.UsersActionCacheEvict;
import com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.save.UsersSaveCacheEvict;
import com.pendezzapizza.pendezzapizza_api.domain.exception.BusinessException;
import com.pendezzapizza.pendezzapizza_api.domain.exception.EntityInUseException;
import com.pendezzapizza.pendezzapizza_api.domain.exception.UserNotFoundException;
import com.pendezzapizza.pendezzapizza_api.domain.model.User;
import com.pendezzapizza.pendezzapizza_api.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class UserService  {

    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder ;

    @Cacheable("users")
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Cacheable(value = "user" , key = "#userId")
    public User findById(UUID userId) {
        return userRepository.findByIdOrThrowException(userId);
    }

    public User findByIdGroupLazy (UUID userId) {
        return userRepository.findByIdGroupLazy(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Cacheable("usersLastUpdate")
    public OffsetDateTime getLastUpdateDate() {
        return userRepository.getLastUpdateDate();
    }

    @Cacheable(value = "usersLastUpdateById", key = "#userId")
    public OffsetDateTime getLastUpdateDateById(UUID userId) {
        return userRepository.getLastUpdateDateById(userId);
    }

    @UsersSaveCacheEvict
    @Transactional
    public User save(User user) {

        userRepository.detach(user);
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent() && !existingUser.get().equals(user)) {
            throw new BusinessException(String.format(
                    "The email '%s' is already in use", user.getEmail()
            ));
        }

        // Mais seguro — usa o próprio encoder para detectar
        if (user.getPassword() != null && !isEncoded(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(user);
    }

    @UsersActionCacheEvict
    @Transactional
    public void delete(UUID userId) {
        try {
            userRepository.delete(findById(userId));
            userRepository.flush();
        }
        catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(userId);
        }
    }

    @UsersActionCacheEvict
    @Transactional
    public void changePassword(UUID userId , String oldPassword , String newPassword) {
        User user = findById(userId);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("Senhas não coincidem, por favor verifique de novo e tente novamente.");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
    }
    private boolean isEncoded(String password) {
        // Cobre $2a$, $2b$, $2y$ e qualquer versão futura do bcrypt
        return password.startsWith("$2");
    }
}
