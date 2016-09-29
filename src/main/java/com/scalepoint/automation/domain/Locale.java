package com.scalepoint.automation.domain;

public enum Locale {
    DK("dk");

    private String value;

    Locale(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Locale get(String locale) {
        for (Locale l : Locale.values()) {
            if (l.value.equalsIgnoreCase(locale)) {
                return l;
            }
        }
        return DK;
    }
}
