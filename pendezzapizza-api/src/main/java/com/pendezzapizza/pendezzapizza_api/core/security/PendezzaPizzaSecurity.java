package com.pendezzapizza.pendezzapizza_api.core.security;

import com.pendezzapizza.pendezzapizza_api.domain.repository.OrderRepository;
import com.pendezzapizza.pendezzapizza_api.domain.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class PendezzaPizzaSecurity {

    private RestaurantRepository restaurantRepository;
    private OrderRepository orderRepository;

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public UUID getUserId() {
        Jwt jwt = (Jwt) getAuthentication().getPrincipal();
        return UUID.fromString(jwt.getClaim("user_id"));
    }

    public boolean managesRestaurant(UUID restaurantId) {
        if (restaurantId == null) {
            return false;
        }
        return restaurantRepository.existsResponsible(restaurantId, getUserId());
    }

    public boolean managesRestaurantOfOrder(UUID orderId) {
        if (orderId == null) {
            return false;
        }
        return orderRepository.isOrderManagedBy(orderId, getUserId());
    }

    public boolean isAuthenticatedUserEquals(UUID userId) {
        return getUserId() != null && userId != null
                && getUserId().equals(userId);
    }

    public boolean hasAuthority(String authorityName) {
        return getAuthentication().getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(authorityName));
    }

    public boolean hasAuthorityWrite() {
        return hasAuthority("SCOPE_WRITE");
    }

    public boolean hasAuthorityRead() {
        return hasAuthority("SCOPE_READ");
    }

    public boolean isAuthenticated() {
        return getAuthentication().isAuthenticated();
    }

    //    Restaurantes
    public boolean canConsultRestaurants() {
        return hasAuthorityRead() && isAuthenticated();
    }

    public boolean canManageRestaurantRegistrations() {
        return hasAuthorityWrite() && hasAuthority("EDITAR_RESTAURANTES");
    }

    public boolean canManageRestaurantOperation(UUID restaurantId) {
        return hasAuthorityWrite() &&
                (hasAuthority("EDITAR_RESTAURANTES") || managesRestaurant(restaurantId));
    }

    //    Pedidos
    public boolean canSearchOrders() {
        return hasAuthorityRead() && isAuthenticated();
    }

    public boolean canSearchOrders(UUID clientId, UUID restaurantId) {
        return hasAuthority("CONSULTAR_PEDIDOS") ||
                isAuthenticatedUserEquals(clientId) || managesRestaurant(restaurantId);
    }

    public boolean canListOrders(UUID clientId, UUID restaurantId) {
        return hasAuthorityRead() && canSearchOrders(clientId, restaurantId);
    }

    public boolean canManageOrders(UUID orderCode) {
        return hasAuthorityWrite() && (hasAuthority("GERENCIAR_PEDIDOS")
                || managesRestaurantOfOrder(orderCode));
    }

    public boolean canCreateOrders() {
        return hasAuthorityWrite() && isAuthenticated();
    }

    //    FormasPagamento
    public boolean canConsultPaymentMethods() {
        return hasAuthorityRead() && isAuthenticated();
    }

    public boolean canEditPaymentMethods() {
        return hasAuthorityWrite() && hasAuthority("EDITAR_FORMAS_PAGAMENTO");
    }

    //      Cidades
    public boolean canConsultCities() {
        return hasAuthorityRead() && isAuthenticated();
    }

    public boolean canEditCities() {
        return hasAuthorityWrite() && hasAuthority("EDITAR_CIDADES");
    }

    //      Estados
    public boolean canConsultStates() {
        return hasAuthorityRead() && isAuthenticated();
    }

    public boolean canEditStates() {
        return hasAuthorityWrite() && hasAuthority("EDITAR_ESTADOS");
    }

    //    UsuariosGruposPermissoes
    public boolean canChangeOwnPasswordUsersGroupsPermissions(UUID userId) {
        return hasAuthorityWrite() && isAuthenticatedUserEquals(userId);
    }

    public boolean canUpdateUsersGroupsPermissions(UUID userId) {
        return hasAuthorityWrite() &&
                (hasAuthority("EDITAR_USUARIOS_GRUPOS_PERMISSOES") || isAuthenticatedUserEquals(userId));
    }

    public boolean canConsultUsersGroupsPermissions() {
        return hasAuthorityRead() && hasAuthority("CONSULTAR_USUARIOS_GRUPOS_PERMISSOES");
    }

    public boolean canEditUsersGroupsPermissions() {
        return hasAuthorityWrite() && hasAuthority("EDITAR_USUARIOS_GRUPOS_PERMISSOES");
    }

    //    Estatisticas
    public boolean canConsultStatistics() {
        return hasAuthorityRead() && hasAuthority("GERAR_RELATORIOS");
    }

}