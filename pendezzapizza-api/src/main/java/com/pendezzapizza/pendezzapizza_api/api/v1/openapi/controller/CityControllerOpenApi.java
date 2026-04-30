/**
 * @summary     Interface que define o contrato da documentação OpenAPI para o controlador de cidades.
 *              Centraliza todas as anotações Swagger, mantendo a implementação do controlador limpa.
 * @difficulty  Low
 * @depends-on  None
 */
package com.pendezzapizza.pendezzapizza_api.api.v1.openapi.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.CityModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.CityDTO;
import com.pendezzapizza.pendezzapizza_api.domain.exception.CityNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.UUID;

/**
 * Contrato de documentação OpenAPI para os endpoints de gerenciamento de cidades.
 *
 * <p>Essa interface serve exclusivamente para separar as anotações do Swagger da lógica
 * do controlador. O controlador real implementa esta interface e herda toda a documentação
 * automaticamente, sem poluir o código de negócio com metadados de API.</p>
 *
 * <p>Isso serve para todas as intefaces da OpenApi, logo eu não vou comentar tudo!</p>
 */
// @Tag agrupa todos os endpoints desta interface sob o título "Cidades" na UI do Swagger
@Tag(name = "Cidades")
public interface CityControllerOpenApi {

    /**
     * Retorna uma página de cidades, com filtro opcional por nome.
     *
     * @param cityName nome parcial ou completo para filtrar cidades (opcional)
     * @param pageable parâmetros de paginação e ordenação (oculto na UI — gerado automaticamente)
     * @param request  contexto da requisição HTTP, usado para suporte a cache condicional (ETag/Last-Modified)
     * @return página de {@link CityModel} correspondente aos critérios informados
     */
    @Operation(summary = "Lista de cidades")
    ResponseEntity<Page<CityModel>> all(
            @Parameter(required = false , description = "Filtra uma cidade pelo nome.") String cityName,
            // hidden = true: Pageable é injetado pelo Spring, não deve aparecer como parâmetro manual no Swagger
            @Parameter(hidden = true) Pageable pageable,
            // hidden = true: ServletWebRequest é infraestrutura interna, não faz parte do contrato público da API
            @Parameter(hidden = true) ServletWebRequest request
    );

    /**
     * Busca uma cidade pelo seu identificador único.
     *
     * @param cityId identificador UUID da cidade
     * @param request contexto da requisição HTTP, usado para suporte a cache condicional (ETag/Last-Modified)
     * @return {@link CityModel} correspondente ao ID informado
     * @throws CityNotFoundException se nenhuma cidade for encontrada com o ID informado (HTTP 404)
     */
    @Operation(summary = "Busca uma Cidade por id", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404",
                    description = "Cidade não encontrada",
                    content = @Content(schema = @Schema(ref = "ApiError"))),
            @ApiResponse(responseCode = "400",
                    description = "Id da cidade inválido",
                    content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ResponseEntity<CityModel> findById(
            @Parameter(description = "Id de uma cidade", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID cityId,
            @Parameter(hidden = true) ServletWebRequest request);

    /**
     * Cadastra uma nova cidade vinculada a um estado existente.
     *
     * @param cityDTO dados da cidade a ser cadastrada
     * @return {@link CityModel} representando a cidade recém-criada
     */
    @Operation(summary = "Cadastra uma Cidade",
            description = "Cadastro de uma Cidade, necesita de um Estado e nome válido")
    CityModel add(
            @RequestBody(description = "Representação de uma nova cidade", required = true) CityDTO cityDTO);

    /**
     * Atualiza os dados de uma cidade existente.
     *
     * @param cityId  identificador UUID da cidade a ser atualizada
     * @param cityDTO novos dados da cidade
     * @return {@link CityModel} com os dados atualizados
     * @throws CityNotFoundException se nenhuma cidade for encontrada com o ID informado (HTTP 404)
     */
    @Operation(summary = "Atualiza uma Cidade por Id", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404",
                    description = "Cidade não encontrada",
                    content = @Content(schema = @Schema(ref = "ApiError"))),
            @ApiResponse(responseCode = "400",
                    description = "Id da cidade inválido",
                    content = @Content(schema = @Schema(ref = "ApiError")))
    })
    CityModel save(
            @Parameter(description = "Id de uma cidade", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID cityId,
            @RequestBody(description = "Representação de uma cidade com dados atualizados", required = true) CityDTO cityDTO);

    /**
     * Remove uma cidade pelo seu identificador único.
     *
     * @param cityId identificador UUID da cidade a ser removida
     * @return resposta sem corpo (HTTP 204) em caso de sucesso
     * @throws CityNotFoundException se nenhuma cidade for encontrada com o ID informado (HTTP 404)
     */
    @Operation(summary = "Remove uma Cidade por Id", responses = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "404",
                    description = "Cidade não encontrada",
                    content = @Content(schema = @Schema(ref = "ApiError"))),
            @ApiResponse(responseCode = "400",
                    description = "Id da cidade inválido",
                    content = @Content(schema = @Schema(ref = "ApiError")))
    })
    ResponseEntity<Void> remove(
            @Parameter(description = "Id de uma cidade", example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48", required = true) UUID cityId);
}