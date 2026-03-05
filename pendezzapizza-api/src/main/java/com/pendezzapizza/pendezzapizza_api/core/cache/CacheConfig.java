package com.pendezzapizza.pendezzapizza_api.core.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager();

        // Definimos os nomes base de todas as entidades
        String[] entities = {
                "city", "state", "paymentMethod", "permission", "group", "user", "order",  "restaurant"
        };

        for (String entity : entities) {
            // regra específica para palavras que terminam com y
            String plural = entity.endsWith("y") ? entity.substring(0, entity.length() - 1) + "ies" : entity + "s";

            // regra específica do plural de paymentMethods
            if (entity.equals("paymentMethod")) plural = "paymentMethods";

            // Cache Individual (ex: "user")
            manager.registerCustomCache(entity, buildCache(200, 30));

            // Cache de Coleção (ex: "users")
            manager.registerCustomCache(plural, buildCache(100, 60));

            // Cache de Última Atualização Global (ex: "usersLastUpdate")
            manager.registerCustomCache(plural + "LastUpdate", buildCache(1, 30));

            // Cache de Última Atualização por ID (ex: "usersLastUpdateById")
            manager.registerCustomCache(plural + "LastUpdateById", buildCache(200, 30));
        }

//        Algumas configurações de cache mais personalizadas:

//        Restaurants:
        manager.registerCustomCache("restaurantsResponsibleUsers", buildCache(200, 30));
        manager.registerCustomCache("restaurantsPaymentMethods", buildCache(200, 30));

//        Products
        manager.registerCustomCache("product", buildCache(200, 30));
        manager.registerCustomCache("productsByRestaurant", buildCache(200, 30));
        manager.registerCustomCache("productsActivesByRestaurant", buildCache(200, 30));
        manager.registerCustomCache("productsLastUpdateDateActivesByRestaurantId", buildCache(1, 30));
        manager.registerCustomCache("productsLastUpdateDateByRestaurantId", buildCache(1, 30));

        return manager;
    }

    // Método auxiliar para fazer o build sem se repetir tanto
    private Cache<Object, Object> buildCache(int size, int minutes) {
        return Caffeine.newBuilder()
                .maximumSize(size)
                .expireAfterWrite(minutes, TimeUnit.MINUTES)
                .build();
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory factory,
            CacheInvalidationListener listener) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.addMessageListener(listener, new PatternTopic("cache:invalidate"));
        return container;
    }
}
