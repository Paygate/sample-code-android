package com.appinlet.payhost.Model;


import lombok.Builder;

@Builder
public class Customer {
        private String Title;
        private String FirstName;
        private String LastName;

    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }

    private String MiddleName;
        private String Telephone;
        private String Mobile;
        private String Email;
        private String DateOfBirth;

        private Address address;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public int getSocialSecurityNumber() {
        return SocialSecurityNumber;
    }

    public void setSocialSecurityNumber(int socialSecurityNumber) {
        SocialSecurityNumber = socialSecurityNumber;
    }

    private int SocialSecurityNumber;

    public String getFax() {
        return Fax;
    }

    public void setFax(String fax) {
        Fax = fax;
    }

    private String Fax;



        // Getter Methods 

        public String getTitle() {
            return Title;
        }

        public String getFirstName() {
            return FirstName;
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

        public String getEmail() {
            return Email;
        }

        // Setter Methods 

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public void setFirstName(String FirstName) {
            this.FirstName = FirstName;
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

        public void setEmail(String Email) {
            this.Email = Email;
        }


    public String toxml() {
        return "            <ns1:Customer>" +
                "               <ns1:Title>" + getTitle() + "</ns1:Title>" +
                "               <ns1:FirstName>" + getFirstName() + "</ns1:FirstName>" +
                "               <ns1:MiddleName>" + getMiddleName() + "</ns1:MiddleName>" +
                "               <ns1:LastName>" + getLastName() + "</ns1:LastName>" +
                "               <ns1:Telephone>" + getTelephone() + "</ns1:Telephone>" +
                "               <ns1:Mobile>" + getMobile() + "</ns1:Mobile>" +
                "               <ns1:Fax>" + getFax() + "</ns1:Fax>" +
                "               <ns1:Email>" + getEmail() + "</ns1:Email>" +
                "               <ns1:DateOfBirth>" + getDateOfBirth() + "</ns1:DateOfBirth>" +
                "               <ns1:SocialSecurityNumber>" + getSocialSecurityNumber() + "</ns1:SocialSecurityNumber>" +
                (getAddress()==null?"":getAddress().toXml()) +
                "            </ns1:Customer>" ;
    }
}