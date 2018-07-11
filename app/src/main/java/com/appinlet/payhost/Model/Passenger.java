package com.appinlet.payhost.Model;

import lombok.Builder;

@Builder
public class Passenger {
    private String Title;
    private String FirstName;
    private String MiddleName;
    private String LastName;
    private String Telephone;
    private String Mobile;
    private String Fax;
    private String Email;
    private String DateOfBirth;
    private String SocialSecurityNumber;


    // Getter Methods

    public String getTitle() {
        return Title;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getMiddleName() {
        return MiddleName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getTelephone() {
        return Telephone;
    }

    public String getMobile() {
        return Mobile;
    }

    public String getFax() {
        return Fax;
    }

    public String getEmail() {
        return Email;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public String getSocialSecurityNumber() {
        return SocialSecurityNumber;
    }

    // Setter Methods

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    public void setMiddleName(String MiddleName) {
        this.MiddleName = MiddleName;
    }

    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    public void setTelephone(String Telephone) {
        this.Telephone = Telephone;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public void setFax(String Fax) {
        this.Fax = Fax;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public void setDateOfBirth(String DateOfBirth) {
        this.DateOfBirth = DateOfBirth;
    }

    public void setSocialSecurityNumber(String SocialSecurityNumber) {
        this.SocialSecurityNumber = SocialSecurityNumber;
    }


    public String toXml() {
        return "                     <ns1:Passenger>" +
                "                        <ns1:Title>" + getTitle() + "</ns1:Title>" +
                "                        <ns1:FirstName>" + getFirstName() + "</ns1:FirstName>" +
                "                        <ns1:MiddleName>" + getFirstName() + "</ns1:MiddleName>" +
                "                        <ns1:LastName>" + getLastName() + "</ns1:LastName>" +
                "                        <ns1:Telephone>" + getTelephone() + "</ns1:Telephone>" +
                "                        <ns1:Mobile>" + getMobile() + "</ns1:Mobile>" +
                "                        <ns1:Fax>" + getFax() + "</ns1:Fax>" +
                "                        <ns1:Email>" + getEmail() + "</ns1:Email>" +
                "                        <ns1:DateOfBirth>" + getDateOfBirth() + "</ns1:DateOfBirth>" +
                "                        <ns1:SocialSecurityNumber>" + getSocialSecurityNumber() + "</ns1:SocialSecurityNumber>" +
                "                     </ns1:Passenger>";
    }
}
