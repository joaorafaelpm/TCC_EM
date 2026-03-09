package com.pendezzapizza.pendezzapizza_api.core.security;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Setter
@Getter
@Validated
@Component
@ConfigurationProperties("pendezzapizza.security.config")
public class SecurityController {

    @NotBlank
    public String url;

    @NotBlank
    public String urlIssuer;

}
