package com.scalepoint.automation.domain;

import com.scalepoint.automation.utils.Configuration;

import java.util.HashMap;
import java.util.Map;

public class ClaimStatus {

    private static final Map<Locale, String> saved = new HashMap<>();
    private static final Map<Locale, String> cancelled = new HashMap<>();
    private static final Map<Locale, String> closedExternally = new HashMap<>();
    private static final Map<Locale, String> completed = new HashMap<>();

    static {
        saved.put(Locale.DK, "Åben");
        cancelled.put(Locale.DK, "Annulleret");
        closedExternally.put(Locale.DK, "Lukket eksternt");
        completed.put(Locale.DK, "Afsluttet");
    }

    public static String saved() {
        return saved.get(Configuration.getLocale());
    }

    public static String cancelled() {
        return cancelled.get(Configuration.getLocale());
    }

    public static String closed() {
        return closedExternally.get(Configuration.getLocale());
    }

    public static String completed() {
        return completed.get(Configuration.getLocale());
    }
}
