package com.pendezzapizza.pendezzapizza_api.core.email;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Sandbox {

    @NotNull
    private String recipient ;

}
