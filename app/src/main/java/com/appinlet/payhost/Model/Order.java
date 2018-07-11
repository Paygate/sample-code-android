package com.appinlet.payhost.Model;

import lombok.Builder;

@Builder
public class Order {
    private String MerchantOrderId;
    private String Currency;
    private String Amount;
    private String TransactionDate;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    Customer customer;
    Address address;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    BillingDetails BillingDetailsObject;
    ShippingDetails ShippingDetailsObject;
    AirlineBookingDetails AirlineBookingDetailsObject;
    private Language Locale;

    // Getter Methods

    public String getMerchantOrderId() {
        return MerchantOrderId;
    }

    public String getCurrency() {
        return Currency;
    }

    public String getAmount() {
        return Amount;
    }

    public String getTransactionDate() {
        return TransactionDate;
    }

    public BillingDetails getBillingDetails() {
        return BillingDetailsObject;
    }

    public ShippingDetails getShippingDetails() {
        return ShippingDetailsObject;
    }

    public AirlineBookingDetails getAirlineBookingDetails() {
        return AirlineBookingDetailsObject;
    }

    public Language getLocale() {
        return Locale;
    }

    // Setter Methods

    public void setMerchantOrderId(String MerchantOrderId) {
        this.MerchantOrderId = MerchantOrderId;
    }

    public void setCurrency(String Currency) {
        this.Currency = Currency;
    }

    public void setAmount(String Amount) {
        this.Amount = Amount;
    }

    public void setTransactionDate(String TransactionDate) {
        this.TransactionDate = TransactionDate;
    }

    public void setBillingDetails(BillingDetails BillingDetailsObject) {
        this.BillingDetailsObject = BillingDetailsObject;
    }

    public void setShippingDetails(ShippingDetails ShippingDetailsObject) {
        this.ShippingDetailsObject = ShippingDetailsObject;
    }

    public void setAirlineBookingDetails(AirlineBookingDetails AirlineBookingDetailsObject) {
        this.AirlineBookingDetailsObject = AirlineBookingDetailsObject;
    }

    public void setLocale(Language Locale) {
        this.Locale = Locale;
    }


    public String toXml() {
        return "            <ns1:Order>" +
                "               <ns1:MerchantOrderId>" + getMerchantOrderId() + "</ns1:MerchantOrderId>" +
                "               <ns1:Currency>" + getCurrency() + "</ns1:Currency>" +
                "               <ns1:Amount>" + getAmount() + "</ns1:Amount>" +
                "               <ns1:TransactionDate>" + getTransactionDate() + "</ns1:TransactionDate>" +
                (getBillingDetails() == null ? "" : getBillingDetails().toXml()) +
                (getShippingDetails() == null ? "" : getShippingDetails().toXml()) +
                (getAirlineBookingDetails() == null ? "" : getAirlineBookingDetails().toXml()) +
                (getLocale() == null ? "" : getLocale().toXml()) +
                "            </ns1:Order>";
    }
}
