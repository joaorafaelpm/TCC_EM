package com.pendezzapizza.pendezzapizza_api.api.exceptionhandler.enuns;

import lombok.Getter;

@Getter
public enum ProblemType {

    INCOMPREHENSIBLE_MESSAGE("/mensagem-incompreenssivel" , "Mensagem Incompreenssível."),
    INVALID_DATA("/dados-invalidos" , "Algum dado foi inserido de forma incorreta."),
    RESOURCE_NOT_FOUND("/recurso-nao-encontrado" , "Recurso não encontrado."),
    SYSTEM_ERROR("/erro-inesperado" , "Erro inesperado."),
    INVALID_PARAMS("/parametro-invalido" , "Parâmetro inválido na URL"),
    USED_ENTITY("/entidade-esta-em-uso" , "Entidade está sendo usada.") ,
    BUSINESS_EXCEPTION("/erro-de-negocio" , "Houve uma violação da regra de negócio."),
    AUTHORITY_EXCEPTION("/acesso-negado" , "Acesso negado.") ;

    private String path;
    private String title;

    ProblemType (String path , String title) {
        this.path = "https://pendezzapizza.com.br" + path ;
        this.title = title;
    }





}
