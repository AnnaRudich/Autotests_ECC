package com.scalepoint.automation.shared;

import com.scalepoint.automation.utils.Configuration;

import java.util.HashMap;
import java.util.Map;

public class ClaimStatus {

    public static final String WORKING = "W";
    public static final String IN_PROGRESS = "P";
    public static final String SETTLED = "S";
    public static final String LOCKED = "L";
    public static final String CANCELED = "X";
    public static final String DROPED = "D";
    public static final String CLOSED_EXTERNAL = "E";

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
