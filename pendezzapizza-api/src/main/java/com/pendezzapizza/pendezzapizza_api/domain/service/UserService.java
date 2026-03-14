package com.pendezzapizza.pendezzapizza_api.domain.service;

import com.pendezzapizza.pendezzapizza_api.domain.exception.BusinessException;
import com.pendezzapizza.pendezzapizza_api.domain.exception.EntityInUseException;
import com.pendezzapizza.pendezzapizza_api.domain.model.User;
import com.pendezzapizza.pendezzapizza_api.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService  {

    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(UUID id) {
        return userRepository.findByIdOrThrowException(id);
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
            userRepository.deleteById(id);
            userRepository.flush();
        }
        catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(id);
        }
    }

    @Transactional
    public void updatePassword(User user, String currentPassword, String newPassword) {
        if (user.passwordDoesNotMatch(currentPassword)) {
            throw new BusinessException("Senhas não coincidem, verifique e tente novamente.");
        }

        if (user.passwordMatches(currentPassword)) {
            user.setPassword(newPassword);
        }

        userRepository.save(user);
    }
}
