/**
 * @summary Serviço responsável por gerenciar as operações de domínio relacionadas a cidades,
 *          incluindo consultas paginadas, buscas por nome/estado, e operações de escrita com invalidação de cache.
 * @difficulty Medium
 * @depends-on CitiesSaveCacheEvict, CitiesActionCacheEvict — anotações customizadas que controlam
 *             a invalidação do cache de cidades após operações de escrita.
 */
package com.pendezzapizza.pendezzapizza_api.domain.service;

import com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.action.CitiesActionCacheEvict;
import com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.save.CitiesSaveCacheEvict;
import com.pendezzapizza.pendezzapizza_api.domain.exception.CityNotFoundException;
import com.pendezzapizza.pendezzapizza_api.domain.model.City;
import com.pendezzapizza.pendezzapizza_api.domain.model.State;
import com.pendezzapizza.pendezzapizza_api.domain.repository.CityRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

// @Transactional(readOnly = true) aplicado na classe inteira: todas as consultas usam
// transações somente-leitura por padrão, o que permite otimizações no banco de dados
// (ex: sem geração de snapshots sujos). Os métodos de escrita sobrescrevem esse comportamento
// com @Transactional sem flags, abrindo uma transação de leitura e escrita.
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CityService {

    private final CityRepository cityRepository;
    private final StateService stateService;

    /**
     * Retorna uma página de todas as cidades cadastradas.
     * A chave de cache combina número e tamanho da página para evitar colisões entre
     * requisições com paginações diferentes.
     *
     * @param pageable parâmetros de paginação (número da página, tamanho, ordenação)
     * @return página de cidades
     */
    @Cacheable(value = "cities", key = "{#pageable.pageNumber, #pageable.pageSize}")
    public Page<City> findAll(Pageable pageable) {
        return cityRepository.findAll(pageable);
    }

    /**
     * Retorna uma página de cidades cujo nome corresponde ao filtro informado.
     * A chave de cache inclui o nome buscado para isolar resultados de buscas distintas.
     *
     * @param cityName nome (ou parte do nome) da cidade a ser filtrado
     * @param pageable parâmetros de paginação
     * @return página de cidades que correspondem ao filtro
     */
    @Cacheable(value = "citiesName", key = "{#pageable.pageNumber, #pageable.pageSize, #cityName}")
    public Page<City> findAllByName(String cityName, Pageable pageable) {
        return cityRepository.findByName(cityName, pageable);
    }

    /**
     * Busca uma cidade pelo seu identificador único.
     * Lança exceção automaticamente via repositório caso o ID não exista,
     * evitando a necessidade de verificação manual de Optional nesta camada.
     *
     * @param cityId identificador UUID da cidade
     * @return cidade encontrada
     * @throws com.pendezzapizza.pendezzapizza_api.domain.exception.CityNotFoundException se não encontrada
     */
    @Cacheable(value = "city", key = "#cityId")
    public City findById(UUID cityId) {
        return cityRepository.findByIdOrThrowException(cityId);
    }

    /**
     * Busca uma cidade pelo nome exato e pelo nome do estado ao qual pertence.
     * Útil para identificar cidades de forma não ambígua, já que o mesmo nome
     * pode existir em estados diferentes.
     *
     * @param cityName  nome exato da cidade
     * @param stateName nome exato do estado
     * @return cidade correspondente à combinação informada
     * @throws CityNotFoundException se nenhuma cidade for encontrada com essa combinação
     */
    @Cacheable(value = "cityAndStateName", key = "{#cityName, #stateName}")
    public City findCityByNameAndStateName(String cityName, String stateName) {
        return cityRepository.findCityByNameAndStateName(cityName, stateName).orElseThrow(() ->
                new CityNotFoundException(cityName, stateName)
        );
    }

    /**
     * Retorna a data e hora da última atualização registrada entre todas as cidades.
     * Usado tipicamente para validação de cache HTTP (ex: cabeçalho Last-Modified).
     *
     * @return data/hora da atualização mais recente no conjunto de cidades
     */
    @Cacheable("citiesLastUpdate")
    public OffsetDateTime getLastUpdateDate() {
        return cityRepository.getLastUpdateDate();
    }

    /**
     * Retorna a data e hora da última atualização de uma cidade específica.
     *
     * @param cityId identificador UUID da cidade
     * @return data/hora da última modificação da cidade informada
     */
    @Cacheable(value = "citiesLastUpdateById", key = "#cityId")
    public OffsetDateTime getLastUpdateDateById(UUID cityId) {
        return cityRepository.getLastUpdateDateById(cityId);
    }

    /**
     * Retorna a data e hora da última atualização de cidades com o nome informado.
     *
     * @param cityName nome da cidade
     * @return data/hora da última modificação encontrada para esse nome
     */
    @Cacheable(value = "citiesLastUpdateByName", key = "#cityName")
    public OffsetDateTime getLastUpdateDateByName(String cityName) {
        return cityRepository.getLastUpdateDateByName(cityName);
    }

    /**
     * Persiste uma nova cidade, resolvendo e vinculando o estado correspondente antes de salvar.
     * Invalida as entradas de cache relacionadas ao objeto retornado (cidade salva) após a operação.
     *
     * @param city objeto cidade com o estado já preenchido (apenas o ID do estado é necessário)
     * @return cidade persistida com todos os dados atualizados
     */
    // @CitiesSaveCacheEvict: anotação customizada que invalida as entradas de cache de cidades
    // com base no objeto retornado pelo método (a cidade salva).
    @CitiesSaveCacheEvict
    @Transactional
    public City save(City city) {
        UUID stateId = city.getState().getId();
        // Garante que o estado vinculado à cidade é uma entidade gerenciada pelo JPA,
        // evitando erros de entidade desanexada ao persistir.
        State state = stateService.findById(stateId);
        city.setState(state);
        return cityRepository.save(city);
    }

    /**
     * Atualiza os dados de uma cidade existente identificada pelo ID, aplicando o novo estado informado.
     * Invalida as entradas de cache relacionadas ao objeto retornado (cidade atualizada) após a operação.
     *
     * @param cityId      identificador UUID da cidade a ser atualizada
     * @param updatedCity objeto com os novos dados da cidade (apenas o ID do estado é necessário)
     * @return cidade atualizada e persistida
     * @throws com.pendezzapizza.pendezzapizza_api.domain.exception.CityNotFoundException se o ID não existir
     */
    // @CitiesSaveCacheEvict: anotação customizada que invalida as entradas de cache de cidades
    // com base no objeto retornado pelo método (a cidade atualizada).
    @CitiesSaveCacheEvict
    @Transactional
    public City save(UUID cityId, City updatedCity) {
        City existingCity = findById(cityId);
        UUID stateId = updatedCity.getState().getId();
        // Mesmo motivo do método save(City): assegura que o estado é uma entidade gerenciada.
        State state = stateService.findById(stateId);
        existingCity.setState(state);
        return cityRepository.save(existingCity);
    }

    /**
     * Remove uma cidade pelo ID, garantindo que a exclusão seja imediatamente sincronizada
     * com o banco de dados na mesma transação.
     * Invalida as entradas de cache relacionadas aos parâmetros do método após a operação.
     *
     * @param cityId identificador UUID da cidade a ser removida
     * @throws com.pendezzapizza.pendezzapizza_api.domain.exception.CityNotFoundException se o ID não existir
     */
    // @CitiesActionCacheEvict: anotação customizada que invalida as entradas de cache de cidades
    // com base nos parâmetros recebidos pelo método (o ID da cidade removida).
    @CitiesActionCacheEvict
    @Transactional
    public void delete(UUID cityId) {
        cityRepository.delete(findById(cityId));
        // flush() força a execução imediata do DELETE no banco dentro da transação atual,
        // garantindo que possíveis violações de integridade sejam capturadas antes do commit.
        cityRepository.flush();
    }
}