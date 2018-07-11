package com.appinlet.payhost.Model;

import lombok.Builder;

@Builder
public class Risk {
    private String AccountNumber;
    private String IpV4Address;

    // Getter Methods

    public String getAccountNumber() {
        return AccountNumber;
    }

    public String getIpV4Address() {
        return IpV4Address;
    }

    // Setter Methods

    public void setAccountNumber(String AccountNumber) {
        this.AccountNumber = AccountNumber;
    }

    public void setIpV4Address(String IpV4Address) {
        this.IpV4Address = IpV4Address;
    }

    public String toXml() {
        return "<ns1:Risk>" +
                "<ns1:AccountNumber>" + getAccountNumber() + "</ns1:AccountNumber>" +
                "<ns1:IpV4Address>" + getIpV4Address() + "</ns1:IpV4Address>" +
                "</ns1:Risk>";
    }
}