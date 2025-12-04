package com.pendezzapizza.pendezzapizza_api.api.v1.model.DTO;

import com.pendezzapizza.pendezzapizza_api.core.validation.productPhoto.FileSize;
import com.pendezzapizza.pendezzapizza_api.core.validation.productPhoto.FileType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductPhotoDTO {

    @NotNull
    @FileSize(max = "500KB")
    @FileType(allowed = {MediaType.IMAGE_JPEG_VALUE ,MediaType.IMAGE_PNG_VALUE })
    private MultipartFile file ;
    @NotBlank
    private String description ;

}
