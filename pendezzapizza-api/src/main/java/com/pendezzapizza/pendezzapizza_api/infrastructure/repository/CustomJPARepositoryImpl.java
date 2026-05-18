/**
 * @summary     Implementação customizada do repositório JPA base, adicionando busca fuzzy paginada
 *              por nome e recuperação de entidades com lançamento automático de exceções tipadas.
 * @difficulty  High
 * @depends-on  None
 */
package com.pendezzapizza.pendezzapizza_api.infrastructure.repository;

import com.pendezzapizza.pendezzapizza_api.domain.exception.*;
import com.pendezzapizza.pendezzapizza_api.domain.model.*;
import com.pendezzapizza.pendezzapizza_api.domain.repository.CustomJPARepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Repositório base genérico que estende {@code SimpleJpaRepository} para fornecer
 * comportamentos compartilhados entre todos os repositórios da aplicação.
 * Registrado como implementação padrão via {@code @EnableJpaRepositories(repositoryBaseClass = ...)}
 * — qualquer repositório que extenda {@code CustomJPARepository} receberá estes métodos automaticamente.
 *
 * @param <T>  tipo da entidade gerenciada
 * @param <ID> tipo do identificador da entidade
 */
public class CustomJPARepositoryImpl<T, ID>
        extends SimpleJpaRepository<T, ID>
        implements CustomJPARepository<T, ID> {

    private EntityManager manager;

    /**
     * Construtor exigido pelo mecanismo de repositório base do Spring Data JPA.
     * O {@code EntityManager} é armazenado localmente pois {@code SimpleJpaRepository}
     * não o expõe para subclasses.
     */
    public CustomJPARepositoryImpl(JpaEntityInformation<T, ?> entityInformation,
                                   EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.manager = entityManager;
    }

    /**
     * Busca entidades pelo campo {@code name} usando correspondência fuzzy,
     * retornando os resultados paginados e ordenados por relevância.
     * <p>
     * A estratégia é híbrida: um filtro LIKE pela primeira letra reduz os candidatos no banco,
     * e o refinamento fuzzy final ocorre em memória para suportar buscas aproximadas
     * (ex: "piza" encontra "pizza"). Resultados com score -1 (sem correspondência) são descartados.
     *
     * @param name     termo de busca; se vazio ou nulo, retorna todos os registros paginados
     * @param pageable configuração de paginação e ordenação
     * @return página de entidades ordenadas por proximidade fuzzy com o termo buscado
     */
    @Override
    public Page<T> findByName(String name, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();

        CriteriaQuery<T> criteria = builder.createQuery(getDomainClass());
        Root<T> root = criteria.from(getDomainClass());

        var predicates = new ArrayList<Predicate>();
        if (StringUtils.hasLength(name)) {
            // Pré-filtra no banco usando apenas a primeira letra para reduzir o volume
            // de registros trazidos para memória antes do scoring fuzzy completo.
            predicates.add(builder.like(
                    builder.lower(root.get("name")),
                    "%" + name.toLowerCase().charAt(0) + "%"
            ));
        }
        criteria.where(predicates.toArray(new Predicate[0]));

        // Traz os candidatos pré-filtrados sem paginação; a paginação é aplicada
        // após o scoring para garantir que a ordenação por relevância seja global.
        List<T> candidates = manager.createQuery(criteria).getResultList();

        String search = StringUtils.hasLength(name) ? name.toLowerCase() : "";

        List<T> sorted = candidates.stream()
                .map(entity -> {
                    String entityName = getNameFromEntity(entity);
                    int score = fuzzyScore(entityName.toLowerCase(), search);
                    return Map.entry(entity, score);
                })
                .filter(e -> e.getValue() >= 0)
                .sorted(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .toList();

        // Paginação manual necessária porque a ordenação por score fuzzy
        // não pode ser delegada ao banco — ocorre inteiramente em memória.
        int total = sorted.size();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), total);
        List<T> pageContent = (start >= total) ? List.of() : sorted.subList(start, end);

        return new PageImpl<>(pageContent, pageable, total);
    }

    /**
     * Calcula a relevância fuzzy entre um texto e um termo de busca.
     * O score é a posição do último caractere encontrado — quanto menor, mais compacto
     * e relevante é o match. Retorna -1 se nem todos os caracteres do termo forem
     * encontrados em ordem no texto.
     *
     * @param text   texto da entidade onde a busca será feita (deve estar em minúsculas)
     * @param search termo buscado (deve estar em minúsculas)
     * @return posição do último caractere matched, ou -1 se não houver correspondência completa
     */
    private int fuzzyScore(String text, String search) {
        if (!StringUtils.hasLength(search)) return 0;

        int searchIndex = 0;
        int lastMatchPos = 0;

        for (int i = 0; i < text.length() && searchIndex < search.length(); i++) {
            if (text.charAt(i) == search.charAt(searchIndex)) {
                lastMatchPos = i;
                searchIndex++;
            }
        }

        if (searchIndex < search.length()) return -1;

        return lastMatchPos;
    }

    /**
     * Extrai o valor do campo {@code name} da entidade via reflexão.
     * Utiliza reflexão pois a classe é genérica e não há contrato de interface
     * que garanta a existência do campo — retorna string vazia se o campo não existir
     * ou for nulo, fazendo com que a entidade receba score mínimo na busca fuzzy.
     *
     * @param entity entidade da qual o nome será extraído
     * @return valor do campo {@code name}, ou string vazia se ausente ou nulo
     */
    private String getNameFromEntity(T entity) {
        try {
            var field = entity.getClass().getDeclaredField("name");
            field.setAccessible(true);
            Object value = field.get(entity);
            return value != null ? value.toString() : "";
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Busca uma entidade pelo seu ID e lança uma exceção tipada e específica
     * caso ela não seja encontrada.
     * O tipo da exceção é determinado em tempo de execução com base na classe
     * da entidade gerenciada, garantindo mensagens de erro contextualizadas.
     *
     * @param id identificador único da entidade
     * @return entidade encontrada
     * @throws EntityNotFoundException (ou subclasse específica) se nenhuma entidade
     *                                 com o ID informado existir no banco
     */
    @Override
    public T findByIdOrThrowException(UUID id) {
        try {
            String jpql = "FROM " + getDomainClass().getSimpleName() + " e WHERE e.id = :id";

            return manager.createQuery(jpql, getDomainClass())
                    .setParameter("id", id)
                    .getSingleResult();

        } catch (NoResultException e) {
            throw createSpecificNotFoundException(id);
        }
    }

    /**
     * Busca uma entidade filtrando simultaneamente pelo seu próprio ID e pelo ID
     * de uma entidade estrangeira associada, lançando exceção tipada se não encontrada.
     * Usado em contextos onde a entidade só faz sentido dentro do escopo de outra
     * (ex: produto pertencente a um restaurante específico).
     *
     * @param foreignEntity instância da entidade estrangeira — usada apenas para inferir
     *                      o nome do campo de relacionamento via {@code getSimpleName()}
     * @param foreignId     ID da entidade estrangeira (pai/dono)
     * @param entityId      ID da entidade a ser buscada
     * @return entidade encontrada dentro do escopo da entidade estrangeira
     * @throws EntityNotFoundException (ou subclasse específica) se a combinação
     *                                 entityId + foreignId não existir no banco
     */
    @Override
    public T findByIdOrThrowException(
            Object foreignEntity,
            UUID foreignId,
            UUID entityId
    ) {
        try {
            // O nome do campo de relacionamento no JPQL é derivado do nome simples da
            // classe estrangeira em minúsculas (ex: Restaurant → restaurant.id),
            // exigindo que o campo na entidade siga esta convenção de nomenclatura.
            String jpql =
                    "FROM " + getDomainClass().getSimpleName()
                            + " e WHERE e." + foreignEntity.getClass().getSimpleName().toLowerCase() + ".id = :foreignId"
                            + " AND e.id = :entityId";

            return manager.createQuery(jpql, getDomainClass())
                    .setParameter("foreignId", foreignId)
                    .setParameter("entityId", entityId)
                    .getSingleResult();

        } catch (NoResultException e) {
            throw createSpecificNotFoundException(foreignEntity, entityId, foreignId);
        }
    }

    /**
     * Mapeia a classe da entidade gerenciada para sua exceção de "não encontrado" correspondente.
     * Centraliza o mapeamento para evitar lógica de desvio espalhada pelos serviços.
     * Inclui um fallback genérico para entidades ainda sem exceção dedicada.
     *
     * @param id identificador que não produziu resultado na busca
     * @return exceção tipada correspondente à entidade, ou {@code EntityNotFoundException} genérica
     */
    private EntityNotFoundException createSpecificNotFoundException(UUID id) {
        Class<?> model = getDomainClass();
        if (model.equals(City.class)) {
            return new CityNotFoundException(id);
        }
        if (model.equals(State.class)) {
            return new StateNotFoundException(id);
        }
        if (model.equals(Group.class)) {
            return new GroupNotFoundException(id);
        }
        if (model.equals(Permission.class)) {
            return new PermissionNotFoundException(id);
        }
        if (model.equals(OrderItem.class)) {
            return new OrderItemNotFoundException(id);
        }
        if (model.equals(Order.class)) {
            return new OrderNotFoundException(id);
        }
        if (model.equals(PaymentMethod.class)) {
            return new PaymentMethodNotFoundException(id);
        }
        if (model.equals(Restaurant.class)) {
            return new RestaurantNotFoundException(id);
        }

        // Fallback para entidades novas que ainda não possuem exceção dedicada.
        // Ao adicionar uma nova entidade ao domínio, registre o mapeamento acima
        // para manter mensagens de erro consistentes e rastreáveis.
        return new EntityNotFoundException(
                model.getSimpleName() + " com ID " + id + " não foi encontrado."
        );
    }

    /**
     * Variante do mapeamento de exceções para entidades cujo contexto de "não encontrado"
     * depende também de uma entidade estrangeira (ex: produto não encontrado em um restaurante).
     * Inclui um fallback genérico para entidades ainda sem exceção dedicada neste escopo.
     *
     * @param o         instância da entidade estrangeira, usada para compor a mensagem de fallback
     * @param entityId  ID da entidade que não foi encontrada
     * @param foreignId ID da entidade estrangeira usada como escopo da busca
     * @return exceção tipada correspondente, ou {@code EntityNotFoundException} genérica
     */
    private EntityNotFoundException createSpecificNotFoundException(Object o, UUID entityId, UUID foreignId) {
        Class<?> model = getDomainClass();

        if (model.equals(Product.class)) {
            return new ProductNotFoundException(entityId, foreignId);
        }
        if (model.equals(ProductPhoto.class)) {
            return new ProductPhotoNotFoundException(entityId, foreignId);
        }

        // Fallback para entidades novas que ainda não possuem exceção dedicada neste escopo.
        // Ao adicionar uma entidade com relacionamento estrangeiro, registre o mapeamento acima.
        return new EntityNotFoundException(
                model.getSimpleName() + " com ID " + entityId +
                        " não foi encontrado no " + o.getClass().getSimpleName() +
                        " com o id de " + foreignId
        );
    }
}