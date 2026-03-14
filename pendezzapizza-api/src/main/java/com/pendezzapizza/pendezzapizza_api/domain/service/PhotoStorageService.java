package com.pendezzapizza.pendezzapizza_api.domain.service;

import lombok.Builder;
import lombok.Getter;

import java.io.InputStream;
import java.util.UUID;

public interface PhotoStorageService {

    InputStream retrieve(String fileName);

    void store(NewPhoto newPhoto);

    void remove(String fileName);

    default void replace(String existingFileName, NewPhoto newPhoto) {
        // As regras de negócio para substituir uma imagem por outra é, primeiro armazenar, e se já existir, remover a antiga
        // É por isso que nós só atribuimos o UUID quando nós vamos guardar a imagem, e não aqui na função de armazenar, serve para nós mantermos o controle das classes/arquivos
        this.store(newPhoto);

        if (existingFileName != null) {
            remove(existingFileName);
        }
    }

    default String generateFileName(String originalName) {
        return UUID.randomUUID() + "_" + originalName;
    }

    @Getter
    @Builder
    class NewPhoto {
        private String fileName;
        private InputStream inputStream;
    }

}
