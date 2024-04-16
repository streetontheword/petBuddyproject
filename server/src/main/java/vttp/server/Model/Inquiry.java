package vttp.server.Model;

import java.util.Date;

public class Inquiry {
    

    private String inquiryId; 
    private String firstName; 
    private String lastName; 
    private String email; 
    private Date birthdate; 
    private String gender; 
    private Date intended_visit_date; 
    private String nationality; 
    private String other;
    private Integer petId;
    private String userId;
    private Boolean firstTimeOwner; 
    private String selectedHour;
    private String url; 
    private String dogName; 
    
    public String getInquiryId() {
        return inquiryId;
    }
    public void setInquiryId(String inquiryId) {
        this.inquiryId = inquiryId;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    
    
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    
    public Date getBirthdate() {
        return birthdate;
    }
    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }
    public Date getIntended_visit_date() {
        return intended_visit_date;
    }
    public void setIntended_visit_date(Date intended_visit_date) {
        this.intended_visit_date = intended_visit_date;
    }
    public String getNationality() {
        return nationality;
    }
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
    public String getOther() {
        return other;
    }
    public void setOther(String other) {
        this.other = other;
    }
    
    public Integer getPetId() {
        return petId;
    }
    public void setPetId(Integer petId) {
        this.petId = petId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public Boolean getFirstTimeOwner() {
        return firstTimeOwner;
    }
    public void setFirstTimeOwner(Boolean firstTimeOwner) {
        this.firstTimeOwner = firstTimeOwner;
    }
    
    public String getSelectedHour() {
        return selectedHour;
    }
    public void setSelectedHour(String selectedHour) {
        this.selectedHour = selectedHour;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getDogName() {
        return dogName;
    }
    public void setDogName(String dogName) {
        this.dogName = dogName;
    }

    public Inquiry() {
    }
    public Inquiry(String inquiryId, String firstName, String lastName, String email, Date birthdate, String gender,
            Date intended_visit_date, String nationality, String other, Integer petId, String userId,
            Boolean firstTimeOwner, String selectedHour, String url, String dogName) {
        this.inquiryId = inquiryId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthdate = birthdate;
        this.gender = gender;
        this.intended_visit_date = intended_visit_date;
        this.nationality = nationality;
        this.other = other;
        this.petId = petId;
        this.userId = userId;
        this.firstTimeOwner = firstTimeOwner;
        this.selectedHour = selectedHour;
        this.url = url;
        this.dogName = dogName;
    }
    @Override
    public String toString() {
        return "Inquiry [inquiryId=" + inquiryId + ", firstName=" + firstName + ", lastName=" + lastName + ", email="
                + email + ", birthdate=" + birthdate + ", gender=" + gender + ", intended_visit_date="
                + intended_visit_date + ", nationality=" + nationality + ", other=" + other + ", petId=" + petId
                + ", userId=" + userId + ", firstTimeOwner=" + firstTimeOwner + ", selectedHour=" + selectedHour
                + ", url=" + url + ", dogName=" + dogName + "]";
    }
   
    

    

    
}
