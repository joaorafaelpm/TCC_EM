package com.pendezzapizza.pendezzapizza_api.core.cache;

import lombok.AllArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@AllArgsConstructor
public class CacheInvalidationAspect {

    private final CacheInvalidatorUtil cacheInvalidatorUtil;

    // "Sempre que um método for anotado com @GroupsCacheEvict, execute isso após o sucesso"
    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.GroupsCacheEvict)")
    public void afterGroupsUpdate() {
        cacheInvalidatorUtil.publishCacheInvalidation("groups", "group", "groupsLastUpdate", "groupsLastUpdateById");
    }

    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.UsersCacheEvict)")
    public void afterUsersUpdate() {
        cacheInvalidatorUtil.publishCacheInvalidation("users", "user", "usersLastUpdate", "usersLastUpdateById");
    }
    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.PermissionsCacheEvict)")
    public void afterPermissionsUpdate() {
        cacheInvalidatorUtil.publishCacheInvalidation("permissions", "permission", "permissionsLastUpdate", "permissionsLastUpdateById");
    }
    @AfterReturning("@annotation(com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.StatesCacheEvict)")
    public void afterStatesUpdate() {
        cacheInvalidatorUtil.publishCacheInvalidation("states", "state" , "statesLastUpdate" , "statesLastUpdateById");

    }
}