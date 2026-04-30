/**
 * @summary     Enum que centraliza os tipos de problemas da API, associando cada erro
 *              a uma URI de documentação e a um título legível para humanos.
 * @difficulty  Low
 * @depends-on  ExceptionHandler
 */
package com.pendezzapizza.pendezzapizza_api.api.exceptionhandler.enuns;

import lombok.Getter;

@Getter
public enum ProblemType {

    // Corpo da requisição malformado ou ilegível (ex: JSON inválido)
    INCOMPREHENSIBLE_MESSAGE("/mensagem-incompreenssivel", "Mensagem Incompreenssível."),

    // Dados semanticamente inválidos (ex: campo com valor fora do domínio esperado)
    INVALID_DATA("/dados-invalidos", "Algum dado foi inserido de forma incorreta."),

    // Recurso buscado não existe no banco de dados
    RESOURCE_NOT_FOUND("/recurso-nao-encontrado", "Recurso não encontrado."),

    // Erros não tratados que escaparam para o handler genérico
    SYSTEM_ERROR("/erro-inesperado", "Erro inesperado."),

    // Parâmetro de URL com tipo ou formato incompatível com o esperado pelo endpoint
    INVALID_PARAMS("/parametro-invalido", "Parâmetro inválido na URL"),

    // Tentativa de remover ou modificar uma entidade que possui dependências ativas
    USED_ENTITY("/entidade-esta-em-uso", "Entidade está sendo usada."),

    // Violação de regra de negócio explícita lançada pela camada de serviço
    BUSINESS_EXCEPTION("/erro-de-negocio", "Houve uma violação da regra de negócio."),

    // Acesso a recurso protegido sem permissão suficiente
    AUTHORITY_EXCEPTION("/acesso-negado", "Acesso negado.");

    private String path;
    private String title;

    /**
     * Monta a URI completa do tipo de problema seguindo o padrão RFC 7807 (Problem Details),
     * que exige uma URI absoluta como identificador único do tipo de erro.
     *
     * @param path  segmento relativo que identifica o tipo de problema
     * @param title descrição curta e legível do problema
     */
    ProblemType(String path, String title) {
        // O domínio base é prefixado aqui para garantir que a URI gerada seja absoluta,
        // conforme exigido pelo padrão RFC 7807 usado no ExceptionHandler.
        this.path = "https://pendezzapizza.com.br" + path;
        this.title = title;
    }
}