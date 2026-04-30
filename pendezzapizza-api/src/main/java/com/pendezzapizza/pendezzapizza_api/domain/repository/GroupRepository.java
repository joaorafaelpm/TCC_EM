package com.pendezzapizza.pendezzapizza_api.domain.repository;

import com.pendezzapizza.pendezzapizza_api.domain.model.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.UUID;

@Repository
public interface GroupRepository extends CustomJPARepository<Group ,  UUID> {

    @EntityGraph(attributePaths = {"permission"})
    Page<Group> findAll(Pageable pageable);

    @Query("select max(g.updateDate) from Group g")
    OffsetDateTime getLastGroupUpdateDate();

    @Query("select max(c.updateDate) from Group c where c.id = :groupId")
    OffsetDateTime getLastGroupUpdateDateById(UUID groupId);

}
