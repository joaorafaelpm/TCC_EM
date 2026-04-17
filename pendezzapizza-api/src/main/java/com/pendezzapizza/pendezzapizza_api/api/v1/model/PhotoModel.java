package com.pendezzapizza.pendezzapizza_api.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhotoModel {

    @Schema(example = "b8bbd21a-4dd3-4954-835c-3493af2ba6a0_arquivo1.jpeg")
    private String fileName;
    @Schema(example = "Prime Rib ao ponto")
    private String description;
    @Schema(example = "image/jpeg")
    private String contentType;
    @Schema(example = "202912")
    private Long size;
}