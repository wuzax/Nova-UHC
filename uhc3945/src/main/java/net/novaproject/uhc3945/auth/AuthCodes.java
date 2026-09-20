package net.novaproject.uhc3945.auth;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public final class AuthCodes {

    static final List<String> SIGNALS = List.of(
            "HIRONDELLE", "BRUME", "CHENE", "LANTERNE", "RUISSEAU", "SILENCE",
            "ETOILE", "CAILLOU", "FENETRE", "PONT", "GRENIER", "HORLOGE");

    static final List<String> SECRETS = List.of(
            "LISIERE", "VERGER", "SENTIER", "CLOCHE", "MARRONNIER", "CITERNE",
            "MOULIN", "VIGIE", "ATLAS", "CORDE", "SEUIL", "FABLE");

    private AuthCodes() {
    }

    public static String normalize(String raw) {
        if (raw == null) {
            return "";
        }
        String stripped = Normalizer.normalize(raw.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "")
                .replace('-', ' ')
                .replace('_', ' ')
                .replaceAll("[^A-Za-z0-9 ]", "")
                .replaceAll("\\s+", " ")
                .toUpperCase(Locale.ROOT)
                .trim();
        return stripped;
    }

    public static String[] parts(String normalized) {
        if (normalized == null || normalized.isEmpty()) {
            return new String[0];
        }
        return normalized.split(" ");
    }

    public static List<String[]> uniquePairs(int count, Random random) {
        if (count <= 0) {
            return List.of();
        }
        List<String> signals = new ArrayList<>(SIGNALS);
        List<String> secrets = new ArrayList<>(SECRETS);
        Collections.shuffle(signals, random);
        Collections.shuffle(secrets, random);
        List<String[]> pairs = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            String signal = signals.get(i % signals.size());
            String secret = secrets.get(i % secrets.size());
            if (i >= signals.size()) {
                signal = signal + (i / signals.size());
            }
            pairs.add(new String[]{signal, secret});
        }
        return pairs;
    }
}
