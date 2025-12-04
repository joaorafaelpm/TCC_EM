package com.pendezzapizza.pendezzapizza_api.domain.repository;

import com.pendezzapizza.pendezzapizza_api.domain.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CustomJPARepository<User, UUID> {

    Optional<User> findByEmail (String email) ;

}
