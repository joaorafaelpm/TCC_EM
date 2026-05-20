package com.pendezzapizza.pendezzapizza_api.core.security;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public @interface CheckSecurity {

    @interface Restaurants {

        // Qualquer usuário autenticado pode listar restaurantes.
        @PreAuthorize("@pendezzaPizzaSecurity.canConsultRestaurants()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @interface CanConsult {
        }

        // Qualquer autenticado pode cadastrar — a promoção para "Dono de Restaurante"
        // acontece automaticamente no service após o cadastro bem-sucedido.
        @PreAuthorize("@pendezzaPizzaSecurity.canRegisterRestaurant()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @interface CanRegister {
        }

        // Somente Admin: criação/edição global de restaurantes (dados cadastrais, endereço etc).
        // Não confundir com CanEditLogic, que é o fluxo operacional do dono/chefe.
        @PreAuthorize("@pendezzaPizzaSecurity.canManageRestaurantRegistrations()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @interface CanManageRegistration {
        }

        // CRÍTICO: dupla verificação — precisa ter EDITAR_LOGICA_RESTAURANTES (grupo Chefe+)
        // E ser responsável pelo restaurante específico (ownership check).
        // O parâmetro #restaurantId deve ser declarado no método do controller com esse nome exato.
        @PreAuthorize("@pendezzaPizzaSecurity.canEditRestaurantLogic(#restaurantId)")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @interface CanEditLogic {
        }

        // Gerenciar operação: Admin pode em qualquer restaurante;
        // Dono somente no seu (ownership check interno).
        @PreAuthorize("@pendezzaPizzaSecurity.canManageRestaurantOperation()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @interface CanManageOperation {
        }
    }

    @interface Orders {

        // Consulta de pedido individual:
        // 1. @PreAuthorize verifica escopos básicos (SCOPE_READ + autenticado) — evita hit no banco
        //    para tokens inválidos antes mesmo de executar a query.
        // 2. @PostAuthorize verifica, após buscar o pedido, se o usuário é o cliente do pedido,
        //    o dono do restaurante, ou tem CONSULTAR_PEDIDOS — sem expor dados antes da checagem.
        @PreAuthorize("@pendezzaPizzaSecurity.canSearchOrders()")
        @PostAuthorize("@pendezzaPizzaSecurity.canSearchOrders(" +
                "returnObject.body.customer.id, returnObject.body.restaurant.id)")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @interface CanSearch {
        }

        // Listagem de pedidos com filtro: verifica clientId e restaurantId já no início
        // pois eles chegam como parâmetro da requisição (não precisa buscar antes).
        @PreAuthorize("@pendezzaPizzaSecurity.canListOrders(" +
                "#orderFilter.customerId, #orderFilter.restaurantId)")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @interface CanList {
        }

        // Gerenciar status do pedido: Admin/Analista (GERENCIAR_PEDIDOS)
        // ou dono do restaurante do pedido (ownership check via orderId).
        @PreAuthorize("@pendezzaPizzaSecurity.canManageOrders(#orderId)")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @interface CanManage {
        }

        // Criar pedido: qualquer usuário autenticado com SCOPE_WRITE.
        @PreAuthorize("@pendezzaPizzaSecurity.canCreateOrders()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @interface CanCreate {
        }
    }

    @interface PaymentMethods {

        @PreAuthorize("@pendezzaPizzaSecurity.canConsultPaymentMethods()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @interface CanConsult {
        }

        @PreAuthorize("@pendezzaPizzaSecurity.canEditPaymentMethods()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @interface CanEdit {
        }
    }

    @interface Cities {

        @PreAuthorize("@pendezzaPizzaSecurity.canConsultCities()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @interface CanConsult {
        }

        @PreAuthorize("@pendezzaPizzaSecurity.canEditCities()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @interface CanEdit {
        }
    }

    @interface States {

        @PreAuthorize("@pendezzaPizzaSecurity.canConsultStates()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @interface CanConsult {
        }

        @PreAuthorize("@pendezzaPizzaSecurity.canEditStates()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @interface CanEdit {
        }
    }

    @interface UsersGroupsPermissions {

        // Alterar somente a própria senha — não pode alterar a de outro usuário.
        @PreAuthorize("@pendezzaPizzaSecurity.canChangeOwnPasswordUsersGroupsPermissions(#userId)")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @interface CanChangeOwnPassword {
        }

        // Atualizar dados de usuário: Admin pode qualquer um; usuário pode a si mesmo.
        @PreAuthorize("@pendezzaPizzaSecurity.canUpdateUsersGroupsPermissions(#userId)")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @interface CanUpdateUser {
        }

        // Listagem global de usuários/grupos/permissões: somente Admin e quem tem a permissão explícita.
        @PreAuthorize("@pendezzaPizzaSecurity.canConsultUsersGroupsPermissions()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @interface CanConsult {
        }

        // Consulta de um usuário específico: Admin/Analista ou o próprio usuário consultando seus dados.
        @PreAuthorize("@pendezzaPizzaSecurity.canConsultOwnUsersGroupsPermissions(#userId)")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @interface CanConsultOwnUser {
        }

        // Editar grupos e permissões: somente Admin.
        @PreAuthorize("@pendezzaPizzaSecurity.canEditUsersGroupsPermissions()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @interface CanEdit {
        }
    }

    @interface Statistics {

        // Somente Analista de Restaurante, Chefe, Dono ou Admin (todos têm GERAR_RELATORIOS).
        @PreAuthorize("@pendezzaPizzaSecurity.canConsultStatistics()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @interface CanConsult {
        }
    }
}