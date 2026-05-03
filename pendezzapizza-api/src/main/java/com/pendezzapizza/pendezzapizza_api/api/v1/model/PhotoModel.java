/**
 * @summary     Representa o modelo de resposta dos metadados de uma foto de produto retornado pela API.
 *              Não contém a imagem em si — apenas informações descritivas e de referência ao arquivo.
 * @difficulty  Low
 * @depends-on  None
 */
package com.pendezzapizza.pendezzapizza_api.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhotoModel {

    // Nome do arquivo gerado no storage, incluindo prefixo UUID para garantir unicidade entre uploads
    @Schema(example = "b8bbd21a-4dd3-4954-835c-3493af2ba6a0_arquivo1.jpeg")
    private String fileName;

    @Schema(example = "Prime Rib ao ponto")
    private String description;

    // Tipo MIME do arquivo — utilizado pelo cliente para renderização e validação do formato
    @Schema(example = "image/jpeg")
    private String contentType;

    // Tamanho em bytes — útil para exibição informativa e validação de limites no cliente
    @Schema(example = "202912")
    private Long size;
}