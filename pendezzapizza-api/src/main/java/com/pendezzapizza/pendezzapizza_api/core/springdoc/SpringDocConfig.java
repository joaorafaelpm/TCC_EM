package com.pendezzapizza.pendezzapizza_api.core.springdoc;

import com.pendezzapizza.pendezzapizza_api.api.exceptionhandler.ApiError;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
@SecurityScheme(name = "security_auth" ,
        type = SecuritySchemeType.OAUTH2 ,
        flows = @OAuthFlows(authorizationCode = @OAuthFlow(
                authorizationUrl = "${springdoc.oAuthFlow.authorizationUrl}",
                tokenUrl = "${springdoc.oAuthFlow.tokenUrl}",
                scopes = {
                        @OAuthScope(name = "READ" , description = "read scope"),
                        @OAuthScope(name = "WRITE" , description = "write scope")
                }
        ))
)
public class SpringDocConfig {

    private static final String BAD_REQUEST_RESPONSE = "BadRequestResponse";
    private static final String NOT_FOUND_REQUEST_RESPONSE = "NotFoundResponse";
    private static final String INTERNAL_SYSTEM_ERROR_RESPONSE = "InternalSystemErrorResponse";
    private static final String NOT_ACCEPTABLE_RESPONSE = "NotAcceptableResponse";

    @Bean
    public OpenAPI openApi () {
        return new OpenAPI().info(new Info()
                        .title("Pendezza Pizza API")
                        .version("v1")
                        .description("Rest API do Pendezza Pizza")
                )
//                    Isso adiciona a necessidade de precisar de autorização em cada um dos endpoints do swagger, porém eu vou manter a anotação por que eu estou  com preguiça de remover cada uma
                .addSecurityItem(new SecurityRequirement().addList("security_auth"))
//                    Eu  adiciono essa configuração  por que naturalmente o  swagger vai acessar a url  da  api, neste caso, como eu estou usando docker seria algafood-api:8080/v1/cidades, porém, pelo navegador não é possível acessar diretamente o docker, logo, a gente acessa o proxy reverso que é no localhost
                .servers(List.of(new Server().url("http://localhost").description("Proxy Reverso")))
                .externalDocs(new ExternalDocumentation()
                        .description("pendezzapizza")
                        .url("https://pendezzapizza.com")
                )
                .tags(Arrays.asList(
//                            Adicionando descrição sem  usar a anotação, desta forma ele reconhece a  tag pelo  nome e adiciona a descrição
                        new Tag().name("Cidades").description("Gerencia as cidades"),
                        new Tag().name("Grupos").description("Gerencia os grupos"),
                        new Tag().name("Formas De Pagamento").description("Gerencia as formas de pagamento"),
                        new Tag().name("Restaurantes").description("Gerencia os restaurantes"),
                        new Tag().name("Estados").description("Gerencia os estados"),
                        new Tag().name("Produtos").description("Gerencia os produtos"),
                        new Tag().name("Permissões").description("Gerencia os permissões"),
                        new Tag().name("Usuários").description("Gerencia os usuários"),
                        new Tag().name("Estatísticas").description("Estatísticas da AlgaFood")
                ))
//                Adicionando components,  esses components são o que  compõe  o schema basecamente, nesse caso tudo que nós  fazemos é implementar a nossa classe  de ApiError no projeto  e também generalizar as respostas dos status da aplicação
                .components(new Components()
                        .schemas(gerarSchemas())
                        .responses(gerarResponses())
                )
                ;

    }



    @Bean
    public OpenApiCustomizer openApiCustomizer  () {
        return openApi -> {
//                Customizando os estatus  da aplicação
            openApi.getPaths()
                .values()
                .forEach(pathItem -> pathItem.readOperationsMap()
                    .forEach((httpMethod, operation) -> {
                        ApiResponses responses = operation.getResponses();
                        switch (httpMethod) {
                            case  GET ->  {
                                responses.addApiResponse("500" , new ApiResponse().$ref(INTERNAL_SYSTEM_ERROR_RESPONSE));
                                responses.addApiResponse("406" , new ApiResponse().$ref(NOT_ACCEPTABLE_RESPONSE));
                            }
                            case  POST  -> {
                                responses.addApiResponse("400" , new ApiResponse().$ref(BAD_REQUEST_RESPONSE));
                                responses.addApiResponse("500" , new ApiResponse().$ref(INTERNAL_SYSTEM_ERROR_RESPONSE));
                            }
                            case  PUT  -> {
                                responses.addApiResponse("400" , new ApiResponse().$ref(BAD_REQUEST_RESPONSE));
                                responses.addApiResponse("500" , new ApiResponse().$ref(INTERNAL_SYSTEM_ERROR_RESPONSE));
                            }
                            case  DELETE -> responses.addApiResponse("500" , new ApiResponse().$ref(INTERNAL_SYSTEM_ERROR_RESPONSE));
                          }
                    })
                );
        };
    }

// Preciso adicionar isso por que a classe Schema não precisa de um objeto para representar, porém o compilador acha que precisa, então eu deixo isso explícito
    @SuppressWarnings("rawtypes")
    private Map<String  , Schema> gerarSchemas ()  {
        final Map<String  , Schema> schemaMap =  new HashMap<>();
        Map<String, Schema> apiErrorSchema = ModelConverters.getInstance().read(ApiError.class);
        Map<String,Schema> apiErrorObjectSchema = ModelConverters.getInstance().read(ApiError.Object.class);

        schemaMap.putAll(apiErrorSchema);
        schemaMap.putAll(apiErrorObjectSchema);

        return schemaMap ;
    }


    private Map<String, ApiResponse> gerarResponses() {
        final Map<String , ApiResponse> apiResponseMap = new HashMap<>();

        Content content = new Content()
                .addMediaType(APPLICATION_JSON_VALUE ,
                    new MediaType().schema(new Schema<ApiError>().$ref("ApiError")));

        apiResponseMap.put(BAD_REQUEST_RESPONSE , new ApiResponse()
                .description("Requisição invalida")
                .content(content));
        apiResponseMap.put(NOT_FOUND_REQUEST_RESPONSE , new ApiResponse()
                .description("Recurso não encontrado")
                .content(content));
        apiResponseMap.put(INTERNAL_SYSTEM_ERROR_RESPONSE , new ApiResponse()
                .description("Erro interno no servidor")
                .content(content));
        apiResponseMap.put(NOT_ACCEPTABLE_RESPONSE, new ApiResponse()
                .description("Recurso não possui representação que poderia ser aceita  pelo  consumidor")
                .content(content));

        return apiResponseMap;
    }


}
