package com.pendezzafood.pendezzapizza.domain.services;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.pendezzafood.pendezzapizza.domain.exceptions.NotFoundException;
import com.pendezzafood.pendezzapizza.domain.exceptions.UsedEntityException;
import com.pendezzafood.pendezzapizza.domain.models.Restaurante;
import com.pendezzafood.pendezzapizza.domain.repositories.RestauranteRepo;

@Service
@AllArgsConstructor
public class RestauranteService {

    RestauranteRepo restauranteRepo;

    public List<Restaurante> findAll() {
        return restauranteRepo.findAll();
    }

    public Restaurante findById(UUID id) {
        return restauranteRepo.findById(id)
            .orElseThrow(() -> new NotFoundException(
                String.format("Restaurante de id '%s' não encontrado!", id)
            ));
    }

    public void remove (UUID id) {
        try {
            restauranteRepo.deleteById(id);
        }
        catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(
                    String.format("Restaurante de código %d não foi encontrada!" , id)
            );
        }
        catch (DataIntegrityViolationException e) {
            throw new UsedEntityException(
                    String.format("Restaurante de código %d tem produtos ativos, logo, não pode ser removida!" , id)
            ) ;
        }
    }



}

