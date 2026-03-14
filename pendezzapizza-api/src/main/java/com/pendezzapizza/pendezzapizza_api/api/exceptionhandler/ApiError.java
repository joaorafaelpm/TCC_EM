package com.pendezzapizza.pendezzapizza_api.api.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

//Classe genérica de Erros para passar ao ExceptionHandler dentro do padrão RFC 7807
//Eu incluo na minha menssagem de erro somente o que for passado dentro de exception handler
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
@Schema(name = "ApiError")
public class ApiError {
//    Os elementos dessa classe foram criados seguindo o padrão do RFC 7807

    @Schema(example = "400")
    private Integer status;
    @Schema(example = "https://pendezzapizza.com.br/dados-invalidos")
    private String type;
    @Schema(example = "Dados Invalidos")
    private String title ;
    @Schema(example = "Um ou mais campos estão invalidos, faça o preenchimento corretamente e tente novamente.")
    private String detail;

    @Schema(example = "Um ou mais campos estão invalidos, faça o preenchimento corretamente e tente novamente.")
    private String userMessage;
    @Schema(example = "2026-01-14T19:32:12.19823323Z")
    private OffsetDateTime timestamp;

    @Schema(description = "Lista de objetos ou campos que geraram um  erro")
    private List<Object> objects;


    @Getter
    @Builder
    @Schema(example = "Objeto Problema")
    public static class Object {
        @Schema(example = "preco")
        private String name;
        @Schema(example = "Preço é invalido")
        private String userMessage;
    }
}


