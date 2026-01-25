package com.pendezzapizza.pendezzapizza_api.core.security.authorizationserver;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.util.List;

public class JdbcOAuth2AuthorizationQueryService implements OAuth2AuthorizationQueryService {

    private final JdbcOperations jdbcOperations;
    private final RowMapper<RegisteredClient> registeredClientRowMapper;
    private final RowMapper<OAuth2Authorization> oAuth2AuthorizationRowMapper;

    private final String LIST_AUTHORIZED_CLIENTS = "select rc.* from oauth2_authorization_consent c " +
            "inner join oauth2_registered_client rc on rc.id = c.registered_client_id " +
            "where c.principal_name = ? ";


    private final String DELETE_AUTHORIZATIONS = "delete from oauth2_authorization " +
            "where principal_name = ? and registered_client_id = ?";



    public JdbcOAuth2AuthorizationQueryService(JdbcOperations jdbcOperations, RegisteredClientRepository repository) {
        this.jdbcOperations = jdbcOperations;
        this.registeredClientRowMapper = new JdbcRegisteredClientRepository.RegisteredClientRowMapper();
        this.oAuth2AuthorizationRowMapper = new JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper(repository);
    }

    @Override
    public List<RegisteredClient> listClientsWithConsent(String principalName) {
        return this.jdbcOperations.query(LIST_AUTHORIZED_CLIENTS, registeredClientRowMapper, principalName);
    }

    @Override
    public void deleteAuthorizations(String principalName, String clientId) {
        this.jdbcOperations.update(DELETE_AUTHORIZATIONS, principalName, clientId);
    }

}