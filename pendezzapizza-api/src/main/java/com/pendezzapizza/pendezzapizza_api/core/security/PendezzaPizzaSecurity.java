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

    // =========================================================
    // Restaurantes
    // =========================================================

    public boolean canConsultRestaurants() {
        return hasAuthorityRead() && isAuthenticated();
    }

    // Qualquer usuário autenticado pode cadastrar um restaurante.
    // Ao cadastrar, o sistema atribui automaticamente o grupo "Dono de Restaurante",
    // tornando o fluxo de promoção transparente — sem intervenção manual de admin.
    public boolean canRegisterRestaurant() {
        return hasAuthorityWrite() && isAuthenticated();
    }

    // CRÍTICO: separamos a permissão administrativa (EDITAR_RESTAURANTES, usada pelo Admin
    // para criar/editar qualquer restaurante) da operação de dono/chefe sobre o próprio restaurante.
    // "canManageRestaurantRegistrations" cobre somente o fluxo administrativo global.
    public boolean canManageRestaurantRegistrations() {
        return hasAuthorityWrite() && hasAuthority("EDITAR_RESTAURANTES");
    }

    // CRÍTICO: ter a permissão EDITAR_LOGICA_RESTAURANTES não basta —
    // o usuário também precisa ser responsável pelo restaurante específico (ownership check).
    // Isso impede que um Chefe do Restaurante A edite o Restaurante B.
    public boolean canEditRestaurantLogic(UUID restaurantId) {
        return hasAuthorityWrite()
                && hasAuthority("EDITAR_LOGICA_RESTAURANTES")
                && managesRestaurant(restaurantId);
    }

    // Gerenciar operação: Admin (EDITAR_RESTAURANTES) pode tudo;
    // Dono só pode no próprio restaurante (ownership check via managesRestaurant).
    public boolean canManageRestaurantOperation() {
        return hasAuthorityWrite()
                && hasAuthority("GERENCIAR_RESTAURANTE");
    }

    // =========================================================
    // Pedidos
    // =========================================================

    // Versão sem parâmetros: usada exclusivamente como guarda inicial do @PreAuthorize,
    // antes do @PostAuthorize verificar clientId e restaurantId do objeto retornado.
    // Não remove a necessidade do @PostAuthorize — ela apenas evita o hit desnecessário
    // no banco quando o token nem tem SCOPE_READ.
    public boolean canSearchOrders() {
        return hasAuthorityRead() && isAuthenticated();
    }

    public boolean canSearchOrders(UUID clientId, UUID restaurantId) {
        return hasAuthority("CONSULTAR_PEDIDOS")
                || isAuthenticatedUserEquals(clientId)
                || managesRestaurant(restaurantId);
    }

    public boolean canListOrders(UUID clientId, UUID restaurantId) {
        return hasAuthorityRead() && canSearchOrders(clientId, restaurantId);
    }

    public boolean canManageOrders(UUID orderCode) {
        return hasAuthorityWrite()
                && (hasAuthority("GERENCIAR_PEDIDOS") || managesRestaurantOfOrder(orderCode));
    }

    public boolean canCreateOrders() {
        return hasAuthorityWrite() && isAuthenticated();
    }

    // =========================================================
    // Formas de Pagamento
    // =========================================================

    public boolean canConsultPaymentMethods() {
        return hasAuthorityRead() && isAuthenticated();
    }

    public boolean canEditPaymentMethods() {
        return hasAuthorityWrite() && hasAuthority("EDITAR_FORMAS_PAGAMENTO");
    }

    // =========================================================
    // Cidades
    // =========================================================

    public boolean canConsultCities() {
        return hasAuthorityRead() && isAuthenticated();
    }

    public boolean canEditCities() {
        return hasAuthorityWrite() && hasAuthority("EDITAR_CIDADES");
    }

    // =========================================================
    // Estados
    // =========================================================

    public boolean canConsultStates() {
        return hasAuthorityRead() && isAuthenticated();
    }

    public boolean canEditStates() {
        return hasAuthorityWrite() && hasAuthority("EDITAR_ESTADOS");
    }

    // =========================================================
    // Usuários, Grupos e Permissões
    // =========================================================

    public boolean canChangeOwnPasswordUsersGroupsPermissions(UUID userId) {
        return hasAuthorityWrite() && isAuthenticatedUserEquals(userId);
    }

    // Permite edição se for Admin (EDITAR_USUARIOS_GRUPOS_PERMISSOES)
    // ou se o usuário está editando a si mesmo (ex: atualizar nome/email).
    public boolean canUpdateUsersGroupsPermissions(UUID userId) {
        return hasAuthorityWrite()
                && (hasAuthority("EDITAR_USUARIOS_GRUPOS_PERMISSOES") || isAuthenticatedUserEquals(userId));
    }

    // Listagem global: somente quem tem a permissão explícita (Admin / Analista).
    public boolean canConsultUsersGroupsPermissions() {
        return hasAuthorityRead() && hasAuthority("CONSULTAR_USUARIOS_GRUPOS_PERMISSOES");
    }

    // Consulta pontual: Admin/Analista ou o próprio usuário consultando seus próprios dados.
    public boolean canConsultOwnUsersGroupsPermissions(UUID userId) {
        return hasAuthorityRead()
                && (hasAuthority("CONSULTAR_USUARIOS_GRUPOS_PERMISSOES") || isAuthenticatedUserEquals(userId));
    }

    public boolean canEditUsersGroupsPermissions() {
        return hasAuthorityWrite() && hasAuthority("EDITAR_USUARIOS_GRUPOS_PERMISSOES");
    }

    // =========================================================
    // Estatísticas / Relatórios
    // =========================================================

    public boolean canConsultStatistics() {
        return hasAuthorityRead() && hasAuthority("GERAR_RELATORIOS");
    }
}