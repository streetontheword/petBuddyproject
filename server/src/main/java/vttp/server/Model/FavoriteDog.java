package vttp.server.Model;

public class FavoriteDog {


    private int petId; 
    private String name; 
    private String gender; 
    private String primaryBreed; 
    private String secondaryBreed; 
    private String url; 

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
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getPrimaryBreed() {
        return primaryBreed;
    }
   
    public void setPrimaryBreed(String primaryBreed) {
        this.primaryBreed = primaryBreed;
    }
  
    public String getSecondaryBreed() {
        return secondaryBreed;
    }
    public void setSecondaryBreed(String secondaryBreed) {
        this.secondaryBreed = secondaryBreed;
    }
    
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    
   
    
    public FavoriteDog(int petId, String name, String gender, String primaryBreed, String secondaryBreed, String url) {
        this.petId = petId;
        this.name = name;
        this.gender = gender;
        this.primaryBreed = primaryBreed;
        this.secondaryBreed = secondaryBreed;
        this.url = url;
    }
    @Override
    public String toString() {
        return "FavoriteDog [petId=" + petId + ", name=" + name + ", gender=" + gender + ", primaryBreed="
                + primaryBreed + ", secondaryBreed=" + secondaryBreed + ", url=" + url + "]";
    }
    public FavoriteDog() {
    }
    

    
}
