package com.appinlet.payhost.Model;

import lombok.Builder;

@Builder
public class Language {
    private String language;

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }


    public String toXml() {
        return
                "<ns1:Locale>" + getLanguage() + "</ns1:Locale>";
    }
}
