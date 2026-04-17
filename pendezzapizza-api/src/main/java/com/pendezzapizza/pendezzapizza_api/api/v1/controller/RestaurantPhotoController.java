package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.RestaurantPhotoModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler.RestaurantPhotoDisassembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.PhotoModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.PhotoDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller.RestaurantPhotoControllerOpenApi;
import com.pendezzapizza.pendezzapizza_api.core.security.CheckSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.exception.EntityNotFoundException;
import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;
import com.pendezzapizza.pendezzapizza_api.domain.model.RestaurantPhoto;
import com.pendezzapizza.pendezzapizza_api.domain.service.PhotoStorageService;
import com.pendezzapizza.pendezzapizza_api.domain.service.RestaurantPhotoService;
import com.pendezzapizza.pendezzapizza_api.domain.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(path ="/v1/restaurants/{restaurantId}/photo")
@AllArgsConstructor
public class RestaurantPhotoController implements RestaurantPhotoControllerOpenApi {

    private final RestaurantPhotoService restaurantPhotoService;
    private final RestaurantService restaurantService;
    private final PhotoStorageService photoStorageService;
    private final RestaurantPhotoModelAssembler photoAssembler;
    private final RestaurantPhotoDisassembler photoDisassembler;

    @CheckSecurity.Restaurants.CanConsult
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PhotoModel> findPhoto(@PathVariable UUID restaurantId, ServletWebRequest request) {
        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
        String eTag = "0";
        OffsetDateTime lastUpdateDate = restaurantService.getLastUpdateDateById(restaurantId);
        if (lastUpdateDate != null) {
            eTag = String.valueOf(lastUpdateDate.toEpochSecond());
        }

        if (request.checkNotModified(eTag)) {
            return null;
        }
        PhotoModel model = photoAssembler.toModel(restaurantPhotoService.findById(restaurantId));
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(model);
    }

    @GetMapping
    public ResponseEntity<InputStreamResource> servePhoto(@PathVariable UUID restaurantId,
                                                          @RequestHeader(name = "accept") String acceptHeaders) throws HttpMediaTypeNotAcceptableException {
        try {
            RestaurantPhoto restaurantPhoto = restaurantPhotoService.findById(restaurantId);
            MediaType mediaType = MediaType.parseMediaType(restaurantPhoto.getContentType());
            List<MediaType> acceptedMediaTypes = MediaType.parseMediaTypes(acceptHeaders);

            verifyMediaTypeCompatibility(mediaType, acceptedMediaTypes);

            InputStream inputStream = photoStorageService.retrieve(restaurantPhoto.getFileName());

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(new InputStreamResource(inputStream));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private void verifyMediaTypeCompatibility(MediaType photoMediaType, List<MediaType> acceptedMediaTypes) throws HttpMediaTypeNotAcceptableException {
        boolean compatible = acceptedMediaTypes.stream()
                .anyMatch(acceptedMediaType -> acceptedMediaType.isCompatibleWith(photoMediaType));

        if (!compatible) {
            throw new HttpMediaTypeNotAcceptableException(acceptedMediaTypes);
        }
    }

    @CheckSecurity.Restaurants.CanManageOperation
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PhotoModel updatePhoto(@PathVariable UUID restaurantId,
                                         @Valid PhotoDTO photoDTO) throws IOException {
        Restaurant restaurant = restaurantService.findById(restaurantId);
        MultipartFile file = photoDTO.getFile();

        RestaurantPhoto photo = photoDisassembler.photoRestaurantDTOToRestaurantPhoto(photoDTO);
        photo.setRestaurant(restaurant);

        RestaurantPhoto savedPhoto = restaurantPhotoService.save(photo, file.getInputStream());
        return photoAssembler.toModel(savedPhoto);
    }

    @CheckSecurity.Restaurants.CanManageOperation
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> removePhoto(@PathVariable UUID restaurantId) {
        restaurantPhotoService.delete(restaurantId);
        return ResponseEntity.noContent().build();
    }
}