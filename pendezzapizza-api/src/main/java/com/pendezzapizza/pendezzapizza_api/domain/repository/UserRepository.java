package com.pendezzapizza.pendezzapizza_api.domain.repository;

import com.pendezzapizza.pendezzapizza_api.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
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

    @EntityGraph(attributePaths = {"groups"})
    Page<User> findAll (Pageable pageable);

    @Query("select max(u.updateDate) from User u")
    OffsetDateTime getLastUpdateDate();

    @Query("select max(u.updateDate) from User u where u.id = :userId")
    OffsetDateTime getLastUpdateDateById(UUID userId);

    @Query("SELECT u FROM User u left join fetch u.groups g WHERE u.id = :userId")
    Optional<User> findByIdGroupLazy (UUID userId) ;
}
