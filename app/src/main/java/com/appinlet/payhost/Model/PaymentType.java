package com.appinlet.payhost.Model;

import lombok.Builder;

@Builder
public class PaymentType {
    private String Method;
    private String Detail;

    // Getter Methods

    public String getMethod() {
        return Method;
    }

    public String getDetail() {
        return Detail;
    }

    // Setter Methods

    public void setMethod(String Method) {
        this.Method = Method;
    }

    public void setDetail(String Detail) {
        this.Detail = Detail;
    }

    public String toXml() {
        return "<ns1:PaymentType>" +
                "<ns1:Method>" + getMethod() + "</ns1:Method>" +
                "<ns1:Detail>" + getDetail() + "</ns1:Detail>" +
                "</ns1:PaymentType>";
    }
}