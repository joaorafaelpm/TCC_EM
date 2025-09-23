package com.pendezzafood.pendezzapizza.domain.services;

import com.pendezzafood.pendezzapizza.domain.exceptions.NotFoundException;
import com.pendezzafood.pendezzapizza.domain.models.Grupo;
import com.pendezzafood.pendezzapizza.domain.models.Usuario;
import com.pendezzafood.pendezzapizza.domain.repositories.GrupoRepo;
import com.pendezzafood.pendezzapizza.domain.repositories.UsuarioRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class UsuarioService {
    private final UsuarioRepo usuarioRepo;
    private final GrupoRepo grupoRepo;

    public List<Usuario> findAll() {
        return usuarioRepo.findAll();
    }

    public Usuario findById(UUID id) {
        return usuarioRepo.findById(id)
            .orElseThrow(() -> new NotFoundException(
                    String.format("Usuário de id '%s' não encontrado!", id)
            ));
    }

    public Usuario add(Usuario usuario) {
        if (usuarioRepo.existsByEmail(usuario.getEmail())) {
            throw new NotFoundException("Email já está em uso!");
        }
        return usuarioRepo.save(usuario);
    }

    public Usuario update(UUID id, Usuario usuario) {
        Usuario existente = findById(id);

        if (usuario.getNome() != null) {
            existente.setNome(usuario.getNome());
        }
        if (usuario.getSenha() != null) {
            existente.setSenha(usuario.getSenha());
        }
        if (usuario.getEmail() != null && !usuario.getEmail().equals(existente.getEmail())) {
            if (usuarioRepo.existsByEmail(usuario.getEmail())) {
                throw new NotFoundException("Email já está em uso!");
            }
            existente.setEmail(usuario.getEmail());
        }
        return usuarioRepo.save(existente);
    }

    public void deleteById(UUID id) {
        Usuario existente = findById(id);
        usuarioRepo.delete(existente);
    }

    // Grupos:
    
    public Usuario addGrupoToUsuario(UUID usuarioId, Long grupoId) {
        Usuario usuario = findById(usuarioId);
        Grupo grupo = grupoRepo.findById(grupoId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Grupo de id '%s' não encontrado!", grupoId)
                ));

        if (!usuario.getGrupos().contains(grupo)) {
            usuario.getGrupos().add(grupo);
        }

        return usuarioRepo.save(usuario);
    }

    public Usuario removeGrupoFromUsuario(UUID usuarioId, Long grupoId) {
        Usuario usuario = findById(usuarioId);
        Grupo grupo = grupoRepo.findById(grupoId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Grupo de id '%s' não encontrado!", grupoId)
                ));

        usuario.getGrupos().remove(grupo);
        return usuarioRepo.save(usuario);
    }
}
