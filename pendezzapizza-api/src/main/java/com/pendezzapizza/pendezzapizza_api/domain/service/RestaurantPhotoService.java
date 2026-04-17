package com.pendezzapizza.pendezzapizza_api.domain.service;

import com.pendezzapizza.pendezzapizza_api.domain.exception.RestaurantPhotoNotFoundException;
import com.pendezzapizza.pendezzapizza_api.domain.model.RestaurantPhoto;
import com.pendezzapizza.pendezzapizza_api.domain.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RestaurantPhotoService {

    private RestaurantRepository restaurantRepository;

    private PhotoStorageService photoStorageService;

    @Transactional
    public RestaurantPhoto findById (UUID restaurantId) {
        return restaurantRepository.findRestaurantPhotoById(restaurantId).orElseThrow(() ->
                new RestaurantPhotoNotFoundException(restaurantId));
    }

    @Transactional
    public RestaurantPhoto save (RestaurantPhoto photo , InputStream fileData) {
        UUID restaurantId = photo.getRestaurantId();
        String newFileName = photoStorageService.generateFileName(photo.getFileName());

        Optional<RestaurantPhoto> existingPhoto = restaurantRepository.findRestaurantPhotoById(restaurantId);

        String existingFileName = null ;

        if (existingPhoto.isPresent()) {
            existingFileName = existingPhoto.get().getFileName();
            restaurantRepository.deletePhoto(existingPhoto.get());
        }

        photo.setFileName(newFileName);
        RestaurantPhoto savedPhoto = restaurantRepository.savePhoto(photo);
        restaurantRepository.flush();

        PhotoStorageService.NewPhoto newPhoto = PhotoStorageService.NewPhoto.builder()
                .fileName(photo.getFileName())
                .inputStream(fileData)
                .build();

        photoStorageService.replace(existingFileName , newPhoto);

        return savedPhoto;
    }

    @Transactional
    public void delete (UUID restaurantId) {
        RestaurantPhoto existingPhoto = findById(restaurantId);
        String existingFileName = existingPhoto.getFileName();
        photoStorageService.remove(existingFileName);
        restaurantRepository.deletePhoto(existingPhoto);
        restaurantRepository.flush();
    }


}