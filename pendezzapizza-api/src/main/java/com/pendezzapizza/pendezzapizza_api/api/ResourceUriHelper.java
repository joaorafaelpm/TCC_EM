package com.pendezzapizza.pendezzapizza_api.api;

import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

// Anotação do Lombok que transforma a classe em final com as classes estáticas e várias implementações para classes 100% utilitárias
@UtilityClass
public class ResourceUriHelper {

    public static void addUriResponseHeader (Object resourceId) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(resourceId)
                .toUri();

        HttpServletResponse response = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getResponse();

        response.setHeader(HttpHeaders.LOCATION , uri.toString());
    }


}
