package com.appinlet.payhost.Model;

import lombok.Builder;

@Builder
public class UserDefined {

    private String key;
    private String value;

    public UserDefined(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return
                "UserDefined{" +
                        "value = '" + value + '\'' +
                        ",key = '" + key + '\'' +
                        "}";
    }
}
