package com.pendezzapizza.pendezzapizza_api.core.security;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public @interface CheckSecurity {

    public @interface Restaurants {
        @PreAuthorize("@pendezzaPizzaSecurity.canConsultRestaurants()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanConsult {
        }

        @PreAuthorize("@pendezzaPizzaSecurity.canManageRestaurantRegistrations()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanManageRegistration {
        }

        @PreAuthorize("@pendezzaPizzaSecurity.canManageRestaurantOperation(#restaurantId)")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanManageOperation {
        }
    }

    public @interface Orders {
        //   Para a consulta de pedidos a gente verifica os escopos padrão (se pode ler e se está autenticado)
//   Depois disso, antes de serializar em um objeto json e retornar ao cliente, a gente verifica se ele tem autoridade de consultar pedidos, se ele é o usuário correspondente do pedido, ou se ele é dono do restaurante onde o pedido foi feito
        @PreAuthorize("@pendezzaPizzaSecurity.canSearchOrders()")
        @PostAuthorize("@pendezzaPizzaSecurity.canSearchOrders(" +
                "returnObject.client.id , returnObject.restaurant.id)")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanSearch {
        }

        @PreAuthorize("@pendezzaPizzaSecurity.canListOrders(" +
                "#orderFilter.clientId , #orderFilter.restaurantId )")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanList {
        }

        @PreAuthorize("@pendezzaPizzaSecurity.canManageOrders(#orderId)")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanManage {
        }

        @PreAuthorize("@pendezzaPizzaSecurity.canCreateOrders()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanCreate {
        }
    }

    public @interface PaymentMethods {
        @PreAuthorize("@pendezzaPizzaSecurity.canConsultPaymentMethods()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanConsult {
        }

        @PreAuthorize("@pendezzaPizzaSecurity.canEditPaymentMethods()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanEdit {
        }
    }

    public @interface Cities {
        @PreAuthorize("@pendezzaPizzaSecurity.canConsultCities()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanConsult {
        }

        @PreAuthorize("@pendezzaPizzaSecurity.canEditCities()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanEdit {
        }
    }

    public @interface States {
        @PreAuthorize("@pendezzaPizzaSecurity.canConsultStates()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanConsult {
        }

        @PreAuthorize("@pendezzaPizzaSecurity.canEditStates()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanEdit {
        }
    }

    public @interface UsersGroupsPermissions {
        @PreAuthorize("@pendezzaPizzaSecurity.canChangeOwnPasswordUsersGroupsPermissions(#userId)")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanChangeOwnPassword {
        }

        @PreAuthorize("@pendezzaPizzaSecurity.canUpdateUsersGroupsPermissions(#userId)")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanUpdateUser {
        }

        @PreAuthorize("@pendezzaPizzaSecurity.canConsultUsersGroupsPermissions()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanConsult {
        }

        @PreAuthorize("@pendezzaPizzaSecurity.canEditUsersGroupsPermissions()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanEdit {
        }
    }

    public @interface Statistics {
        @PreAuthorize("@pendezzaPizzaSecurity.canConsultStatistics()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanConsult {
        }
    }

}