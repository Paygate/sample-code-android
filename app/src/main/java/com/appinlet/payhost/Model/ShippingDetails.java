package com.appinlet.payhost.Model;

import lombok.Builder;

@Builder
public class ShippingDetails {
    Customer CustomerObject;
    Address AddressObject;
    private String DeliveryDate;
    private String DeliveryMethod;
    private String InstallationRequested;

    // Getter Methods

    public Customer getCustomer() {
        return CustomerObject;
    }

    public Address getAddress() {
        return AddressObject;
    }

    public String getDeliveryDate() {
        return DeliveryDate;
    }

    public String getDeliveryMethod() {
        return DeliveryMethod;
    }

    public String getInstallationRequested() {
        return InstallationRequested;
    }

    // Setter Methods

    public void setCustomer(Customer CustomerObject) {
        this.CustomerObject = CustomerObject;
    }

    public void setAddress(Address AddressObject) {
        this.AddressObject = AddressObject;
    }

    public void setDeliveryDate(String DeliveryDate) {
        this.DeliveryDate = DeliveryDate;
    }

    public void setDeliveryMethod(String DeliveryMethod) {
        this.DeliveryMethod = DeliveryMethod;
    }

    public void setInstallationRequested(String InstallationRequested) {
        this.InstallationRequested = InstallationRequested;
    }

    public String toXml() {
        return "               <ns1:ShippingDetails>" +
                "                  <!-- Customer Details -->" +
                (getCustomer() == null ? "" : getCustomer().toxml()) +
                "                  <!-- Address Details -->" +
                (getAddress() == null ? "" : getAddress().toXml()) +
                "                  <ns1:DeliveryDate>" + getDeliveryDate() + "</ns1:DeliveryDate>" +
                "                  <ns1:DeliveryMethod>" + getDeliveryMethod() + "</ns1:DeliveryMethod>" +
                "                  <ns1:InstallationRequested>" + getInstallationRequested() + "</ns1:InstallationRequested>" +
                "               </ns1:ShippingDetails>";
    }
}
