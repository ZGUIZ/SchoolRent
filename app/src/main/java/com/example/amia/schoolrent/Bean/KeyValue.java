package com.example.amia.schoolrent.Bean;


import org.litepal.crud.LitePalSupport;

public class KeyValue extends LitePalSupport {
    private String key;
    private String value;

    public KeyValue() {
    }

    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
