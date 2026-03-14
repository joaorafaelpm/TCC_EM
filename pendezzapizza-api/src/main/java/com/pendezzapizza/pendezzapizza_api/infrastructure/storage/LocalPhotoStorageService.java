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


    @Value(value = "${pendezza-pizza.storage.local.direction.diretorio-fotos}")
    private Path photosDirectory ;


    @Override
    public void store(NewPhoto newPhoto) {
        try {
            Path filePath = getFilePath(newPhoto.getFileName());
            FileCopyUtils.copy(newPhoto.getInputStream() , Files.newOutputStream(filePath));
        } catch (Exception e) {
            throw new StorageException("Não foi possivel encontrar o arquivo", e);
        }
    }

    @Override
    public void remove(String fileName) {
        try {
            Path filePath = getFilePath(fileName);
            Files.deleteIfExists(filePath);
        } catch (Exception e) {
            throw new StorageException("Não foi possivel deletar o arquivo" , e);
        }
    }

    @Override
    public InputStream retrieve(String fileName) {
        try {
            Path filePath = getFilePath(fileName);
            return Files.newInputStream(filePath);
        } catch (Exception e) {
            throw new StorageException("Não foi possivel recuperar o arquivo" , e);
        }
    }

    private Path getFilePath(String fileName) {
        return photosDirectory.resolve(Path.of(fileName));
    }
}