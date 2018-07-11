package com.appinlet.payhost.Model;

import lombok.Builder;

@Builder
public class Passengers {
    Passenger PassengerObject;
    private String TravellerType;

    // Getter Methods

    public Passenger getPassenger() {
        return PassengerObject;
    }

    public String getTravellerType() {
        return TravellerType;
    }

    // Setter Methods

    public void setPassenger(Passenger PassengerObject) {
        this.PassengerObject = PassengerObject;
    }

    public void setTravellerType(String TravellerType) {
        this.TravellerType = TravellerType;
    }


    public String toXml() {
        return "                  <ns1:Passengers>" +
                (getPassenger() == null ? "" : getPassenger().toXml()) +
                "                     <ns1:TravellerType>" + getTravellerType() + "</ns1:TravellerType>" +
                "                  </ns1:Passengers>";
    }
}
