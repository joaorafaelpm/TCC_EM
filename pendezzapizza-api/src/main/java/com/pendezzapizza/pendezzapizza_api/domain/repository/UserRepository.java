package com.pendezzapizza.pendezzapizza_api.domain.repository;

import com.pendezzapizza.pendezzapizza_api.domain.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CustomJPARepository<User, UUID> {

    @Query("""
    select distinct u 
    from User u
    left join fetch u.groups g
    left join fetch g.permission
    where u.email = :email
    """)
    Optional<User> findByEmail(String email);

}
