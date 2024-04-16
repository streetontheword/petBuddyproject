package vttp.server.Service;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;


import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import vttp.server.Model.AdoptedDogProfile;
import vttp.server.Model.FavoriteDog;
import vttp.server.Model.Inquiry;
import vttp.server.Repository.AdoptedPetRepository;

@Service
public class DogApiService {

    @Value("${client.id}")
    private String clientId;

    @Value("${client.secret}")
    private String clientSecret;

    private static final String BASE_URL = "https://api.petfinder.com/v2/";
    RestTemplate template = new RestTemplate();

    @Autowired
    AdoptedPetRepository adoptPetRepo;


    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public  List<AdoptedDogProfile> makeApiRequest() {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("grant_type", "client_credentials");
        requestBody.put("client_id", clientId);
        requestBody.put("client_secret", clientSecret);

        // set HttpHeaders
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("https://api.petfinder.com/v2/oauth2/token",
                requestEntity, String.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String access = responseEntity.getBody();
            JsonReader jsonReader = Json.createReader(new StringReader(access));
            JsonObject jsonObject = jsonReader.readObject();
            String accessToken = jsonObject.getString("access_token");
            String category = "dog";
            List<AdoptedDogProfile> listOfDogs = this.getAllDogs(accessToken, category);
            return listOfDogs;
        } else {
            throw new RuntimeException(
                    "Failed to obtain access token. HTTP status code: " + responseEntity.getStatusCode());
        }
    }
    


    public List<AdoptedDogProfile> getAllDogs(String accessToken, String category) {

        String endpoint = "animals";
        String limit = "100";

        String url = UriComponentsBuilder.fromUriString(BASE_URL + endpoint)
                .queryParam("type", category)
                .queryParam("limit", limit)
                .toUriString();
        // ok
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = template.exchange(url, HttpMethod.GET, requestEntity, String.class);
        // System.out.println("THISIS THE RESULT>>>" + responseEntity.getBody());
        // //works
        JsonArray animals;
        JsonReader jsonReader = Json.createReader(new StringReader(responseEntity.getBody()));
        JsonObject jsonObj = jsonReader.readObject();
        // System.out.println("response>>>>" + jsonObj); // works
        animals = jsonObj.getJsonArray("animals");
        // System.out.println(animals); //works too
        
        List<AdoptedDogProfile> listOfDogsProfiles = new ArrayList<>();
        
        for (JsonValue jsonValue : animals) {
            AdoptedDogProfile dog = new AdoptedDogProfile();

            JsonObject jsonObject = jsonValue.asJsonObject();
            // System.out.println(jsonObject);
            int id = jsonObject.getInt("id");
            // System.out.println(id);

            // for breeds
            JsonObject breedsObject = jsonObject.getJsonObject("breeds");
            List<String> listOfBreeds = new ArrayList<>();
            // Breeds breed = new Breeds();
            String primaryBreed = breedsObject.getString("primary");
            String secondaryBreed = breedsObject.isNull("secondary") ? null : breedsObject.getString("secondary");
           
            // breed.setMixed(breedsObject.getBoolean("mixed"));
            // breed.setUnknown(breedsObject.getBoolean("unknown"));
            dog.setPrimaryBreed(primaryBreed);
            dog.setSecondaryBreed(secondaryBreed);
            Boolean isMixed = breedsObject.getBoolean("mixed");
            Boolean isUnknown = breedsObject.getBoolean("unknown");
           dog.setIsMixed(isMixed);
           dog.setIsUnknown(isUnknown);
            // System.out.println("list of breeds " + breed);

            // for age, gender, size, coat, description, published
            String age = jsonObject.getString("age");
            String gender = jsonObject.getString("gender");
            String size = jsonObject.getString("size");
            String coat = jsonObject.isNull("coat") ? null : jsonObject.getString("coat");
            String description = jsonObject.isNull("description") ? null : jsonObject.getString("description");
            String name = jsonObject.getString("name");
            //System.out.println(age + gender + size + coat + description + name);
            String published = jsonObject.getString("published_at");

            // for colors
            JsonObject colorObject = jsonObject.getJsonObject("colors");
            String primaryColor = colorObject.isNull("primary") ? null : colorObject.getString("primary");
            String secondaryColor = colorObject.isNull("secondary") ? null : colorObject.getString("secondary");
            String tertiaryColor = colorObject.isNull("tertiary") ? null : colorObject.getString("tertiary");

            // System.out.println(primaryColor + secondaryColor + tertiaryColor);
            List<String> listOfColors = new ArrayList<>();
            listOfColors.add(primaryColor);
            listOfColors.add(secondaryColor);
            listOfColors.add(tertiaryColor);
            // System.out.println("list of colors " + listOfColors);

            // for tags
            JsonArray tags = jsonObject.getJsonArray("tags");
            // System.out.println("tags>>>" + tags);
            if (tags != null) {
                for (JsonValue jsonValue2 : tags) {
                    List<String> listOfCharacteristics = new ArrayList<>();
                    String tagsValue = jsonValue2.toString();
                    listOfCharacteristics.add(tagsValue);
                    dog.setCharacteristics(listOfCharacteristics);
                   // System.out.println("tags >> " + tagsValue);

                }
            } else {
                System.out.println("Tags are null");
            }

            // for environment
            JsonObject environmentObject = jsonObject.getJsonObject("environment");
            Boolean children = environmentObject.isNull("children") ? null : environmentObject.getBoolean("children");
            Boolean dogs = environmentObject.isNull("dogs") ? null : environmentObject.getBoolean("dogs");
            Boolean cats = environmentObject.isNull("cats") ? null : environmentObject.getBoolean("cats");

            dog.setIsGoodWithChildren(children);
            dog.setIsGoodWithDogs(dogs);
            dog.setIsGoodWithCats(cats);

            //System.out.println("list of environments " + listofEnvironments);

            // for photos
            JsonArray photos = jsonObject.getJsonArray("photos");
            List<String> listOfUrls = new ArrayList<>();
            if (photos != null) {
                for (JsonValue jsonPic : photos) {
                    JsonObject jsonPicObject = jsonPic.asJsonObject();
                    String picUrl = jsonPicObject.getString("full");
                    listOfUrls.add(picUrl);
                    dog.setUrl(listOfUrls);
                    System.out.println("pictures " + picUrl);
                }
            } else {
                System.out.println("photos are null");

            }

            // for attributes
            JsonObject attributeObject = jsonObject.getJsonObject("attributes");
            Boolean spayed = attributeObject.isNull("spayed_neutered") ? null
                    : attributeObject.getBoolean("spayed_neutered");
            Boolean houseTrained = attributeObject.isNull("house_trained") ? null
                    : attributeObject.getBoolean("house_trained");
            Boolean declawed = attributeObject.isNull("declawed") ? null : attributeObject.getBoolean("declawed");
            Boolean specialNeeds = attributeObject.isNull("special_needs") ? null
                    : attributeObject.getBoolean("special_needs");
            Boolean currentShots = attributeObject.isNull("shots_current") ? null
                    : attributeObject.getBoolean("shots_current");
            dog.setIsSpayedAndNeutered(spayed);
            dog.setIsHouseTrained(houseTrained);
            dog.setIsDeclawed(declawed);
            dog.setIsSpecialNeeds(specialNeeds);
            dog.setIsVaccinated(currentShots);

            //System.out.println("list of attributes " + listOfAttributes);

            dog.setName(name);
            dog.setId(id);
            dog.setAge(age);
            dog.setCoat(coat);
            dog.setGender(gender);
            dog.setSize(size);
            dog.setDescription(description);
          
            
            dog.setColor(listOfColors);
            dog.setPublished(published);
            listOfDogsProfiles.add(dog);
           // System.out.println(dog);

        }
        return listOfDogsProfiles;
    }

    public Long getCount(){
       return adoptPetRepo.getCount();
    }


    public void saveToMongo(List<AdoptedDogProfile>listOfAdoptedDogs){
        adoptPetRepo.saveToMongo(listOfAdoptedDogs);
        
    }

    public String saveToMongoIndivDog(AdoptedDogProfile adoptedDogProfile){
        return adoptPetRepo.saveToMongoIndivDog(adoptedDogProfile);
        
    }

    public String saveToS3(MultipartFile imageFile) {

        // String id = news.getId();
        String id = UUID.randomUUID().toString().substring(0, 8);

        String url;

        try {
            url = adoptPetRepo.saveToS3(id, imageFile.getInputStream(), imageFile.getContentType(), imageFile.getSize());
            System.out.println("In Svc>>>>> url retrieved" + url);
            
            return url;

        } catch (IOException e) {

            e.printStackTrace();
        }
        return id;
    }

    public List<AdoptedDogProfile> getDogFromMongo(int pageSize, int skip){
        return adoptPetRepo.getDogsFromMongo(pageSize,skip);
    }

    public List<AdoptedDogProfile> getDogFromMongoWithoutPagiantion(){
        return adoptPetRepo.getDogsFromMongoWithoutPagination();
    }

    public AdoptedDogProfile getIndivDog(int id){
        return adoptPetRepo.findOneAdoptedDog(id);
    }

    public Boolean favoriteDog(FavoriteDog favDog, String userId){
        return adoptPetRepo.favoriteDog(favDog, userId);
    }

    public List<FavoriteDog> retreiveFavorites(String userId){
        return adoptPetRepo.getlistOfFavoritedDogs(userId);
    }

    public String deletedSaved(int petId, String userId){
        return adoptPetRepo.deleteSaved(petId, userId);
    }

    public Boolean makeInquiry(Inquiry inquiry, String userid){
        String inquiryId = UUID.randomUUID().toString().substring(0, 8);
        inquiry.setInquiryId(inquiryId);
        return adoptPetRepo.makeInquiry(inquiry, userid);
        
    }

    public List<Inquiry> getAllInquiry() throws ParseException{
       return adoptPetRepo.getInquiry();
    }

    public List<Inquiry> getConfirmedInquiry() throws ParseException{
       
        return adoptPetRepo.getConfirmedInquiry();
     }
 

    public String acceptInquiry(String inquiryId, String userId){

        return adoptPetRepo.acceptInquiry(inquiryId, userId);
    }

    public String declineInquiry(String inquiryId, String userId){
  
        return adoptPetRepo.declineInquiry(inquiryId, userId);
    }

    public int deleteAdoptedDog(int petId){
     adoptPetRepo.deleteAdoptedDog(petId);
     return petId;

    }

    public Inquiry getConfirmedAppointment(String inquiryId) throws ParseException{
        return adoptPetRepo.getConfirmedAppointment(inquiryId);
    }

 
}
