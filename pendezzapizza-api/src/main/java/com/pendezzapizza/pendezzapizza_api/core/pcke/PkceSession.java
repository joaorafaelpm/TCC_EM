package com.pendezzapizza.pendezzapizza_api.core.pcke;

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