package com.pendezzafood.pendezzapizza.api.controller;

import com.pendezzafood.pendezzapizza.domain.models.Usuario;
import com.pendezzafood.pendezzapizza.domain.services.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/usuarios-grupos/{usuarioId}")
@AllArgsConstructor
public class UsuarioGrupoController {

    private final UsuarioService usuarioService;

    // PUT: adiciona um grupo ao usuário
    @PutMapping("/adicionar")
    public ResponseEntity<Usuario> adicionarGrupo(
            @PathVariable UUID usuarioId,
            @RequestParam Long grupoId) {

        Usuario usuarioAtualizado = usuarioService.addGrupoToUsuario(usuarioId, grupoId);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    // PUT: remove um grupo do usuário
    @PutMapping("/remover")
    public ResponseEntity<Usuario> removerGrupo(
            @PathVariable UUID usuarioId,
            @RequestParam Long grupoId) {

        Usuario usuarioAtualizado = usuarioService.removeGrupoFromUsuario(usuarioId, grupoId);
        return ResponseEntity.ok(usuarioAtualizado);
    }
}
