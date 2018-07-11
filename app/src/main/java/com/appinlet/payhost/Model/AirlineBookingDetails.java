package com.appinlet.payhost.Model;

import lombok.Builder;

@Builder
public class AirlineBookingDetails {
    private String TicketNumber;
    private String PNR;
    Passengers PassengersObject;
    FlightLegs FlightLegsObject;


    // Getter Methods

    public String getTicketNumber() {
        return TicketNumber;
    }

    public String getPNR() {
        return PNR;
    }

    public Passengers getPassengers() {
        return PassengersObject;
    }

    public FlightLegs getFlightLegs() {
        return FlightLegsObject;
    }

    // Setter Methods

    public void setTicketNumber(String TicketNumber) {
        this.TicketNumber = TicketNumber;
    }

    public void setPNR(String PNR) {
        this.PNR = PNR;
    }

    public void setPassengers(Passengers PassengersObject) {
        this.PassengersObject = PassengersObject;
    }

    public void setFlightLegs(FlightLegs FlightLegsObject) {
        this.FlightLegsObject = FlightLegsObject;
    }


    public String toXml() {
        return "               <ns1:AirlineBookingDetails>" +
                "                  <ns1:TicketNumber>" + getTicketNumber() + "</ns1:TicketNumber>" +
                "                  <ns1:PNR>" + getPNR() + "</ns1:PNR>" +
                (getPassengers() == null ? "" : getPassengers().toXml()) +
                (getFlightLegs() == null ? "" : getFlightLegs().toXml()) +
                "               </ns1:AirlineBookingDetails>";
    }
}
