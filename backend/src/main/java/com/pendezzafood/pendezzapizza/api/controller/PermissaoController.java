package com.pendezzafood.pendezzapizza.api.controller;

import com.pendezzafood.pendezzapizza.domain.models.Permissao;
import com.pendezzafood.pendezzapizza.domain.services.PermissaoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/permissoes")
@AllArgsConstructor
public class PermissaoController {
    private final PermissaoService permissaoService;

    // GET: lista todas as permissões
    @GetMapping
    public List<Permissao> listarPermissoes() {
        return permissaoService.findAll();
    }

    // GET: busca uma permissão pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Permissao> buscarPermissao(@PathVariable UUID id) {
        return ResponseEntity.ok(permissaoService.findById(id));
    }

    // POST: cria uma nova permissão
    @PostMapping
    public ResponseEntity<Permissao> criarPermissao(@RequestBody Permissao permissao) {
        Permissao novaPermissao = permissaoService.save(permissao);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaPermissao);
    }

    // PUT: atualiza uma permissão existente
    @PutMapping("/{id}")
    public ResponseEntity<Permissao> atualizarPermissao(@PathVariable UUID id,
                                                        @RequestBody Permissao permissao) {
        Permissao atualizado = permissaoService.save(id, permissao);
        return ResponseEntity.ok(atualizado);
    }

    // DELETE: remove uma permissão pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPermissao(@PathVariable UUID id) {
        permissaoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
