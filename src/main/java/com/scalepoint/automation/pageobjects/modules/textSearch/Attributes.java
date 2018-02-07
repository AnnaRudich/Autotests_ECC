package com.scalepoint.automation.pageobjects.modules.textSearch;

import org.openqa.selenium.By;

public enum Attributes {
    SMARTPHONE_NEJ("Smartphone", new YesNoAttributeAction(), "Nej", "og kun denne"),
    NFC_NEJ("NFC (Near Field Communication)", new YesNoAttributeAction(), "Nej" ),
    DUAL_KAMERA_NEJ("Dual kamera (bag)", new YesNoAttributeAction(),"Nej","og kun denne"),
    GPS_NEJ("GPS", new YesNoAttributeAction(), "Nej", "og kun denne");

    private String name;
    private By by;
    private SearchAttributesActions action;
    private String[] options;


    Attributes(String name, SearchAttributesActions action, String... options) {
        this.name = name;
        this.by = By.xpath(String.format("//span[text()='%s']", name));
        this.action = action;
        this.options = options;
    }

    public String getName() {
        return name;
    }

    public By getBy() {
        return by;
    }

    public SearchAttributesActions getAction() {
        return action;
    }

    public String[] getOptions() {
        return options;
    }

    public void selectAttribute() {
        action.selectAttribute(this);
    }

}
