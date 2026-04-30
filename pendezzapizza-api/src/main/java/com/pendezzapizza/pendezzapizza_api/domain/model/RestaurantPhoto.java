/**
 * @summary     Representa a foto de um restaurante, armazenando metadados do arquivo
 *              como nome, tipo de conteúdo e tamanho. Cada restaurante pode ter no máximo uma foto.
 * @difficulty  Medium
 * @depends-on  None
 */
package com.pendezzapizza.pendezzapizza_api.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.UUID;

/**
 * Entidade JPA que armazena os metadados da foto de um restaurante.
 * O arquivo em si é armazenado em um serviço externo de storage (ex: S3);
 * esta entidade guarda apenas as informações necessárias para localizá-lo e exibi-lo.
 *
 * Implementa {@code Serializable} para permitir que objetos desta classe
 * sejam convertidos em bytes — necessário para cache e transferência entre sistemas.
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Garante que apenas o campo "id" seja usado nas comparações de igualdade entre objetos RestaurantPhoto
@Entity
public class RestaurantPhoto implements Serializable {

    /**
     * Identificador único da foto, compartilhado com o restaurante associado.
     * O valor deste campo é o mesmo UUID do restaurante vinculado — não é gerado
     * de forma independente. Isso é garantido pela combinação de {@code @MapsId}
     * no campo {@code restaurant} com este {@code @GeneratedValue}.
     *
     * {@code @Id} — marca este campo como chave primária da tabela.
     * {@code @GeneratedValue} — o valor é derivado do restaurante associado via {@code @MapsId}, não gerado separadamente.
     * {@code @EqualsAndHashCode.Include} — inclui apenas este campo ao comparar dois objetos RestaurantPhoto, evitando comparações incorretas baseadas em outros atributos mutáveis.
     * {@code @JdbcTypeCode(SqlTypes.BINARY)} — instrui o Hibernate a tratar o UUID como bytes binários no JDBC, compatível com a definição BINARY(16) no banco.
     * {@code @Column(columnDefinition = "BINARY(16)")} — armazena o UUID como 16 bytes no banco em vez de uma string de 36 caracteres, economizando espaço e melhorando performance em índices.
     */
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    /**
     * Restaurante ao qual esta foto pertence.
     * {@code @OneToOne} com {@code @MapsId} significa que esta entidade compartilha
     * a chave primária com o restaurante — o {@code id} desta foto é o mesmo UUID do restaurante,
     * eliminando a necessidade de uma coluna FK separada e garantindo que cada restaurante
     * tenha no máximo uma foto.
     * Carregado com {@code LAZY} pois os dados do restaurante raramente são necessários
     * ao acessar apenas os metadados da foto.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Restaurant restaurant;

    /** Nome do arquivo no serviço de storage externo, usado para localizar e recuperar a imagem. */
    @Column(name = "archive_name")
    private String fileName;

    /** Texto alternativo ou legenda da foto, utilizado para acessibilidade e exibição na interface. */
    private String description;

    /**
     * Tipo MIME do arquivo (ex: "image/jpeg", "image/png").
     * Necessário para definir o header {@code Content-Type} ao servir a imagem via HTTP.
     */
    @Column(name = "content_type")
    private String contentType;

    /** Tamanho do arquivo em bytes, usado para validação de limites de upload e exibição de informações. */
    private Long size;

//    Eu comentei isso aq pq tava quebrando tudo
    // updateDate comentado intencionalmente — esta entidade é substituída por inteiro a cada novo upload,
    // tornando o rastreamento de atualização incremental sem utilidade prática no fluxo atual.

    /**
     * Retorna o ID do restaurante ao qual esta foto pertence.
     * Útil para autorização e isolamento de dados por restaurante sem precisar
     * navegar manualmente pela cadeia {@code photo → restaurant}.
     * Retorna {@code null} se o restaurante não estiver carregado ou seu ID for nulo.
     *
     * @return UUID do restaurante, ou {@code null} se o restaurante não estiver disponível
     */
    public UUID getRestaurantId() {
        if (getRestaurant() != null && getRestaurant().getId() != null) {
            return getRestaurant().getId();
        }
        return null;
    }
}