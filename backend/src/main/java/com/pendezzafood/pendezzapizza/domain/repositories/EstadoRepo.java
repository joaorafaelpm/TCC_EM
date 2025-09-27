package com.pendezzafood.pendezzapizza.domain.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pendezzafood.pendezzapizza.domain.models.Estado;

@Repository
public interface EstadoRepo extends JpaRepository<Estado , UUID> {

}
