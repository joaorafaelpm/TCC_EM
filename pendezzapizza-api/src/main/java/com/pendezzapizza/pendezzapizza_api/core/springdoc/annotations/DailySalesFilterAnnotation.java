package com.pendezzapizza.pendezzapizza_api.core.springdoc.annotations;


import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Parameter(
        in = ParameterIn.QUERY ,
        name = "restauranteId",
        description = "Id do restaurante para o filtro da pesquisa",
        example = "1",
        schema = @Schema(type = "integer")
)
@Parameter(
        in = ParameterIn.QUERY ,
        name = "dataCriacaoInicio",
        description = "Data/Hora inicial para o filtro da pesquisa",
        example = "2019-12-01T00:00:00Z",
        schema = @Schema(type = "string", format = "date-time")
)
@Parameter(
        in = ParameterIn.QUERY ,
        name = "dataCriacaoFim",
        description = "Data/Hora final para o filtro da pesquisa",
        example = "2019-12-01T23:59:59Z",
        schema = @Schema(type = "string", format = "date-time")
)
public @interface DailySalesFilterAnnotation {
}
