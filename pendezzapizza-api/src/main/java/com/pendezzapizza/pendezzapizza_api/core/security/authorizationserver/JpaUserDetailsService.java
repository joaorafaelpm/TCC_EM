package com.pendezzapizza.pendezzapizza_api.core.security.authorizationserver;

import com.pendezzapizza.pendezzapizza_api.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository ;

//    Isso no contexto do JPA impede que ele feche a transação logo após consultar o nome e extenda isso até nós pegarmos a lista de grupo dentro da entidade
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.pendezzapizza.pendezzapizza_api.domain.model.User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com e-mail informado!"));
        return new User(user.getEmail() , user.getPassword(), getAuthorities(user));
    }

    private Collection<GrantedAuthority> getAuthorities (com.pendezzapizza.pendezzapizza_api.domain.model.User user) {
        return user.getGroups().stream()
                .flatMap(group -> group.getPermission().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getName())))
                .collect(Collectors.toSet());
    }
}
