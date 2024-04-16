package vttp.server.Controller;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.server.Model.AdoptedDogProfile;
import vttp.server.Model.FavoriteDog;
import vttp.server.Model.Inquiry;
import vttp.server.Service.DogApiService;

@RestController
@CrossOrigin
@RequestMapping(path = "/api")
public class AdoptionController {

    @Autowired
    DogApiService adoptedApiSvc;

    @GetMapping(path = "/dog")
    public ResponseEntity<List<AdoptedDogProfile>> getAllDogs(String type) {

        List<AdoptedDogProfile> dog = adoptedApiSvc.makeApiRequest();
        // System.out.println("hello>>" + accessToken);
        // type = "dog";
        // List<AdoptedDogProfile> dog = adoptedApiSvc.getAllDogs(accessToken, type);
        adoptedApiSvc.saveToMongo(dog);
    

        return ResponseEntity.ok().body(dog);

    }

    @GetMapping(path = "/getDogs")
    public ResponseEntity<List<AdoptedDogProfile>> getDogs(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "21") int pageSize) {

        int skip = page * pageSize;

        List<AdoptedDogProfile> listOfDogs = adoptedApiSvc.getDogFromMongo(pageSize, skip);
        // System.out.println(listOfDogs);

        return ResponseEntity.ok().body(listOfDogs);
    }

    @PostMapping(path = "/saveDogs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> postSave(@RequestPart(name = "imageFile", required = false) MultipartFile imageFile,
            @RequestPart(name = "name") String name,
            @RequestPart("gender") String gender,
            @RequestPart(name = "coat", required = false) String coat,
            @RequestPart("age") String age,
            @RequestPart(name = "size", required = false) String size,
            @RequestPart("description") String description,
            @RequestPart(name = "listOfCharacteristics", required = false) String listOfCharacteristics,
            @RequestPart("primaryBreed") String primaryBreed,
            @RequestPart(name = "secondaryBreed", required = false) String secondaryBreed,
            @RequestPart("listOfColors") String listOfColors,
            @RequestPart("isMixed") String isMixed,
            @RequestPart("isGoodWithDogs") String isGoodWithDogs,
            @RequestPart("isGoodWithCats") String isGoodWithCats,
            @RequestPart("isDeclawed") String isDeclawed,
            @RequestPart("isGoodWithChildren") String isGoodWithChildren,
            @RequestPart("isHouseTrained") String isHouseTrained,
            @RequestPart("isSpecialNeeds") String isSpecialNeeds,
            @RequestPart("isVaccinated") String isVaccinated) throws ParseException {

        System.out.println("here successfully");
        List<String> characteristicsList = new ArrayList<>();
        characteristicsList.add(listOfCharacteristics);

        List<String> colorList = new ArrayList<>();
        colorList.add(listOfColors);

        AdoptedDogProfile adoptedDogProfile = new AdoptedDogProfile();
        // String id = UUID.randomUUID().toString().substring(0, 8);
        // int intId = Integer.parseInt(id, 16);
        Random random = new Random();
        int intId = random.nextInt(90000000) + 10000000;
        adoptedDogProfile.setId(intId);
        adoptedDogProfile.setName(name);
        adoptedDogProfile.setGender(gender);
        adoptedDogProfile.setCoat(coat);
        adoptedDogProfile.setAge(age);
        adoptedDogProfile.setSize(size);
        adoptedDogProfile.setDescription(description);
        adoptedDogProfile.setDescription(description);
        adoptedDogProfile.setPrimaryBreed(primaryBreed);
        adoptedDogProfile.setSecondaryBreed(secondaryBreed);
        adoptedDogProfile.setCharacteristics(characteristicsList);
        adoptedDogProfile.setColor(colorList);
        System.out.println(LocalDate.now());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate currentDate = LocalDate.now();
        // Format the current date as a string
        String currentDateAsString = currentDate.format(formatter);
        adoptedDogProfile.setPublished(currentDateAsString);

        Boolean mixed = Boolean.parseBoolean(isMixed);
        Boolean goodWithDogs = Boolean.parseBoolean(isGoodWithDogs);
        Boolean goodWithCats = Boolean.parseBoolean(isGoodWithCats);
        Boolean goodWithChildren = Boolean.parseBoolean(isGoodWithChildren);
        Boolean declawed = Boolean.parseBoolean(isDeclawed);
        Boolean houseTrained = Boolean.parseBoolean(isHouseTrained);
        Boolean specialNeeds = Boolean.parseBoolean(isSpecialNeeds);
        Boolean vaccinated = Boolean.parseBoolean(isVaccinated);
        adoptedDogProfile.setIsMixed(mixed);
        adoptedDogProfile.setIsGoodWithDogs(goodWithDogs);
        adoptedDogProfile.setIsGoodWithCats(goodWithCats);
        adoptedDogProfile.setIsGoodWithChildren(goodWithChildren);
        adoptedDogProfile.setIsDeclawed(declawed);
        adoptedDogProfile.setIsHouseTrained(houseTrained);
        adoptedDogProfile.setIsSpecialNeeds(specialNeeds);
        adoptedDogProfile.setIsVaccinated(vaccinated);

        System.out.println(adoptedDogProfile);

        String urlFromS3 = adoptedApiSvc.saveToS3(imageFile);

        List<String> urlList = new ArrayList<>();
        urlList.add(urlFromS3);

        adoptedDogProfile.setUrl(urlList);

        String resp = adoptedApiSvc.saveToMongoIndivDog(adoptedDogProfile);
        JsonObject jsonObj = Json.createObjectBuilder().add("success", resp).build();
        return ResponseEntity.ok().body(jsonObj.toString());

    }

    @GetMapping(path = "/getCount")
    public ResponseEntity<Long> getCount() {
        Long count = adoptedApiSvc.getCount();

        return ResponseEntity.ok().body(count);
    }

    @GetMapping(path = "/getAllDogsWithoutPagination")
    public ResponseEntity<List<AdoptedDogProfile>> getAllDogsWithoutPagination() {
        List<AdoptedDogProfile> listOfDogs = adoptedApiSvc.getDogFromMongoWithoutPagiantion();
        return ResponseEntity.ok().body(listOfDogs);
    }

    @GetMapping(path = "/getDogs/{id}")
    public ResponseEntity<AdoptedDogProfile> getInidivudalDogs(@PathVariable("id") int id) {
        AdoptedDogProfile dog = adoptedApiSvc.getIndivDog(id);
        System.out.println(dog);
        return ResponseEntity.ok().body(dog);
    }

    @PostMapping(path = "/{id}/remove")
    public ResponseEntity<String> removedAdoptedDog(@PathVariable int id) {
        // JsonObject response = Json.createObjectBuilder()
        // .add("deleted", "thread with ID %s
        // deleted".formatted(forumSvc.deleteThread(threadId)))
        // .build();
        // return ResponseEntity.ok().body(response.toString());
        JsonObject response = Json.createObjectBuilder()
                .add("deleted", "Dog with ID %d removed from database".formatted(adoptedApiSvc.deleteAdoptedDog(id)))
                .build();

        return ResponseEntity.ok().body(response.toString());

    }

    @PostMapping(path = "/{userId}/save")
    public ResponseEntity<String> favoriteDog(@PathVariable("userId") String userId, @RequestBody String jsonString) {

        FavoriteDog favoriteDog = new FavoriteDog();
        JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
        JsonObject jsonObject = jsonReader.readObject();
        // System.out.println("RECEIVED>>>>>" + jsonObject);
        int id = jsonObject.getInt("id");
        String name = jsonObject.getString("petName");
        String gender = jsonObject.getString("gender");
        String primaryBreed = jsonObject.getString("primaryBreed");
        String secondaryBreed = jsonObject.getString("secondaryBreed", "null");
        JsonArray jsonArray = jsonObject.getJsonArray("url");
        String url = jsonArray.getString(0);
        // System.out.println(url);

        favoriteDog.setGender(gender);
        favoriteDog.setName(name);
        favoriteDog.setPetId(id);
        favoriteDog.setPrimaryBreed(primaryBreed);
        favoriteDog.setSecondaryBreed(secondaryBreed);
        favoriteDog.setUrl(url);
        // System.out.println(favoriteDog);

        // save toSQL
        if (adoptedApiSvc.favoriteDog(favoriteDog, userId)) {
            String msg = "Doggo successfully saved! You may go to your Saved Searches to view";
            JsonObject jsonObj = Json.createObjectBuilder().add("success", msg).build();
            return ResponseEntity.ok().body(jsonObj.toString());

        } else {
            String fail = "Unable to save pet at the moment";
            // JsonObject jsonFail = Json.createObjectBuilder().add("failure",
            // fail).build();
            return ResponseEntity.ok().body(fail);
        }

    }

    @GetMapping(path = "/{userId}/favorites")
    public ResponseEntity<List<FavoriteDog>> getFavorites(@PathVariable("userId") String userId) {

        List<FavoriteDog> listOfFavs = adoptedApiSvc.retreiveFavorites(userId);
        return ResponseEntity.ok().body(listOfFavs);

    }

    @PostMapping(path = "/{userId}/delete")
    public ResponseEntity<String> deleteSaved(@RequestBody int petId, @PathVariable String userId) {
        // System.out.println(petId);

        try {
            String resp = adoptedApiSvc.deletedSaved(petId, userId);
            JsonObject jsonObj = Json.createObjectBuilder().add("success", resp).build();
            return ResponseEntity.ok().body(jsonObj.toString());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Failed to accept the delete");

        }

    }

    @PostMapping(path = "/{userId}/sendinquiry")
    public ResponseEntity<String> sendInquiry(@PathVariable("userId") String userId, @RequestBody String jsonResp)
            throws ParseException {

        Inquiry inquiry = new Inquiry();
        JsonReader jsonReader = Json.createReader(new StringReader(jsonResp));
        JsonObject jsonObject = jsonReader.readObject();
        inquiry.setUserId(userId);
        // inquiry.setPetId(petid);
        inquiry.setFirstName(jsonObject.getString("firstName"));
        inquiry.setLastName(jsonObject.getString("lastName"));
        inquiry.setEmail(jsonObject.getString("email"));
        inquiry.setPetId((jsonObject.getInt("petid")));
        inquiry.setSelectedHour(jsonObject.getString("selectedHour"));
        // inquiry.setFirstTimeOwner(jsonObject.getString("firstTimeOwner"));
        inquiry.setFirstTimeOwner(jsonObject.getBoolean("firstTimeOwner"));
        String dob = jsonObject.getString("dateOfBirth");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Date date = formatter.parse(dob);
        inquiry.setBirthdate(date);

        String visitDate = jsonObject.getString("dateOfViewing");

        Date intendedvisitdate = formatter.parse(visitDate);
        inquiry.setIntended_visit_date(intendedvisitdate);

        inquiry.setGender(jsonObject.getString("gender"));
        inquiry.setNationality(jsonObject.getString("nationality"));
        inquiry.setOther(jsonObject.getString("other"));
        inquiry.setDogName(jsonObject.getString("petName"));
        inquiry.setUrl(jsonObject.getString("url"));

        try {
            Boolean result = adoptedApiSvc.makeInquiry(inquiry, userId);

            return ResponseEntity.ok().body(result.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body("error saving inquiry");

        }

    }

    @GetMapping(path = "/getinquiry")
    public ResponseEntity<List<Inquiry>> getAllEnquries() {

        List<Inquiry> listOfInquiry = new ArrayList<>();
        try {
            listOfInquiry = adoptedApiSvc.getAllInquiry();
            System.out.println("managed to retrieve list");
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("date exception");
        }
        return ResponseEntity.ok().body(listOfInquiry);
    }

    @GetMapping(path = "/getconfirmed")
    public ResponseEntity<List<Inquiry>> getConfirmedEnquries() {

        List<Inquiry> listOfInquiry = new ArrayList<>();
        try {
            listOfInquiry = adoptedApiSvc.getConfirmedInquiry();
            System.out.println("managed to retrieve confirmed list");
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("date exception");
        }
        return ResponseEntity.ok().body(listOfInquiry);
    }

    @PostMapping(path = "/{userId}/acceptinquiry")
    public ResponseEntity<String> acceptInquiry(@PathVariable("userId") String userId, @RequestBody String inquiryId) {

        try {
            String resp = adoptedApiSvc.acceptInquiry(inquiryId, userId);

            JsonObject jsonObj = Json.createObjectBuilder().add("success", resp).build();
            return ResponseEntity.ok().body(jsonObj.toString());
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Failed to accept the inquiry");
        }

    }

    @PostMapping(path = "/{userId}/declineinquiry")
    public ResponseEntity<String> declineInquiry(@PathVariable("userId") String userId, @RequestBody String inquiryId) {

        try {
            String resp = adoptedApiSvc.declineInquiry(inquiryId, userId);

            JsonObject jsonObj = Json.createObjectBuilder().add("success", resp).build();
            return ResponseEntity.ok().body(jsonObj.toString());
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Failed to accept the inquiry");
        }

    }

    @GetMapping(path = "/getIndividual/{inquiryId}")
    public ResponseEntity<Inquiry> getIndividualAppointment(@PathVariable("inquiryId") String inquiryId)
            throws ParseException {

        Inquiry inquiry = adoptedApiSvc.getConfirmedAppointment(inquiryId);
        return ResponseEntity.ok().body(inquiry);

    }

}