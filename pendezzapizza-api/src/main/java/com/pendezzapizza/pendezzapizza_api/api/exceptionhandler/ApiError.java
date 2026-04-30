/**
 * @summary     Representa o corpo padronizado de respostas de erro da API, seguindo o formato
 *              definido pela RFC 7807 (Problem Details for HTTP APIs).
 * @difficulty  Low
 * @depends-on  None
 */
package com.pendezzapizza.pendezzapizza_api.api.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Contrato de resposta de erro da API no padrão RFC 7807 (Problem Details for HTTP APIs).
 *
 * <p>Cada campo corresponde a uma propriedade definida pela especificação RFC 7807,
 * garantindo interoperabilidade com clientes que reconhecem esse padrão.</p>
 *
 * <p>Campos não preenchidos pelo {@code ExceptionHandler} são automaticamente
 * omitidos da resposta JSON graças a {@code @JsonInclude(NON_NULL)},
 * evitando ruído desnecessário para o consumidor da API.</p>
 */
// @JsonInclude(NON_NULL): garante que apenas os campos explicitamente preenchidos pelo
// ExceptionHandler apareçam no JSON — campos nulos são omitidos da resposta.
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
// @Schema: expõe esta classe ao Swagger UI com o nome "ApiError" na documentação gerada.
@Schema(name = "ApiError")
public class ApiError {

    /** Código HTTP do erro (ex.: 400, 404, 500). Espelha o status da resposta HTTP. */
    @Schema(example = "400")
    private Integer status;

    /**
     * URI que identifica o tipo do problema de forma única e estável.
     * Deve apontar para uma página com descrição legível por humanos,
     * conforme exigido pela RFC 7807.
     */
    @Schema(example = "https://pendezzapizza.com.br/dados-invalidos")
    private String type;

    /** Resumo curto e legível do tipo do problema (ex.: "Dados Inválidos"). */
    @Schema(example = "Dados Invalidos")
    private String title;

    /**
     * Descrição técnica detalhada do erro, voltada a desenvolvedores.
     * Pode conter contexto interno que não deve ser exposto diretamente ao usuário final.
     */
    @Schema(example = "Um ou mais campos estão invalidos, faça o preenchimento corretamente e tente novamente.")
    private String detail;

    /**
     * Mensagem amigável destinada a ser exibida diretamente ao usuário final na interface.
     * Diferente de {@code detail}, deve ser genérica o suficiente para não expor
     * informações sensíveis do sistema.
     */
    @Schema(example = "Um ou mais campos estão invalidos, faça o preenchimento corretamente e tente novamente.")
    private String userMessage;

    /**
     * Momento exato em que o erro ocorreu, com fuso horário (ISO 8601 com offset).
     * Usar {@code OffsetDateTime} em vez de {@code LocalDateTime} preserva o fuso
     * horário do servidor, facilitando rastreamento em ambientes distribuídos.
     */
    @Schema(example = "2026-01-14T19:32:12.19823323Z")
    private OffsetDateTime timestamp;

    /**
     * Lista opcional de campos ou sub-recursos que contribuíram para o erro.
     * Populada pelo ExceptionHandler em casos de validação com múltiplos problemas,
     * como erros de Bean Validation em vários campos simultaneamente.
     */
    @Schema(description = "Lista de objetos ou campos que geraram um erro")
    private List<Object> objects;


    /**
     * Representa um item individual dentro da lista de erros de {@code ApiError}.
     *
     * <p>Usado para detalhar qual campo ou objeto específico causou o problema,
     * permitindo que o cliente identifique e corrija cada ocorrência separadamente.</p>
     */
    @Getter
    @Builder
    @Schema(example = "Objeto Problema")
    public static class Object {

        /** Nome do campo ou objeto que originou o erro (ex.: "preco", "quantidade"). */
        @Schema(example = "preco")
        private String name;

        /** Mensagem amigável descrevendo o problema neste campo específico, para exibição ao usuário. */
        @Schema(example = "Preço é invalido")
        private String userMessage;
    }
}