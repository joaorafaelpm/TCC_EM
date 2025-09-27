package com.pendezzafood.pendezzapizza.domain.services;

import com.pendezzafood.pendezzapizza.domain.exceptions.NotFoundException;
import com.pendezzafood.pendezzapizza.domain.models.Estado;
import com.pendezzafood.pendezzapizza.domain.repositories.EstadoRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class EstadoService {
    private final EstadoRepo estadoRepo;

    public List<Estado> findAll() {
        return estadoRepo.findAll();
    }

    public Estado findById(UUID id) {
        return estadoRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Estado de id '%s' não encontrado!", id)
                ));
    }

    public Estado save(Estado grupo) {
        return estadoRepo.save(grupo);
    }

    public Estado save(UUID id, Estado grupo) {
        Estado existente = findById(id);

        return estadoRepo.save(existente);
    }

    public void deleteById(UUID id) {
        Estado existente = findById(id);
        estadoRepo.delete(existente);
    }
}
