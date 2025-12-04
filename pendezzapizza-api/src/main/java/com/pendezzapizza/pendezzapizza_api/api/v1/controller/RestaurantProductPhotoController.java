package com.pendezzapizza.pendezzapizza_api.api.v1.controller;


import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.ProductPhotoAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler.ProductPhotoDisassembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.DTO.ProductPhotoDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.ProductPhotoModel;
import com.pendezzapizza.pendezzapizza_api.domain.exception.EntityNotFoundException;
import com.pendezzapizza.pendezzapizza_api.domain.model.Product;
import com.pendezzapizza.pendezzapizza_api.domain.model.ProductPhoto;
import com.pendezzapizza.pendezzapizza_api.domain.service.PhotoStorageService;
import com.pendezzapizza.pendezzapizza_api.domain.service.ProductPhotoCatalogService;
import com.pendezzapizza.pendezzapizza_api.domain.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/restaurants/{restaurantId}/products/{productId}/photo")
@AllArgsConstructor
public class RestaurantProductPhotoController {

    private ProductPhotoCatalogService productPhotoService;
    private ProductService productService;
    private PhotoStorageService photoStorageService;

    private ProductPhotoAssembler productPhotoAssembler;
    private ProductPhotoDisassembler productPhotoDisassembler;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductPhotoModel getPhoto(
            @PathVariable UUID restaurantId,
            @PathVariable UUID productId
    ) {
        return productPhotoAssembler.toModel(
                productPhotoService.findById(restaurantId, productId)
        );
    }

    @GetMapping
    public ResponseEntity<InputStreamResource> servePhoto(
            @PathVariable UUID restaurantId,
            @PathVariable UUID productId,
            @RequestHeader(name = "accept") String acceptHeaders
    ) throws HttpMediaTypeNotAcceptableException {
        try {
            ProductPhoto productPhoto = productPhotoService.findById(restaurantId, productId);

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

    /**
     * At first, this remains inside the controller because of an infrastructure limitation:
     * When this exception is thrown, we must ensure that NO JSON is returned.
     * Currently, the ExceptionHandler catches the exception and returns JSON,
     * causing a "Not Acceptable" — not because the media type is wrong,
     * but because we handled the error incorrectly.
     */
    private void verifyMediaTypeCompatibility(
            MediaType photoMediaType,
            List<MediaType> acceptedMediaTypes
    ) throws HttpMediaTypeNotAcceptableException {

        boolean compatible = acceptedMediaTypes.stream()
                .anyMatch(accepted -> accepted.isCompatibleWith(photoMediaType));

        if (!compatible) {
            throw new HttpMediaTypeNotAcceptableException(acceptedMediaTypes);
        }
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductPhotoModel uploadPhoto(
            @PathVariable UUID restaurantId,
            @PathVariable UUID productId,
            @Valid ProductPhotoDTO productPhotoDTO
    ) throws IOException {

        Product product = productService.findById(restaurantId, productId);
        MultipartFile file = productPhotoDTO.getFile();

        ProductPhoto productPhoto =
                productPhotoDisassembler.photoProductDTOToProductPhoto(productPhotoDTO);

        productPhoto.setProduct(product);

        ProductPhoto savedPhoto =
                productPhotoService.save(productPhoto, file.getInputStream());

        return productPhotoAssembler.toModel(savedPhoto);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePhoto(
            @PathVariable UUID restaurantId,
            @PathVariable UUID productId
    ) {
        productPhotoService.delete(restaurantId, productId);
    }
}
