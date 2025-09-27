package com.pendezzafood.pendezzapizza.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pendezzafood.pendezzapizza.domain.models.Usuario;
import com.pendezzafood.pendezzapizza.domain.services.UsuarioService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
@AllArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    // GET: lista todos os usuários
    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioService.findAll();
    }

    // GET: busca um usuário pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuario(@PathVariable UUID id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    // POST: cria um novo usuário
    @PostMapping
    public ResponseEntity<Usuario> criarUsuario(@RequestBody Usuario usuario) {
        Usuario novoUsuario = usuarioService.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    // PUT: atualiza um usuário existente
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable UUID id,
                                                    @RequestBody Usuario usuario) {
        Usuario atualizado = usuarioService.save(id, usuario);
        return ResponseEntity.ok(atualizado);
    }

    // DELETE: remove um usuário pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable UUID id) {
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
