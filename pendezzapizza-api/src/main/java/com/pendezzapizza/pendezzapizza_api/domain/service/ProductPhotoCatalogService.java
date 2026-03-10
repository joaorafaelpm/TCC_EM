package com.pendezzapizza.pendezzapizza_api.domain.service;

import com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.action.ProductsActionCacheEvict;
import com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.save.ProductsSaveCacheEvict;
import com.pendezzapizza.pendezzapizza_api.domain.exception.ProductPhotoNotFoundException;
import com.pendezzapizza.pendezzapizza_api.domain.model.ProductPhoto;
import com.pendezzapizza.pendezzapizza_api.domain.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductPhotoCatalogService {

    private ProductRepository productRepository;

    private PhotoStorageService photoStorageService;

    @Transactional
    public ProductPhoto findById (UUID restaurantId , UUID productId) {
        return productRepository.findProductPhotoById(restaurantId , productId).orElseThrow(() ->
                new ProductPhotoNotFoundException(restaurantId , productId));
    }

    @Transactional
    public ProductPhoto save (ProductPhoto photo , InputStream fileData) {
        UUID restaurantId = photo.getRestaurantId();
        UUID productId = photo.getProduct().getId();
        String newFileName = photoStorageService.generateFileName(photo.getFileName());

        Optional<ProductPhoto> existingPhoto = productRepository.findProductPhotoById(restaurantId, productId);

        String existingFileName = null ;

        if (existingPhoto.isPresent()) {
            existingFileName = existingPhoto.get().getFileName();
            productRepository.delete(existingPhoto.get());
        }

        photo.setFileName(newFileName);
        ProductPhoto savedPhoto = productRepository.save(photo);
        productRepository.flush();

        PhotoStorageService.NewPhoto newPhoto = PhotoStorageService.NewPhoto.builder()
                .fileName(photo.getFileName())
                .inputStream(fileData)
                .build();

        photoStorageService.replace(existingFileName , newPhoto);

        return savedPhoto;
    }

    @Transactional
    public void delete (UUID restaurantId , UUID productId) {
        ProductPhoto existingPhoto = findById(restaurantId, productId);
        String existingFileName = existingPhoto.getFileName();
        photoStorageService.remove(existingFileName);
        productRepository.delete(existingPhoto);
        productRepository.flush();
    }


}