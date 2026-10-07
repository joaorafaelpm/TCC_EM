package com.pendezzapizza.pendezzapizza_api.domain.repository;

import com.pendezzapizza.pendezzapizza_api.domain.model.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface criada para fazer a ponte entre banco e entidade de <b>grupo</b>
 *
 * <p>Toda query sql a mais está implementada dentro da função com seu código sql explicado  </p>
 */
@Repository
public interface GroupRepository extends CustomJPARepository<Group ,  UUID> {

//    EntityGraph é usado para durante a requisição receber esse objeto primeiro e é uma solução um pouco mais refinadas que definir o Fetch como LAZY ou EAGER na própria entidade
    @EntityGraph(attributePaths = {"permission"})
    Page<Group> findAll(Pageable pageable);

    Optional<Group> findByName (String name);

//    Seleciona ultima data de atualização de todos os grupos
    @Query("select max(g.updateDate) from Group g")
    OffsetDateTime getLastGroupUpdateDate();

//    Seleciona ultima data de atualização de um grupo específico
    @Query("select max(c.updateDate) from Group c where c.id = :groupId")
    OffsetDateTime getLastGroupUpdateDateById(UUID groupId);

}
