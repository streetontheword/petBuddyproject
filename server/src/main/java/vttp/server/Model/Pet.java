package vttp.server.Model;

import java.util.Date;

public class Pet {

    private int petId; 
    private String name; 
    private Date dateOfBirth;
    private Date dateOfLastVaccination; 
    private String gender; 
    private String comments; 
    private String breed;  
    private String userId;
    private String imageUrl;
    private String microchipNumber; 
    
    public int getPetId() {
        return petId;
    }
    public void setPetId(int petId) {
        this.petId = petId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public Date getDateOfLastVaccination() {
        return dateOfLastVaccination;
    }
    public void setDateOfLastVaccination(Date dateOfLastVaccination) {
        this.dateOfLastVaccination = dateOfLastVaccination;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }
    public String getBreed() {
        return breed;
    }
    public void setBreed(String breed) {
        this.breed = breed;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
      public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    } 

    

   
    public Pet(int petId, String name, Date dateOfBirth, Date dateOfLastVaccination, String gender, String comments,
            String breed, String userId, String imageUrl, String microchipNumber) {
        this.petId = petId;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.dateOfLastVaccination = dateOfLastVaccination;
        this.gender = gender;
        this.comments = comments;
        this.breed = breed;
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.microchipNumber = microchipNumber;
    }
    public Pet() {
    }
   
    @Override
    public String toString() {
        return "Pet [petId=" + petId + ", name=" + name + ", dateOfBirth=" + dateOfBirth + ", dateOfLastVaccination="
                + dateOfLastVaccination + ", gender=" + gender + ", comments=" + comments + ", breed=" + breed
                + ", userId=" + userId + ", imageUrl=" + imageUrl + ", microchipNumber=" + microchipNumber + "]";
    }
    public String getMicrochipNumber() {
        return microchipNumber;
    }
    public void setMicrochipNumber(String microchipNumber) {
        this.microchipNumber = microchipNumber;
    }
   
    


    
}
