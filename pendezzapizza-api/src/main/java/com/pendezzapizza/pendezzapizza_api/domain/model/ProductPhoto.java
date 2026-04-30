/**
 * @summary     Representa a foto de um produto do cardápio, armazenando metadados do arquivo
 *              como nome, tipo de conteúdo e tamanho. Cada produto pode ter no máximo uma foto.
 * @difficulty  Medium
 * @depends-on  None
 */
package com.pendezzapizza.pendezzapizza_api.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Entidade JPA que armazena os metadados da foto de um produto.
 * O arquivo em si é armazenado em um serviço externo de storage (ex: S3);
 * esta entidade guarda apenas as informações necessárias para localizá-lo e exibi-lo.
 *
 * Implementa {@code Serializable} para permitir que objetos desta classe
 * sejam convertidos em bytes — necessário para cache e transferência entre sistemas.
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Garante que apenas o campo "id" seja usado nas comparações de igualdade entre objetos ProductPhoto
@Entity
public class ProductPhoto implements Serializable {

    /**
     * Identificador único da foto, compartilhado com o produto associado.
     * O valor deste campo é o mesmo UUID do produto vinculado — não é gerado
     * de forma independente. Isso é garantido pela combinação de {@code @MapsId}
     * no campo {@code product} com este {@code @GeneratedValue}.
     *
     * {@code @Id} — marca este campo como chave primária da tabela.
     * {@code @GeneratedValue} — o valor é derivado do produto associado via {@code @MapsId}, não gerado separadamente.
     * {@code @EqualsAndHashCode.Include} — inclui apenas este campo ao comparar dois objetos ProductPhoto, evitando comparações incorretas baseadas em outros atributos mutáveis.
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
     * Produto ao qual esta foto pertence.
     * {@code @OneToOne} com {@code @MapsId} significa que esta entidade compartilha
     * a chave primária com o produto — o {@code id} desta foto é o mesmo UUID do produto,
     * eliminando a necessidade de uma coluna FK separada e garantindo que cada produto
     * tenha no máximo uma foto.
     * Carregado com {@code LAZY} pois os dados do produto raramente são necessários
     * ao acessar apenas os metadados da foto.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Product product;

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

    /** Preenchida automaticamente pelo Hibernate sempre que o registro for atualizado no banco. */
    @UpdateTimestamp
    private OffsetDateTime updateDate;

    /**
     * Retorna o ID do restaurante ao qual o produto desta foto pertence.
     * Útil para autorização e isolamento de dados por restaurante sem precisar
     * navegar manualmente pela cadeia {@code photo → product → restaurant}.
     * Retorna {@code null} se o produto ou o restaurante não estiverem carregados.
     *
     * @return UUID do restaurante, ou {@code null} se o produto ou restaurante não estiver disponível
     */
    public UUID getRestaurantId() {
        if (getProduct() != null && getProduct().getRestaurant() != null) {
            return getProduct().getRestaurant().getId();
        }
        return null;
    }
}