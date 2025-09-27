package com.pendezzafood.pendezzapizza.domain.services;

import com.pendezzafood.pendezzapizza.domain.exceptions.NotFoundException;
import com.pendezzafood.pendezzapizza.domain.models.Estado;
import com.pendezzafood.pendezzapizza.domain.models.Cidade;
import com.pendezzafood.pendezzapizza.domain.repositories.EstadoRepo;
import com.pendezzafood.pendezzapizza.domain.repositories.CidadeRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class CadastroCidadeService {

    CidadeRepo cidadeRepo;
    EstadoRepo estadoRepo;

    public List<Cidade> findAll() {
        return cidadeRepo.findAll();
    }

    public Cidade findById(UUID id) {
        return cidadeRepo.findById(id)
            .orElseThrow(() -> new NotFoundException(
                String.format("Cidades de id '%s' não encontrado!", id)
            ));
    }

    public Cidade save (Cidade cidade) {
        Estado estado = estadoRepo.findById(cidade.getEstado().getId()).orElseThrow(
                () -> new NotFoundException(
                    String.format("Não foi encontrado um estado com id de %d!" , cidade.getEstado().getId())
            ));

        cidade.setEstado(estado);
        return cidadeRepo.save(cidade);
    }

    public void remove (UUID id) {
            cidadeRepo.findById(id).orElseThrow(() ->
                    new NotFoundException(
                        String.format("Não foi encontrada cidade de id %d" , id)
                ));
    }

}