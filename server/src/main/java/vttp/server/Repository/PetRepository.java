package vttp.server.Repository;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

import vttp.server.Model.Pet;
import vttp.server.enums.UserRole;

@Repository
public class PetRepository {

    @Autowired
    private JdbcTemplate template;

    @Autowired
    AmazonS3 s3;

    private static final String SQL_INSERT_PET = """
            insert into petInformation(userId, name, dateOfBirth, dateOfLastVaccination, microchipNumber, gender, comments, breed, picture) values (?,?,?,?,?,?,?,?,?)
                """;

    private static final String SQL_RETRIEVE_PETS = """
            select * from petInformation where userId = ?
                """;

    private static final String SQL_RETRIEVE_PET = """
            select * from petInformation where petId = ?
            """;
    // private static final String SQL_DELETE_PET = """
    //         delete from petInformation where petId = ?
    //         """;

    public boolean createPetAccount(Pet pet) {
        return template.update(SQL_INSERT_PET, pet.getUserId(), pet.getName(), pet.getDateOfBirth(),
                pet.getDateOfLastVaccination(),
                pet.getMicrochipNumber(), pet.getGender(), pet.getComments(), pet.getBreed(), pet.getImageUrl())>0;

    }

    public String saveToS3(String imageId, InputStream is, String contentType, long length) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);
        metadata.setContentLength(length);

        String key = "images/%s".formatted(imageId);

        System.out.println("ID from REPO>>>: " + imageId);

        PutObjectRequest putReq = new PutObjectRequest(
                "streetontheword" // bucket name
                , key, // key
                is, metadata);

        putReq = putReq.withCannedAcl(CannedAccessControlList.PublicRead);

        // upload to s3 bucked
        PutObjectResult result = s3.putObject(putReq);
        String url = s3.getUrl("streetontheword", key).toExternalForm();
        System.out.println("In repo>>> URL: " + url);

        return url;

    }

    private static final String SQL_UPDATE_PET_INFORMATION = """
            update petInformation
            set name = ?,
            dateOfBirth = ?,
            dateOfLastVaccination = ?,
            microchipNumber = ?,
            gender = ?,
            comments = ?,
            breed = ?

            where petId = ? and userId = ?
                """;

    public String updatePetAccount(Pet pet, String petId, String userId) {

        if (template.update(SQL_UPDATE_PET_INFORMATION, pet.getName(), pet.getDateOfBirth(),
                pet.getDateOfLastVaccination(), pet.getMicrochipNumber(), pet.getGender(), pet.getComments(),
                pet.getBreed(), petId, userId) > 0) {
            return "Pet's information successfully updated";
        } else {
            return "Unsuccessful Update";
        }
    }

    public String updatePictureInS3(String imageId, InputStream is, String contentType, long length) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);
        metadata.setContentLength(length);

        String key = "images/%s".formatted(imageId);

        System.out.println("ID from REPO>>>: " + imageId);

        PutObjectRequest putReq = new PutObjectRequest(
                "streetontheword" // bucket name
                , key, // key
                is, metadata);

        putReq = putReq.withCannedAcl(CannedAccessControlList.PublicRead);

        // upload to s3 bucked
        PutObjectResult result = s3.putObject(putReq);
        String url = s3.getUrl("streetontheword", key).toExternalForm();
        System.out.println("In repo>>> URL: " + url);

        return url;

    }

    private static final String SQL_UPDATE_PET_DISPLAY_PICTURE = """
            update petInformation
            set picture = ?
            where petId = ? and userId = ?

                """;

    public String updatePictureInSQL(Pet pet) {

        if (template.update(SQL_UPDATE_PET_DISPLAY_PICTURE, pet.getImageUrl(), pet.getPetId(), pet.getUserId()) > 0) {
            return "Pet's Picture successfully updated";
        } else {
            return "Unsuccessful Update";
        }

    }

    public List<Pet> getPetFromUserId(String userId) {
        SqlRowSet rs = template.queryForRowSet(SQL_RETRIEVE_PETS, userId);

        List<Pet> listOfPets = new ArrayList<>();
        while (rs.next()) {
            Pet pet = new Pet();
            pet.setName(rs.getString("name"));
            pet.setDateOfBirth(rs.getDate("dateOfBirth"));
            pet.setDateOfLastVaccination(rs.getDate("dateOfLastVaccination"));
            pet.setMicrochipNumber(rs.getString("microchipNumber"));
            pet.setGender(rs.getString("gender"));
            pet.setBreed(rs.getString("breed"));
            pet.setComments(rs.getString("comments"));
            pet.setImageUrl(rs.getString("picture"));
            pet.setUserId(rs.getString("userId"));
            pet.setPetId(rs.getInt("petId"));
            listOfPets.add(pet);
            System.out.println("in repo>>>>" + listOfPets);
            System.out.println("Pet>>>>>>>" + pet);
        }

        return listOfPets;

    }

    public Pet getIndividualPet(int petId) {
        SqlRowSet rs = template.queryForRowSet(SQL_RETRIEVE_PET, petId);
        Pet pet = new Pet();
        while (rs.next()) {

            pet.setName(rs.getString("name"));
            pet.setDateOfBirth(rs.getDate("dateOfBirth"));
            pet.setDateOfLastVaccination(rs.getDate("dateOfLastVaccination"));
            pet.setMicrochipNumber(rs.getString("microchipNumber"));
            pet.setGender(rs.getString("gender"));
            pet.setBreed(rs.getString("breed"));
            pet.setComments(rs.getString("comments"));
            pet.setImageUrl(rs.getString("picture"));
            pet.setUserId(rs.getString("userId"));
            pet.setPetId(rs.getInt("petId"));
        }
        return pet;
    }

   

    private static final String SQL_DELETE_INDIVIDUAL_PET = """
        delete from petInformation where userId = ? and petId = ?
            """;

    public String deleteIndividualPet(String userId, int petId){
        if (template.update(SQL_DELETE_INDIVIDUAL_PET, userId, petId) > 0) {
            return "successfuly deleted pet";
        }
        return "Unsuccessful deletion of pet";
    }
}
