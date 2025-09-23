package com.pendezzafood.pendezzapizza.api.controller;

import com.pendezzafood.pendezzapizza.domain.models.Permissao;
import com.pendezzafood.pendezzapizza.domain.services.PermissaoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<Permissao> buscarPermissao(@PathVariable Long id) {
        return ResponseEntity.ok(permissaoService.findById(id));
    }

    // POST: cria uma nova permissão
    @PostMapping
    public ResponseEntity<Permissao> criarPermissao(@RequestBody Permissao permissao) {
        Permissao novaPermissao = permissaoService.add(permissao);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaPermissao);
    }

    // PUT: atualiza uma permissão existente
    @PutMapping("/{id}")
    public ResponseEntity<Permissao> atualizarPermissao(@PathVariable Long id,
                                                        @RequestBody Permissao permissao) {
        Permissao atualizado = permissaoService.update(id, permissao);
        return ResponseEntity.ok(atualizado);
    }

    // DELETE: remove uma permissão pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPermissao(@PathVariable Long id) {
        permissaoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
