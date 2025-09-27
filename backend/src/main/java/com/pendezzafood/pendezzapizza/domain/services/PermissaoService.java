package com.pendezzafood.pendezzapizza.domain.services;

import com.pendezzafood.pendezzapizza.domain.exceptions.NotFoundException;
import com.pendezzafood.pendezzapizza.domain.models.Permissao;
import com.pendezzafood.pendezzapizza.domain.repositories.PermissaoRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class PermissaoService {
    private final PermissaoRepo permissaoRepo;

    public List<Permissao> findAll() {
        return permissaoRepo.findAll();
    }

    public Permissao findById(UUID id) {
        return permissaoRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Permissão de id '%s' não encontrada!", id)
                ));
    }

    public Permissao save(Permissao permissao) {
        return permissaoRepo.save(permissao);
    }

    public Permissao save(UUID id, Permissao permissao) {
        Permissao existente = findById(id);

        if (permissao.getNome() != null) {
            existente.setNome(permissao.getNome());
        }
        if (permissao.getDescricao() != null) {
            existente.setDescricao(permissao.getDescricao());
        }

        return permissaoRepo.save(existente);
    }

    public void deleteById(UUID id) {
        Permissao existente = findById(id);
        permissaoRepo.delete(existente);
    }
}
