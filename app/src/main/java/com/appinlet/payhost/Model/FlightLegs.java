package com.appinlet.payhost.Model;

public class FlightLegs {
    private String DepartureAirport;
    private String DepartureCountry;
    private String DepartureCity;
    private String DepartureDateTime;
    private String ArrivalAirport;
    private String ArrivalCountry;
    private String ArrivalCity;
    private String ArrivalDateTime;
    private String MarketingCarrierCode;
    private String MarketingCarrierName;
    private String IssuingCarrierCode;
    private String IssuingCarrierName;
    private String FlightNumber;
    private String BaseFareAmount;
    private String BaseFareCurrency;


    // Getter Methods

    public String getDepartureAirport() {
        return DepartureAirport;
    }

    public String getDepartureCountry() {
        return DepartureCountry;
    }

    public String getDepartureCity() {
        return DepartureCity;
    }

    public String getDepartureDateTime() {
        return DepartureDateTime;
    }

    public String getArrivalAirport() {
        return ArrivalAirport;
    }

    public String getArrivalCountry() {
        return ArrivalCountry;
    }

    public String getArrivalCity() {
        return ArrivalCity;
    }

    public String getArrivalDateTime() {
        return ArrivalDateTime;
    }

    public String getMarketingCarrierCode() {
        return MarketingCarrierCode;
    }

    public String getMarketingCarrierName() {
        return MarketingCarrierName;
    }

    public String getIssuingCarrierCode() {
        return IssuingCarrierCode;
    }

    public String getIssuingCarrierName() {
        return IssuingCarrierName;
    }

    public String getFlightNumber() {
        return FlightNumber;
    }

    public String getBaseFareAmount() {
        return BaseFareAmount;
    }

    public String getBaseFareCurrency() {
        return BaseFareCurrency;
    }

    // Setter Methods

    public void setDepartureAirport(String DepartureAirport) {
        this.DepartureAirport = DepartureAirport;
    }

    public void setDepartureCountry(String DepartureCountry) {
        this.DepartureCountry = DepartureCountry;
    }

    public void setDepartureCity(String DepartureCity) {
        this.DepartureCity = DepartureCity;
    }

    public void setDepartureDateTime(String DepartureDateTime) {
        this.DepartureDateTime = DepartureDateTime;
    }

    public void setArrivalAirport(String ArrivalAirport) {
        this.ArrivalAirport = ArrivalAirport;
    }

    public void setArrivalCountry(String ArrivalCountry) {
        this.ArrivalCountry = ArrivalCountry;
    }

    public void setArrivalCity(String ArrivalCity) {
        this.ArrivalCity = ArrivalCity;
    }

    public void setArrivalDateTime(String ArrivalDateTime) {
        this.ArrivalDateTime = ArrivalDateTime;
    }

    public void setMarketingCarrierCode(String MarketingCarrierCode) {
        this.MarketingCarrierCode = MarketingCarrierCode;
    }

    public void setMarketingCarrierName(String MarketingCarrierName) {
        this.MarketingCarrierName = MarketingCarrierName;
    }

    public void setIssuingCarrierCode(String IssuingCarrierCode) {
        this.IssuingCarrierCode = IssuingCarrierCode;
    }

    public void setIssuingCarrierName(String IssuingCarrierName) {
        this.IssuingCarrierName = IssuingCarrierName;
    }

    public void setFlightNumber(String FlightNumber) {
        this.FlightNumber = FlightNumber;
    }

    public void setBaseFareAmount(String BaseFareAmount) {
        this.BaseFareAmount = BaseFareAmount;
    }

    public void setBaseFareCurrency(String BaseFareCurrency) {
        this.BaseFareCurrency = BaseFareCurrency;
    }


    public String toXml() {
        return "                  <ns1:FlightLegs>" +
                "                     <ns1:DepartureAirport>" + getDepartureAirport() + "</ns1:DepartureAirport>" +
                "                     <ns1:DepartureCountry>" + getDepartureCountry() + "</ns1:DepartureCountry>" +
                "                     <ns1:DepartureCity>" + getDepartureCity() + "</ns1:DepartureCity>" +
                "                     <ns1:DepartureDateTime>" + getDepartureDateTime() + "</ns1:DepartureDateTime>" +
                "                     <ns1:ArrivalAirport>" + getArrivalAirport() + "</ns1:ArrivalAirport>" +
                "                     <ns1:ArrivalCountry>" + getArrivalCountry() + "</ns1:ArrivalCountry>" +
                "                     <ns1:ArrivalCity>" + getArrivalCity() + "</ns1:ArrivalCity>" +
                "                     <ns1:ArrivalDateTime>" + getArrivalDateTime() + "</ns1:ArrivalDateTime>" +
                "                     <ns1:MarketingCarrierCode>" + getMarketingCarrierCode() + "</ns1:MarketingCarrierCode>" +
                "                     <ns1:MarketingCarrierName>" + getMarketingCarrierName() + "</ns1:MarketingCarrierName>" +
                "                     <ns1:IssuingCarrierCode>" + getIssuingCarrierCode() + "</ns1:IssuingCarrierCode>" +
                "                     <ns1:IssuingCarrierName>" + getIssuingCarrierName() + "</ns1:IssuingCarrierName>" +
                "                     <ns1:FlightNumber>" + getFlightNumber() + "</ns1:FlightNumber>" +
                "                     <ns1:BaseFareAmount>" + getBaseFareAmount() + "</ns1:BaseFareAmount>" +
                "                     <ns1:BaseFareCurrency>" + getBaseFareCurrency() + "</ns1:BaseFareCurrency>" +
                "                  </ns1:FlightLegs>";
    }
}
