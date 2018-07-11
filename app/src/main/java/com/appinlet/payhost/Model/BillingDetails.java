package com.appinlet.payhost.Model;

import lombok.Builder;

@Builder
public class BillingDetails {
    Customer CustomerObject;
    Address AddressObject;


    // Getter Methods

    public Customer getCustomer() {
        return CustomerObject;
    }

    public Address getAddress() {
        return AddressObject;
    }

    // Setter Methods

    public void setCustomer(Customer CustomerObject) {
        this.CustomerObject = CustomerObject;
    }

    public void setAddress(Address AddressObject) {
        this.AddressObject = AddressObject;
    }

    public String toXml() {
        return "               <ns1:BillingDetails>" +
                (CustomerObject == null ? "" : CustomerObject.toxml()) +
                (AddressObject == null ? "" : AddressObject.toXml()) +
                "               </ns1:BillingDetails>";
    }
}
