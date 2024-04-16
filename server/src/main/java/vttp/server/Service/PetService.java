package vttp.server.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import vttp.server.Model.Pet;
import vttp.server.Repository.PetRepository;

@Service
public class PetService {

    @Autowired
    PetRepository petRepo;

    public String saveToS3(MultipartFile imageFile) {

        // String id = news.getId();
        String id = UUID.randomUUID().toString().substring(0, 8);

        String url;
        Pet pet = new Pet();

        try {
            url = petRepo.saveToS3(id, imageFile.getInputStream(), imageFile.getContentType(), imageFile.getSize());
            System.out.println("In Svc>>>>> url retrieved" + url);
            pet.setImageUrl(url);
            return url;

        } catch (IOException e) {

            e.printStackTrace();
        }
        return id;
    }

    public String saveToSql(Pet pet) {

        if (petRepo.createPetAccount(pet)){
            return "My individual pet successfully saved!";
        } return "Unable to save information at this point of time";
    }

    public String updateFormFieldInSql(Pet pet, String petId, String userId) {
        return petRepo.updatePetAccount(pet, petId, userId);
    }


    public String updatePictureInS3(MultipartFile imageFile) {

        String id = UUID.randomUUID().toString().substring(0, 8);
        String url;
        Pet pet = new Pet();
        try {
            url = petRepo.updatePictureInS3(id, imageFile.getInputStream(), imageFile.getContentType(), imageFile.getSize());
            System.out.println("In Svc>>>>> url retrieved" + url);
            pet.setImageUrl(url);
            return url;
 
        } catch (IOException e) {

            e.printStackTrace();
        }
        return id;
    }

    public String updatePictureInSql(Pet pet){
       return petRepo.updatePictureInSQL(pet);
    }
    public List<Pet> retreievePet(String userId) {
        List<Pet> listOfPets = new ArrayList<>();
        listOfPets = petRepo.getPetFromUserId(userId);

        return listOfPets;

    }

    public Pet retrieveIndividualPet(int petId) {
        return petRepo.getIndividualPet(petId);
    }

    public String deleteIndividual(String userId, int petId) {
        return petRepo.deleteIndividualPet(userId, petId);
    }

}
