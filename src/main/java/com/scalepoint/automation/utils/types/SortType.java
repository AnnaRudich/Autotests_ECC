package com.scalepoint.automation.utils.types;

public enum SortType {
    ASC ("ascending"),
    DESC ("descending");

    private String extJsValue;

    SortType(String extJsValue) {
        this.extJsValue = extJsValue;
    }

    public String getExtJsValue() {
        return extJsValue;
    }

    public boolean isExtJsValueEqualTo(String attributeValue) {
        return extJsValue.equals(attributeValue);
    }
}
