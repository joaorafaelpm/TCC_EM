/**
 * @summary     Implementação local do serviço de armazenamento de fotos, gravando arquivos
 *              diretamente no sistema de arquivos da máquina onde a aplicação está rodando.
 * @difficulty  Low
 * @depends-on  None
 */
package com.pendezzapizza.pendezzapizza_api.infrastructure.storage;

import com.pendezzapizza.pendezzapizza_api.domain.service.PhotoStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class LocalPhotoStorageService implements PhotoStorageService {

    /**
     * Diretório base onde as fotos serão armazenadas, configurado via propriedade externa.
     * O tipo {@code Path} já é suportado diretamente pelo conversor do Spring para {@code @Value}.
     */
    @Value(value = "${pendezza-pizza.storage.local.direction.diretorio-fotos}")
    private Path photosDirectory;

    /**
     * Armazena um novo arquivo de foto no diretório local configurado.
     * Utiliza {@code FileCopyUtils} para garantir que o stream de entrada seja
     * copiado e fechado corretamente, mesmo em caso de falha parcial.
     *
     * @param newPhoto objeto contendo o nome do arquivo e o stream de dados a ser gravado
     * @throws StorageException se ocorrer qualquer erro de I/O durante a gravação
     */
    @Override
    public void store(NewPhoto newPhoto) {
        try {
            Path filePath = getFilePath(newPhoto.getFileName());
            FileCopyUtils.copy(newPhoto.getInputStream(), Files.newOutputStream(filePath));
        } catch (Exception e) {
            throw new StorageException("Não foi possivel encontrar o arquivo", e);
        }
    }

    /**
     * Remove o arquivo correspondente ao nome informado do diretório local.
     * Utiliza {@code deleteIfExists} para que a operação seja idempotente —
     * não lança erro caso o arquivo já tenha sido removido anteriormente.
     *
     * @param fileName nome do arquivo a ser removido
     * @throws StorageException se ocorrer um erro de I/O ao tentar deletar o arquivo
     */
    @Override
    public void remove(String fileName) {
        try {
            Path filePath = getFilePath(fileName);
            Files.deleteIfExists(filePath);
        } catch (Exception e) {
            throw new StorageException("Não foi possivel deletar o arquivo", e);
        }
    }

    /**
     * Recupera o conteúdo de um arquivo como stream de leitura.
     * O chamador é responsável por fechar o {@code InputStream} retornado
     * para evitar vazamento de recursos.
     *
     * @param fileName nome do arquivo a ser recuperado
     * @return stream de leitura do arquivo localizado no diretório configurado
     * @throws StorageException se o arquivo não existir ou ocorrer um erro de I/O na leitura
     */
    @Override
    public InputStream retrieve(String fileName) {
        try {
            Path filePath = getFilePath(fileName);
            return Files.newInputStream(filePath);
        } catch (Exception e) {
            throw new StorageException("Não foi possivel recuperar o arquivo", e);
        }
    }

    /**
     * Resolve o caminho absoluto de um arquivo dentro do diretório de fotos configurado.
     * Centraliza a construção do path para garantir consistência entre os métodos públicos.
     */
    private Path getFilePath(String fileName) {
        return photosDirectory.resolve(Path.of(fileName));
    }
}