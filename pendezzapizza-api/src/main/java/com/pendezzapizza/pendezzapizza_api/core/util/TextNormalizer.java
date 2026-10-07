package com.pendezzapizza.pendezzapizza_api.core.util;

import java.text.Normalizer;

public final class TextNormalizer {

    private TextNormalizer() {
    }

    // "São Paulo" -> "saopaulo" | "Ribeirão Preto" -> "ribeiraopreto"
    public static String toNormalizedKey(String input) {
        if (input == null)
            return null;

        String semAcentos = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", ""); // remove os diacríticos (acentos, til, cedilha)

        return semAcentos.toLowerCase()
                .replaceAll("[^a-z0-9]", ""); // remove espaços, hífen, apóstrofo, etc.
    }
}
