package com.appinlet.payhost.Model;

import java.util.ArrayList;

import lombok.Builder;

@Builder
public class Address {


 private String City;
 private String Country;
 private String State;
 private String Zip;
 private String AddressLine1;
 private String AddressLine2;

 public String getAddressLine1() {
  return AddressLine1;
 }

 public void setAddressLine1(String addressLine1) {
  AddressLine1 = addressLine1;
 }

 public String getAddressLine2() {
  return AddressLine2;
 }

 public void setAddressLine2(String addressLine2) {
  AddressLine2 = addressLine2;
 }

 public String getAddressLine3() {
  return AddressLine3;
 }

 public void setAddressLine3(String addressLine3) {
  AddressLine3 = addressLine3;
 }

 private String AddressLine3;


 // Getter Methods 

 public String getCity() {
  return City;
 }

 public String getCountry() {
  return Country;
 }

 public String getState() {
  return State;
 }

 public String getZip() {
  return Zip;
 }

 // Setter Methods 

 public void setCity(String City) {
  this.City = City;
 }

 public void setCountry(String Country) {
  this.Country = Country;
 }

 public void setState(String State) {
  this.State = State;
 }

 public void setZip(String Zip) {
  this.Zip = Zip;
 }

 public String toXml() {
   return "               <ns1:Address>" +
           "                  <ns1:AddressLine>"+getAddressLine1()+"</ns1:AddressLine>" +
           "                  <ns1:AddressLine>"+getAddressLine2()+"</ns1:AddressLine>" +
           "                  <ns1:AddressLine>"+getAddressLine3()+"</ns1:AddressLine>" +
           "                  <ns1:City>"+getCity()+"</ns1:City>" +
           "                  <ns1:Country>"+getCountry()+"</ns1:Country>" +
           "                  <ns1:State>"+getState()+"</ns1:State>" +
           "                  <ns1:Zip>"+getZip()+"</ns1:Zip>" +
           "               </ns1:Address>";
 }
}