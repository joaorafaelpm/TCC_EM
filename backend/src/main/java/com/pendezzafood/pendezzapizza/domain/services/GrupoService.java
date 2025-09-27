package com.pendezzafood.pendezzapizza.domain.services;

import com.pendezzafood.pendezzapizza.domain.exceptions.NotFoundException;
import com.pendezzafood.pendezzapizza.domain.models.Grupo;
import com.pendezzafood.pendezzapizza.domain.repositories.GrupoRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class GrupoService {
    private final GrupoRepo grupoRepo;

    public List<Grupo> findAll() {
        return grupoRepo.findAll();
    }

    public Grupo findById(UUID id) {
        return grupoRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Grupo de id '%s' não encontrado!", id)
                ));
    }

    public Grupo save(Grupo grupo) {
        return grupoRepo.save(grupo);
    }

    public Grupo save(UUID id, Grupo grupo) {
        Grupo existente = findById(id);

        if (grupo.getNome() != null) {
            existente.setNome(grupo.getNome());
        }

        if (grupo.getPermissoes() != null && !grupo.getPermissoes().isEmpty()) {
            existente.setPermissoes(grupo.getPermissoes());
        }

        return grupoRepo.save(existente);
    }

    public void deleteById(UUID id) {
        Grupo existente = findById(id);
        grupoRepo.delete(existente);
    }
}
