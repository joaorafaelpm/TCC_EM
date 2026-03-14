package com.pendezzapizza.pendezzapizza_api.api.v1.model.dto;

import com.pendezzapizza.pendezzapizza_api.core.validation.productphoto.FileSize;
import com.pendezzapizza.pendezzapizza_api.core.validation.productphoto.FileType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductPhotoDTO {

    @Schema(description = "Arquivo da foto do produto (máximo 500KB, apenas JPG e PNG)")
    @NotNull
    @FileSize(max = "500KB")
    @FileType(allowed = {org.springframework.http.MediaType.IMAGE_JPEG_VALUE, org.springframework.http.MediaType.IMAGE_PNG_VALUE})
    private MultipartFile file;

    @Schema(description = "Descrição da foto do produto")
    @NotBlank
    private String description;
}