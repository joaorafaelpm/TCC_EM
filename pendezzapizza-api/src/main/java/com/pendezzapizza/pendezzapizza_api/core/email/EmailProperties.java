package com.pendezzapizza.pendezzapizza_api.core.email;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Validated
@Getter
@Setter
@Component
@ConfigurationProperties("pendezza-pizza.email")
public class EmailProperties {


    private Sandbox sandbox = new Sandbox();

    @NotNull
    private String sender;

    private Implementation impl = Implementation.MOCK;

    public enum Implementation {
        SMTP, MOCK , SANDBOX
    }

}
