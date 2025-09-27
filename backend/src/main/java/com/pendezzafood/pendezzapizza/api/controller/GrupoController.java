package com.pendezzafood.pendezzapizza.api.controller;

import com.pendezzafood.pendezzapizza.domain.models.Grupo;
import com.pendezzafood.pendezzapizza.domain.services.GrupoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/grupos")
@AllArgsConstructor
public class GrupoController {
    private final GrupoService grupoService;

    // GET: lista todos os grupos
    @GetMapping
    public List<Grupo> listarGrupos() {
        return grupoService.findAll();
    }

    // GET: busca um grupo pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Grupo> buscarGrupo(@PathVariable UUID id) {
        return ResponseEntity.ok(grupoService.findById(id));
    }

    // POST: cria um novo grupo
    @PostMapping
    public ResponseEntity<Grupo> criarGrupo(@RequestBody Grupo grupo) {
        Grupo novoGrupo = grupoService.save(grupo);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoGrupo);
    }

    // PUT: atualiza um grupo existente
    @PutMapping("/{id}")
    public ResponseEntity<Grupo> atualizarGrupo(@PathVariable UUID id,
                                                @RequestBody Grupo grupo) {
        Grupo atualizado = grupoService.save(id, grupo);
        return ResponseEntity.ok(atualizado);
    }

    // DELETE: remove um grupo pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarGrupo(@PathVariable UUID id) {
        grupoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
