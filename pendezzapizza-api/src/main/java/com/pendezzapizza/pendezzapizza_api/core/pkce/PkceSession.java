package com.pendezzapizza.pendezzapizza_api.core.pkce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PkceSession implements Serializable {
    private String codeVerifier;
}