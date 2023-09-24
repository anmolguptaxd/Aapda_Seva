package com.example.aapdaseva;
public class Agency {
    private String agencyname;
    private String area;
    private String contactPerson;
    private String email; // Add email field as the unique identifier
    private double latitude; // Add latitude field
    private double longitude; // Add longitude field

    private String agencyNumber;

    public Agency() {

    }

    // Constructor with email
    public Agency(String agencyName, String area, String agencyNumber, String contactPerson, String email, double latitude, double longitude) {
        this.agencyname = agencyName;
        this.area = area;
        this.contactPerson = contactPerson;
        this.email = email; // Set the email from the constructor
        this.latitude = latitude; // Set the latitude from the constructor
        this.longitude = longitude; // Set the longitude from the constructor
        this.agencyNumber = agencyNumber;  // Initialize the new field
    }


    public String getName(){
        return agencyname;
    }

    // Getter and setter methods for email, latitude, and longitude
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAgencyNumber() {
        return agencyNumber;
    }

    public void setAgencyNumber(String agencyNumber) {
        this.agencyNumber = agencyNumber;
    }

    // Getter and setter methods for other fields
    public String getAgencyName() {
        return agencyname;
    }

    public void setAgencyName(String agencyName) {
        this.agencyname = agencyName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getCalamity() {
        return area;
    }
}
