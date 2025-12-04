package com.pendezzapizza.pendezzapizza_api.domain.service;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Set;

public interface SendEmailService {

    @Autowired
    void send(Message message) ;

    @Getter
    @Builder
    class Message {

//        Esse @Singular significa que ao invés de passar um novo objeto set ele simplifica para "destinatario" ou seja, ele cria uma string única, como no set, e deixa esse objeto adicionável como uma lista simplesmente passando outro destinatário
        @Singular
        private Set<String> recipients ;

        @NotNull
        private String subject  ;
        @NotNull
        private String body ;

//        O singular não sabe transformar variaveis de plural para singular (kkkkk burro pcrlh)
        @Singular("variable")
        private Map<String , Object> variables ;

    }

}
