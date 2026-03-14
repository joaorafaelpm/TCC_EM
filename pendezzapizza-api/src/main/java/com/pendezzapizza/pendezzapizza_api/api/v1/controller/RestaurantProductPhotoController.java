package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.ProductPhotoModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler.ProductPhotoDisassembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.ProductPhotoModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.ProductPhotoDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller.RestaurantProductPhotoControllerOpenApi;
import com.pendezzapizza.pendezzapizza_api.core.security.CheckSecurity;
import com.pendezzapizza.pendezzapizza_api.domain.exception.EntityNotFoundException;
import com.pendezzapizza.pendezzapizza_api.domain.model.Product;
import com.pendezzapizza.pendezzapizza_api.domain.model.ProductPhoto;
import com.pendezzapizza.pendezzapizza_api.domain.service.PhotoStorageService;
import com.pendezzapizza.pendezzapizza_api.domain.service.ProductPhotoCatalogService;
import com.pendezzapizza.pendezzapizza_api.domain.service.ProductService;
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
@RequestMapping(path ="/v1/restaurants/{restaurantId}/products/{productId}/photo")
@AllArgsConstructor
public class RestaurantProductPhotoController implements RestaurantProductPhotoControllerOpenApi {

    private final ProductPhotoCatalogService photoService;
    private final ProductService productService;
    private final PhotoStorageService photoStorageService;
    private final ProductPhotoModelAssembler photoAssembler;
    private final ProductPhotoDisassembler photoDisassembler;

    @CheckSecurity.Restaurants.CanConsult
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductPhotoModel> findPhoto(@PathVariable UUID restaurantId, @PathVariable UUID productId , ServletWebRequest request) {
        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
        String eTag = "0";
        OffsetDateTime lastUpdateDate = productService.findLastUpdateDateAndActivesByRestaurantId(restaurantId);
        if (lastUpdateDate != null) {
            eTag = String.valueOf(lastUpdateDate.toEpochSecond());
        }

        if (request.checkNotModified(eTag)) {
            return null;
        }
        ProductPhotoModel model = photoAssembler.toModel(photoService.findById(restaurantId, productId));
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(model);
    }

    @GetMapping
    public ResponseEntity<InputStreamResource> servePhoto(@PathVariable UUID restaurantId, @PathVariable UUID productId,
                                                          @RequestHeader(name = "accept") String acceptHeaders) throws HttpMediaTypeNotAcceptableException {
        try {
            ProductPhoto productPhoto = photoService.findById(restaurantId, productId);
            MediaType mediaType = MediaType.parseMediaType(productPhoto.getContentType());
            List<MediaType> acceptedMediaTypes = MediaType.parseMediaTypes(acceptHeaders);

            verifyMediaTypeCompatibility(mediaType, acceptedMediaTypes);

            InputStream inputStream = photoStorageService.retrieve(productPhoto.getFileName());

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
    public ProductPhotoModel updatePhoto(@PathVariable UUID restaurantId, @PathVariable UUID productId,
                                         @Valid ProductPhotoDTO productPhotoDTO) throws IOException {
        Product product = productService.findById(restaurantId, productId);
        MultipartFile file = productPhotoDTO.getFile();

        ProductPhoto photo = photoDisassembler.photoProductDTOToProductPhoto(productPhotoDTO);
        photo.setProduct(product);

        ProductPhoto savedPhoto = photoService.save(photo, file.getInputStream());
        return photoAssembler.toModel(savedPhoto);
    }

    @CheckSecurity.Restaurants.CanManageOperation
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> removePhoto(@PathVariable UUID restaurantId, @PathVariable UUID productId) {
        photoService.delete(restaurantId, productId);
        return ResponseEntity.noContent().build();
    }
}