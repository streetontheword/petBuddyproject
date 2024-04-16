package vttp.server.Controller;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.server.Model.Pet;
import vttp.server.Service.PetService;

@RestController
@RequestMapping(path = "/api")
@CrossOrigin
public class PetController {

    @Autowired
    PetService petSvc;

    @PostMapping(path = "/{userId}/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> postSave(@RequestPart(name = "imageFile", required = false) MultipartFile imageFile,
            @RequestPart("name") String name,
            @RequestPart("dateOfBirth") String dateOfBirth,
            @RequestPart("dateOfVaccination") String dateOfLastVaccination,
            @RequestPart("gender") String gender, @RequestPart("microChipNumber") String microchipNumber,
            @RequestPart("comments") String comments,
            @RequestPart("breed") String breed, @PathVariable("userId") String userId) throws ParseException {

        Pet pet = new Pet();
        pet.setName(name);
      
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Date dob = formatter.parse(dateOfBirth);
        Date dateOfVac = formatter.parse(dateOfLastVaccination);
        pet.setDateOfBirth(dob);
        pet.setDateOfLastVaccination(dateOfVac);

        pet.setMicrochipNumber(microchipNumber);
        pet.setGender(gender);
        pet.setComments(comments);
        pet.setBreed(breed);
        pet.setUserId(userId);

        String urlFromS3 = petSvc.saveToS3(imageFile);
        pet.setImageUrl(urlFromS3);

        String resp = petSvc.saveToSql(pet);

        JsonObject jsonObj = Json.createObjectBuilder().add("success", resp).build();
        return ResponseEntity.ok().body(jsonObj.toString());

    }

    @PostMapping(path = "/{userId}/updateDetails")
    public ResponseEntity<String> updateFormField(@RequestBody String resp) throws ParseException {

       

        JsonReader jsonReader = Json.createReader(new StringReader(resp));
        JsonObject jsonObject = jsonReader.readObject();
        String petId = jsonObject.getString("petId");
        String userId = jsonObject.getString("userId");
        String name = jsonObject.getString("name");
        String dateOfBirth = jsonObject.getString("dateOfBirth");

        // SimpleDateFormat formatter = new SimpleDateFormat("MM-DD-YYYY");
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = inputFormat.parse(dateOfBirth);
      
        String dateOfLastVaccination = jsonObject.getString("dateOfVaccination");
        Date dateOfVac = inputFormat.parse(dateOfLastVaccination);
        String gender = jsonObject.getString("gender");
        String microChipNumber = jsonObject.getString("microchipNumber");
        String comments = jsonObject.getString("comments");
        String breed = jsonObject.getString("breed");
        Pet pet = new Pet();
        pet.setName(name);
        pet.setDateOfBirth(date);
        pet.setDateOfLastVaccination(dateOfVac);
        pet.setGender(gender);
        pet.setMicrochipNumber(microChipNumber);
        pet.setComments(comments);
        pet.setBreed(breed);

        String response = petSvc.updateFormFieldInSql(pet, petId, userId);

        JsonObject jsonObj = Json.createObjectBuilder().add("success", response).build();
        return ResponseEntity.ok().body(jsonObj.toString());
    }

    @PostMapping(path = "/{userId}/{petId}/updatePicture")
    public ResponseEntity<String> updateDisplayPicture(
            @RequestPart(name = "imageFile", required = false) MultipartFile imageFile,
            @PathVariable("userId") String userId, @PathVariable int petId) {

        Pet pet = new Pet();

        String urlFromS3 = petSvc.updatePictureInS3(imageFile);
        pet.setUserId(userId);
        pet.setImageUrl(urlFromS3);
        pet.setPetId(petId);
        String msg = petSvc.updatePictureInSql(pet);

        JsonObject jsonObj = Json.createObjectBuilder().add("success", msg).build();
        return ResponseEntity.ok().body(jsonObj.toString());

    }

    @GetMapping(path = "/{userId}/pets")
    public ResponseEntity<List<Pet>> getPets(@PathVariable("userId") String userId) {

        List<Pet> listOfPets = petSvc.retreievePet(userId);
       

        return ResponseEntity.ok().body(listOfPets);

    }

    @GetMapping(path = "/{petId}")

    public ResponseEntity<Pet> getIndividualPet(@PathVariable("petId") int petId) {

        Pet pet = petSvc.retrieveIndividualPet(petId);

        return ResponseEntity.ok().body(pet);
    }

    @PostMapping(path = "/{petId}/deletePet")
    @CrossOrigin
    public ResponseEntity<String> deleteIndividualPet(@RequestBody String userId, @PathVariable int petId) {
      
        try {
            String resp = petSvc.deleteIndividual(userId, petId);
            JsonObject jsonObj = Json.createObjectBuilder().add("success", resp).build();
            return ResponseEntity.ok().body(jsonObj.toString());
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Failed to accept the inquiry");

        }

    }
}
