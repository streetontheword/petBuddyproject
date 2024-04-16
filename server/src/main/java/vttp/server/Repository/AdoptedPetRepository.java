package vttp.server.Repository;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;

import vttp.server.Model.AdoptedDogProfile;
import vttp.server.Model.FavoriteDog;
import vttp.server.Model.Inquiry;

@Repository
public class AdoptedPetRepository {

    @Autowired
    MongoTemplate template;

    @Autowired
    AmazonS3 s3;

    @Autowired
    private JdbcTemplate sqlTemplate;

    public void saveToMongo(List<AdoptedDogProfile> listOfDogs) {
        MongoCollection<Document> collection = template.getCollection("dogsForAdoption");
        for (AdoptedDogProfile dog : listOfDogs) {
            int id = dog.getId();
            String name = dog.getName();
            String gender = dog.getGender();
            String age = dog.getAge();
            String size = dog.getSize();
            String coat = dog.getCoat();
            List<String> listOfurl = dog.getUrl();
            String published = dog.getPublished();
            String description = dog.getDescription();
            List<String> listOfCharacteristics = dog.getCharacteristics();
            String primaryBreed = dog.getPrimaryBreed();
            String secondaryBreed = dog.getSecondaryBreed();
            // List<String> listOfBreeds = dog.getBreeds();
            List<String> listOfColors = dog.getColor();
            Boolean isMixed = dog.getIsMixed();
            Boolean isUnknown = dog.getIsUnknown();
            Boolean isGoodWithDogs = dog.getIsGoodWithDogs();
            Boolean isGoodWithChildren = dog.getIsGoodWithChildren();
            Boolean isGoodWithCats = dog.getIsGoodWithCats();
            Boolean isSpayedAndNeutered = dog.getIsSpayedAndNeutered();
            Boolean isHouseTrained = dog.getIsHouseTrained();
            Boolean isDeclawed = dog.getIsDeclawed();
            Boolean isSpecialNeeds = dog.getIsSpecialNeeds();
            Boolean isVaccinated = dog.getIsVaccinated();
            Document doc = new Document().append("id", id).append("name", name).append("gender", gender)
                    .append("age", age)
                    .append("size", size).append("coat", coat).append("published", published)
                    .append("description", description).append("primaryBreed", primaryBreed)
                    .append("secondaryBreed", secondaryBreed)
                    .append("mixed", isMixed)
                    .append("unknown", isUnknown).append("listOfColors", listOfColors)
                    .append("goodWithDogs", isGoodWithDogs)
                    .append("goodWithChildren", isGoodWithChildren).append("goodWithCats", isGoodWithCats)
                    .append("spayedAndNeutered", isSpayedAndNeutered).append("houseTrained", isHouseTrained)
                    .append("isDeclawed", isDeclawed).append("specialNeeds", isSpecialNeeds)
                    .append("vaccinated", isVaccinated);

            if (listOfCharacteristics != null) {
                doc.append("listOfCharacteristics", listOfCharacteristics);
            }
            if (listOfurl != null) {
                doc.append("listOfurl", listOfurl);
            }
            InsertOneResult result = collection.insertOne(doc);
        }
    }

    public String saveToMongoIndivDog(AdoptedDogProfile adoptedDogProfile) {
        MongoCollection<Document> collection = template.getCollection("dogsForAdoption");
        int id = adoptedDogProfile.getId();
        String name = adoptedDogProfile.getName();
        String gender = adoptedDogProfile.getGender();
        String age = adoptedDogProfile.getAge();
        String size = adoptedDogProfile.getSize();
        String coat = adoptedDogProfile.getCoat();
        List<String> listOfurl = adoptedDogProfile.getUrl();
        String published = adoptedDogProfile.getPublished();
        String description = adoptedDogProfile.getDescription();
        List<String> listOfCharacteristics = adoptedDogProfile.getCharacteristics();
        String primaryBreed = adoptedDogProfile.getPrimaryBreed();
        String secondaryBreed = adoptedDogProfile.getSecondaryBreed();
        List<String> listOfColors = adoptedDogProfile.getColor();
        Boolean isMixed = adoptedDogProfile.getIsMixed();
        Boolean isUnknown = adoptedDogProfile.getIsUnknown();
        Boolean isGoodWithDogs = adoptedDogProfile.getIsGoodWithDogs();
        Boolean isGoodWithChildren = adoptedDogProfile.getIsGoodWithChildren();
        Boolean isGoodWithCats = adoptedDogProfile.getIsGoodWithCats();
        Boolean isSpayedAndNeutered = adoptedDogProfile.getIsSpayedAndNeutered();
        Boolean isHouseTrained = adoptedDogProfile.getIsHouseTrained();
        Boolean isDeclawed = adoptedDogProfile.getIsDeclawed();
        Boolean isSpecialNeeds = adoptedDogProfile.getIsSpecialNeeds();
        Boolean isVaccinated = adoptedDogProfile.getIsVaccinated();
        Document doc = new Document().append("id", id).append("name", name).append("gender", gender)
                .append("age", age)
                .append("size", size).append("coat", coat).append("published", published)
                .append("description", description).append("primaryBreed", primaryBreed)
                .append("secondaryBreed", secondaryBreed)
                .append("mixed", isMixed)
                .append("unknown", isUnknown).append("listOfColors", listOfColors)
                .append("goodWithDogs", isGoodWithDogs)
                .append("goodWithChildren", isGoodWithChildren).append("goodWithCats", isGoodWithCats)
                .append("spayedAndNeutered", isSpayedAndNeutered).append("houseTrained", isHouseTrained)
                .append("isDeclawed", isDeclawed).append("specialNeeds", isSpecialNeeds)
                .append("vaccinated", isVaccinated);

        if (listOfCharacteristics != null) {
            doc.append("listOfCharacteristics", listOfCharacteristics);
        }
        if (listOfurl != null) {
            doc.append("listOfurl", listOfurl);
        }
        InsertOneResult result = collection.insertOne(doc);
        String msg = "successfully saved Dog";
        return msg;
    }

    public String saveToS3(String imageId, InputStream is, String contentType, long length) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);
        metadata.setContentLength(length);

        String key = "images/%s".formatted(imageId);

        // System.out.println("ID from REPO>>>: " + imageId);

        PutObjectRequest putReq = new PutObjectRequest(
                "streetontheword" // bucket name
                , key, // key
                is, metadata);

        putReq = putReq.withCannedAcl(CannedAccessControlList.PublicRead);

        // upload to s3 bucked
        PutObjectResult result = s3.putObject(putReq);
        String url = s3.getUrl("streetontheword", key).toExternalForm();
        // System.out.println("In repo>>> URL: " + url);

        return url;

    }

    public List<AdoptedDogProfile> getDogsFromMongo(int limit, int skip) {
    

        List<AdoptedDogProfile> listOfAdoptedDogs = new ArrayList<>();
        Query query = new Query().limit(limit).skip(skip);
       
        List<Document> results = template.find(query, Document.class, "dogsForAdoption");

        for (Document d : results) {
            AdoptedDogProfile dog = new AdoptedDogProfile();
            dog.setName(d.getString("name"));
            dog.setAge(d.getString("age"));
            // List<String> listOfBreeds = d.getList("listOfBreeds", String.class);
            List<String> listOfPics = d.getList("listOfurl", String.class);
            List<String> listOfColors = d.getList("listOfColors", String.class);
            List<String> listOfCharacteristics = d.getList("listOfCharacteristics", String.class);

            // System.out.println(listOfPics);
            if (listOfPics != null) {
                dog.setUrl(listOfPics);
            } else {
                List<String> listOfstock = new ArrayList<>();
                listOfstock.add(
                        "https://media.istockphoto.com/id/1022675764/vector/man-and-dog-walking-back-view-silhouette.jpg?s=612x612&w=0&k=20&c=niPJZXzI2jA5NghQI3ncXTBASaAM1XbuZPZ9LaPZp_M=");
                dog.setUrl(listOfstock);
            }

            dog.setId(d.getInteger("id"));
            dog.setPrimaryBreed(d.getString("primaryBreed"));
            dog.setSecondaryBreed(d.getString("secondaryBreed"));
            dog.setCharacteristics(listOfCharacteristics);
            dog.setColor(listOfColors);
            dog.setSize(d.getString("size"));
            dog.setGender(d.getString("gender"));
            listOfAdoptedDogs.add(dog);
            // System.out.println(listOfAdoptedDogs);

        }
        return listOfAdoptedDogs;
    }

    public List<AdoptedDogProfile> getDogsFromMongoWithoutPagination() {

        List<AdoptedDogProfile> listOfAdoptedDogs = new ArrayList<>();
        List<Document> results = template.findAll(Document.class, "dogsForAdoption");

        for (Document d : results) {
            AdoptedDogProfile dog = new AdoptedDogProfile();
            dog.setName(d.getString("name"));
            dog.setAge(d.getString("age"));
            // List<String> listOfBreeds = d.getList("listOfBreeds", String.class);
            List<String> listOfPics = d.getList("listOfurl", String.class);
            List<String> listOfColors = d.getList("listOfColors", String.class);
            List<String> listOfCharacteristics = d.getList("listOfCharacteristics", String.class);

            // System.out.println(listOfPics);
            if (listOfPics != null) {
                dog.setUrl(listOfPics);
            } else {
                List<String> listOfstock = new ArrayList<>();
                listOfstock.add(
                        "https://media.istockphoto.com/id/1022675764/vector/man-and-dog-walking-back-view-silhouette.jpg?s=612x612&w=0&k=20&c=niPJZXzI2jA5NghQI3ncXTBASaAM1XbuZPZ9LaPZp_M=");
                dog.setUrl(listOfstock);
            }

            dog.setId(d.getInteger("id"));
            dog.setPrimaryBreed(d.getString("primaryBreed"));
            dog.setSecondaryBreed(d.getString("secondaryBreed"));
            dog.setCharacteristics(listOfCharacteristics);
            dog.setColor(listOfColors);
            dog.setSize(d.getString("size"));
            dog.setGender(d.getString("gender"));
            listOfAdoptedDogs.add(dog);
            // System.out.println(listOfAdoptedDogs);

        }
        return listOfAdoptedDogs;
    }

    public Long getCount() {
        Query query = new Query();
        Long count = template.count(query, "dogsForAdoption");
        return count;
    }

    // db.dogsForAdoption.find({
    // id: 71109875
    // })
    public AdoptedDogProfile findOneAdoptedDog(int id) {
        Query query = Query.query(Criteria.where("id").is(id));
        List<Document> results = template.find(query, Document.class, "dogsForAdoption");
        AdoptedDogProfile dog = new AdoptedDogProfile();
        for (Document d : results) {
            dog.setName(d.getString("name"));
            dog.setAge(d.getString("age"));
            List<String> listOfBreeds = d.getList("listOfBreeds", String.class);
            List<String> listOfPics = d.getList("listOfurl", String.class);
            List<String> listOfColors = d.getList("listOfColors", String.class);
            List<String> listOfCharacteristics = d.getList("listOfCharacteristics", String.class);
            dog.setId(d.getInteger("id"));
            // dog.setBreeds(listOfBreeds);
            dog.setSize(d.getString("size"));
            dog.setPrimaryBreed(d.getString("primaryBreed"));
            dog.setSecondaryBreed(d.getString("secondaryBreed"));
            dog.setCharacteristics(listOfCharacteristics);
            dog.setColor(listOfColors);
            dog.setCoat(d.getString("coat"));
            dog.setDescription(d.getString("description"));
            dog.setGender(d.getString("gender"));
            dog.setPublished(d.getString("published"));
            // environment variable
            dog.setIsHouseTrained(d.getBoolean("houseTrained"));
            dog.setIsSpayedAndNeutered(d.getBoolean("spayedAndNeutered"));
            dog.setIsSpecialNeeds(d.getBoolean("specialNeeds"));
            dog.setIsDeclawed(d.getBoolean("isDeclawed"));
            dog.setIsVaccinated(d.getBoolean("vaccinated"));
            // temperament
            dog.setIsGoodWithDogs(d.getBoolean("goodWithDogs"));
            dog.setIsGoodWithChildren(d.getBoolean("goodWithChildren"));
            dog.setIsGoodWithCats(d.getBoolean("goodWithCats"));
            if (listOfPics != null) {
                dog.setUrl(listOfPics);
            } else {
                List<String> listOfstock = new ArrayList<>();
                listOfstock.add(
                        "https://media.istockphoto.com/id/1022675764/vector/man-and-dog-walking-back-view-silhouette.jpg?s=612x612&w=0&k=20&c=niPJZXzI2jA5NghQI3ncXTBASaAM1XbuZPZ9LaPZp_M=");
                dog.setUrl(listOfstock);
            }

        }
        return dog;

    }

    // sql for favoriting
    private static final String SQL_FAVORITE_PET = """
            insert into savedInterestedPets(petId, name, primaryBreed, secondaryBreed, gender, url, userId) values (?,?,?,?,?,?,?)
            """;

    public Boolean favoriteDog(FavoriteDog favDog, String userId) {
        return sqlTemplate.update(SQL_FAVORITE_PET, favDog.getPetId(), favDog.getName(), favDog.getPrimaryBreed(),
                favDog.getSecondaryBreed(), favDog.getGender(), favDog.getUrl(), userId) > 0;
    }

    private static final String SQL_RETRIEVE_FAVPETS = """
            select * from savedInterestedPets where userId = ?
            """;

    public List<FavoriteDog> getlistOfFavoritedDogs(String userId) {
        SqlRowSet rs = sqlTemplate.queryForRowSet(SQL_RETRIEVE_FAVPETS, userId);
        List<FavoriteDog> listOfFavoriteDogs = new ArrayList<>();
        while (rs.next()) {
            FavoriteDog favoriteDog = new FavoriteDog();
            int petId = rs.getInt("petId");
            String petName = rs.getString("name");
            String primaryBreed = rs.getString("primaryBreed");
            String secondaryBreed = rs.getString("secondaryBreed");
            String gender = rs.getString("gender");
            String url = rs.getString("url");
            favoriteDog.setPetId(petId);
            favoriteDog.setName(petName);
            favoriteDog.setPrimaryBreed(primaryBreed);
            favoriteDog.setUrl(url);

            if (secondaryBreed != null) {
                favoriteDog.setSecondaryBreed(secondaryBreed);
            }
            favoriteDog.setGender(gender);
            listOfFavoriteDogs.add(favoriteDog);
        }
        return listOfFavoriteDogs;
    }

    private static final String SQL_DELETE_SAVED = """
            delete from savedInterestedPets where petId = ? and userId = ?
                """;

    public String deleteSaved(int petId, String userId) {
        if (sqlTemplate.update(SQL_DELETE_SAVED, petId, userId) > 0) {
            return "successfuly deleted saved pet";
        }
        return "Unsuccessful deletion of pet";
    }

    private static final String SQL_INQUIRY = """
            insert into inquiry (inquiryId, first_name, last_name, email, birthdate, gender, intended_visit_date, nationality, other, petid, dogName, url, firstTimeOwner, selectedHour, userId) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
            """;

    public Boolean makeInquiry(Inquiry inquiry, String userid) {
        return sqlTemplate.update(SQL_INQUIRY, inquiry.getInquiryId(), inquiry.getFirstName(), inquiry.getLastName(),
                inquiry.getEmail(), inquiry.getBirthdate(),
                inquiry.getGender(), inquiry.getIntended_visit_date(), inquiry.getNationality(), inquiry.getOther(),
                inquiry.getPetId(), inquiry.getDogName(), inquiry.getUrl(), inquiry.getFirstTimeOwner(),
                inquiry.getSelectedHour(), userid) > 0;
    }

    private static final String SQL_GET_PENDING_INQUIRY = """
            select * from inquiry where confirmed = false and declined = false
            ORDER BY intended_visit_date ASC
            """;

    public List<Inquiry> getInquiry() throws ParseException {
        SqlRowSet rs = sqlTemplate.queryForRowSet(SQL_GET_PENDING_INQUIRY);
        List<Inquiry> listOfInquiry = new ArrayList<>();

        while (rs.next()) {

            Inquiry inquiry = new Inquiry();

            String intendedvisitdate = rs.getString("intended_visit_date");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(intendedvisitdate);
            String dob = rs.getString("birthdate");
            Date dobdate = formatter.parse(dob);
            inquiry.setIntended_visit_date(date);
            inquiry.setBirthdate(dobdate);
            inquiry.setInquiryId(rs.getString("inquiryId"));
            inquiry.setUserId(rs.getString("userId"));
            inquiry.setFirstName(rs.getString("first_name"));
            inquiry.setLastName(rs.getString("last_name"));
            inquiry.setEmail(rs.getString("email"));
            inquiry.setGender(rs.getString("gender"));
            inquiry.setNationality(rs.getString("nationality"));
            inquiry.setOther(rs.getString("other"));
            inquiry.setFirstTimeOwner(rs.getBoolean("firstTimeOwner"));
            inquiry.setPetId(rs.getInt("petid"));
            inquiry.setDogName(rs.getString("dogName"));
            inquiry.setUrl(rs.getString("url"));
            inquiry.setSelectedHour(rs.getString("selectedHour"));

            listOfInquiry.add(inquiry);
        }

        return listOfInquiry;
    }

    private static final String SQL_GET_CONFIRMED_INQUIRY = """
            select * from inquiry where confirmed = true and declined = false
            ORDER BY intended_visit_date ASC
            """;

    public List<Inquiry> getConfirmedInquiry() throws ParseException {
        SqlRowSet rs = sqlTemplate.queryForRowSet(SQL_GET_CONFIRMED_INQUIRY);
        List<Inquiry> listOfConfirmedInquiry = new ArrayList<>();

        while (rs.next()) {

            Inquiry inquiry = new Inquiry();

            String intendedvisitdate = rs.getString("intended_visit_date");

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(intendedvisitdate);
       
            String dob = rs.getString("birthdate");
            Date dobdate = formatter.parse(dob);
            inquiry.setIntended_visit_date(date);
            inquiry.setBirthdate(dobdate);
            inquiry.setInquiryId(rs.getString("inquiryId"));
            inquiry.setUserId(rs.getString("userId"));
            inquiry.setFirstName(rs.getString("first_name"));
            inquiry.setLastName(rs.getString("last_name"));
            inquiry.setEmail(rs.getString("email"));
            inquiry.setGender(rs.getString("gender"));
            inquiry.setNationality(rs.getString("nationality"));
            inquiry.setOther(rs.getString("other"));
            inquiry.setFirstTimeOwner(rs.getBoolean("firstTimeOwner"));
            inquiry.setPetId(rs.getInt("petid"));
            inquiry.setDogName(rs.getString("dogName"));
            inquiry.setUrl(rs.getString("url"));
            inquiry.setSelectedHour(rs.getString("selectedHour"));
            listOfConfirmedInquiry.add(inquiry);
        }

        return listOfConfirmedInquiry;
    }

    private static final String SQL_ACCEPT_INQUIRY = """
            update inquiry
            set confirmed = true
            where inquiryId = ? and userId = ?
             """;

    public String acceptInquiry(String inquiryId, String userId) {

        if (sqlTemplate.update(SQL_ACCEPT_INQUIRY, inquiryId, userId) > 0) {
            return "Confirmed column updated successfully";
        }
        return "No rows updated for " + userId;
    }

    private static final String SQL_DECLINE_INQUIRY = """
            update inquiry
            set declined = true
            where inquiryId = ? and userId = ?
             """;

    public String declineInquiry(String inquiryId, String userId) {

        if (sqlTemplate.update(SQL_DECLINE_INQUIRY, inquiryId, userId) > 0) {
            return "Declined column updated successfully";
        }
        return "No rows updated for " + userId;
    }

    public DeleteResult deleteAdoptedDog(int petId) {
        Query query = Query.query(Criteria.where("id").is(petId));
        DeleteResult result = template.remove(query, Document.class, "dogsForAdoption");
        // System.out.println("Delete result" + result);
        return result;
    }

    private static final String SQL_GET_CONFIRMED_APPOINTMENT = """
            select * from inquiry where inquiryId = ? and confirmed = true
            """;

    public Inquiry getConfirmedAppointment(String inquiryId) throws ParseException {

        SqlRowSet rs = sqlTemplate.queryForRowSet(SQL_GET_CONFIRMED_APPOINTMENT, inquiryId);
        Inquiry inquiry = new Inquiry();
        while (rs.next()) {
            String intendedvisitdate = rs.getString("intended_visit_date");

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(intendedvisitdate);
            // System.out.println(date);
            String dob = rs.getString("birthdate");
            Date dobdate = formatter.parse(dob);
            inquiry.setIntended_visit_date(date);
            inquiry.setBirthdate(dobdate);
            inquiry.setInquiryId(rs.getString("inquiryId"));
            inquiry.setUserId(rs.getString("userId"));
            inquiry.setFirstName(rs.getString("first_name"));
            inquiry.setLastName(rs.getString("last_name"));
            inquiry.setEmail(rs.getString("email"));
            inquiry.setGender(rs.getString("gender"));
            inquiry.setNationality(rs.getString("nationality"));
            inquiry.setOther(rs.getString("other"));
            inquiry.setFirstTimeOwner(rs.getBoolean("firstTimeOwner"));
            inquiry.setPetId(rs.getInt("petid"));
            inquiry.setDogName(rs.getString("dogName"));
            inquiry.setUrl(rs.getString("url"));
            inquiry.setSelectedHour(rs.getString("selectedHour"));
            // System.out.println(inquiry);

        }
        return inquiry;
    }

}
