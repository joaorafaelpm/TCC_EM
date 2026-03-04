package com.pendezzapizza.pendezzapizza_api.domain.service;

import com.pendezzapizza.pendezzapizza_api.domain.exception.BusinessException;
import com.pendezzapizza.pendezzapizza_api.domain.exception.EntityInUseException;
import com.pendezzapizza.pendezzapizza_api.domain.model.User;
import com.pendezzapizza.pendezzapizza_api.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class UserService  {

    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder ;


    public List<User> findAll() {
        return userRepository.findAll();
    }
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User findById(UUID id) {
        return userRepository.findByIdOrThrowException(id);
    }

    public OffsetDateTime getLastUpdateDate() {
        return userRepository.getLastUpdateDate();
    }

    public OffsetDateTime getLastUpdateDateById(UUID userId) {
        return userRepository.getLastUpdateDateById(userId);
    }

    @Transactional
    public User save(User user) {

        userRepository.detach(user);
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent() && !existingUser.get().equals(user)) {
            throw new BusinessException(String.format(
                    "The email '%s' is already in use", user.getEmail()
            ));
        }

        return userRepository.save(user);
    }

    @Transactional
    public void delete(UUID id) {
        try {
            userRepository.delete(findById(id));
            userRepository.flush();
        }
        catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(id);
        }
    }

    @Transactional
    public void changePassword(UUID id , String oldPassword , String newPassword) {
        User user = findById(id);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("Senhas não coincidem, por favor verifique de novo e tente novamente.");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
    }
}
