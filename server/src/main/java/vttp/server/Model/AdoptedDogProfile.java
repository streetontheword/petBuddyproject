package vttp.server.Model;

import java.util.List;


public class AdoptedDogProfile {

    
    private int id; 
    private String name; 
    private String age;
    private String gender; 
    private String size; 
    private String coat; 
    private List<String> url; 
    private String published;
    private List<String> characteristics;   //tags 
    private Boolean isGoodWithDogs; 
    private Boolean isGoodWithChildren; 
    private Boolean isGoodWithCats; 
    private Boolean isSpayedAndNeutered;
    private Boolean isHouseTrained;
    private Boolean isDeclawed;
    private Boolean isSpecialNeeds;
    private Boolean isVaccinated;
    private Boolean isMixed; 
    private Boolean isUnknown;
    private String primaryBreed; 
    private String secondaryBreed; 
    // private List<String> breeds; 
    private List<String> color; 
    private String description;

    

    
    public Boolean getIsMixed() {
        return isMixed;
    }
    public void setIsMixed(Boolean isMixed) {
        this.isMixed = isMixed;
    }
    public Boolean getIsUnknown() {
        return isUnknown;
    }
    public void setIsUnknown(Boolean isUnknown) {
        this.isUnknown = isUnknown;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }
    public String getCoat() {
        return coat;
    }
    public void setCoat(String coat) {
        this.coat = coat;
    }
    public List<String> getUrl() {
        return url;
    }
    public void setUrl(List<String> url) {
        this.url = url;
    }
    public String getPublished() {
        return published;
    }
    public void setPublished(String published) {
        this.published = published;
    }
    public List<String> getCharacteristics() {
        return characteristics;
    }
    public void setCharacteristics(List<String> characteristics) {
        this.characteristics = characteristics;
    }
    public Boolean getIsGoodWithDogs() {
        return isGoodWithDogs;
    }
    public void setIsGoodWithDogs(Boolean isGoodWithDogs) {
        this.isGoodWithDogs = isGoodWithDogs;
    }
    public Boolean getIsGoodWithChildren() {
        return isGoodWithChildren;
    }
    public void setIsGoodWithChildren(Boolean isGoodWithChildren) {
        this.isGoodWithChildren = isGoodWithChildren;
    }
    public Boolean getIsGoodWithCats() {
        return isGoodWithCats;
    }
    public void setIsGoodWithCats(Boolean isGoodWithCats) {
        this.isGoodWithCats = isGoodWithCats;
    }
    public Boolean getIsSpayedAndNeutered() {
        return isSpayedAndNeutered;
    }
    public void setIsSpayedAndNeutered(Boolean isSpayedAndNeutered) {
        this.isSpayedAndNeutered = isSpayedAndNeutered;
    }
    public Boolean getIsHouseTrained() {
        return isHouseTrained;
    }
    public void setIsHouseTrained(Boolean isHouseTrained) {
        this.isHouseTrained = isHouseTrained;
    }
    public Boolean getIsDeclawed() {
        return isDeclawed;
    }
    public void setIsDeclawed(Boolean isDeclawed) {
        this.isDeclawed = isDeclawed;
    }
    public Boolean getIsSpecialNeeds() {
        return isSpecialNeeds;
    }
    public void setIsSpecialNeeds(Boolean isSpecialNeeds) {
        this.isSpecialNeeds = isSpecialNeeds;
    }
    public Boolean getIsVaccinated() {
        return isVaccinated;
    }
    public void setIsVaccinated(Boolean isVaccinated) {
        this.isVaccinated = isVaccinated;
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
    public List<String> getColor() {
        return color;
    }
    public void setColor(List<String> color) {
        this.color = color;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
   
    public AdoptedDogProfile() {
    }
    public AdoptedDogProfile(int id, String name, String age, String gender, String size, String coat, List<String> url,
            String published, List<String> characteristics, Boolean isGoodWithDogs, Boolean isGoodWithChildren,
            Boolean isGoodWithCats, Boolean isSpayedAndNeutered, Boolean isHouseTrained, Boolean isDeclawed,
            Boolean isSpecialNeeds, Boolean isVaccinated, Boolean isMixed, Boolean isUnknown, String primaryBreed,
            String secondaryBreed, List<String> color, String description) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.size = size;
        this.coat = coat;
        this.url = url;
        this.published = published;
        this.characteristics = characteristics;
        this.isGoodWithDogs = isGoodWithDogs;
        this.isGoodWithChildren = isGoodWithChildren;
        this.isGoodWithCats = isGoodWithCats;
        this.isSpayedAndNeutered = isSpayedAndNeutered;
        this.isHouseTrained = isHouseTrained;
        this.isDeclawed = isDeclawed;
        this.isSpecialNeeds = isSpecialNeeds;
        this.isVaccinated = isVaccinated;
        this.isMixed = isMixed;
        this.isUnknown = isUnknown;
        this.primaryBreed = primaryBreed;
        this.secondaryBreed = secondaryBreed;
        this.color = color;
        this.description = description;
    }
    @Override
    public String toString() {
        return "AdoptedDogProfile [id=" + id + ", name=" + name + ", age=" + age + ", gender=" + gender + ", size="
                + size + ", coat=" + coat + ", url=" + url + ", published=" + published + ", characteristics="
                + characteristics + ", isGoodWithDogs=" + isGoodWithDogs + ", isGoodWithChildren=" + isGoodWithChildren
                + ", isGoodWithCats=" + isGoodWithCats + ", isSpayedAndNeutered=" + isSpayedAndNeutered
                + ", isHouseTrained=" + isHouseTrained + ", isDeclawed=" + isDeclawed + ", isSpecialNeeds="
                + isSpecialNeeds + ", isVaccinated=" + isVaccinated + ", isMixed=" + isMixed + ", isUnknown="
                + isUnknown + ", primaryBreed=" + primaryBreed + ", secondaryBreed=" + secondaryBreed + ", color="
                + color + ", description=" + description + "]";
    }
   

   

   


    

    
}
