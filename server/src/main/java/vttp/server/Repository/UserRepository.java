package vttp.server.Repository;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

import vttp.server.Model.User;
import vttp.server.enums.UserRole;

@Repository
public class UserRepository {

    public static final String SQL_INSERT_USER = """
            insert into users(userId, email, first_name, last_name, username, password, role, userUrl) values (?,?,?,?,?,?,?,?)
            """;

    public static final String SQL_RETRIEVE_USER = """
            select * from users where email = ?
                """;

    public static final String SQL_RETRIEVE_USER_FROM_ROLE = """
            select * from users where role = ?
                """;

    public static final String SQL_RETRIEVE_USER_FROM_ID = """
            select * from users where userId = ?
                """;

    private static final String SQL_UPDATE_DISPLAY_PICTURE = """
            update users
            set userUrl = ?
            where userId = ?
                """;

    @Autowired
    private JdbcTemplate template;

    @Autowired
    AmazonS3 s3;

    private UserRole userRole;

    public User createUser(User user) {

        System.out.println("in repo");

        try {
            template.update(SQL_INSERT_USER, user.getUserId(), user.getEmail(), user.getFirstName(), user.getLastName(),
                    user.getUserName(), user.getPassword(), "user", user.getUrl());
            System.out.println("successfull saved");
            User savedUser = new User();
            savedUser.setEmail(user.getEmail());
            savedUser.setUserId(user.getUserId());
            savedUser.setFirstName(user.getFirstName());
            savedUser.setLastName(user.getLastName());
            savedUser.setPassword(user.getPassword());
            savedUser.setRole("user");
            savedUser.setUrl(user.getUrl());
            System.out.println("in repo");

            return savedUser;

        } catch (Exception e) {
            System.err.println("Error saving user: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public User getPasswordByEmail(String email) {
        SqlRowSet rs = template.queryForRowSet(SQL_RETRIEVE_USER, email);
        User user = new User();
        while (rs.next()) {
            user.setPassword(rs.getString("password"));
            user.setEmail(rs.getString("email"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setUserName(rs.getString("username"));
            user.setUserId(rs.getString("userId"));
            switch (rs.getString("role")) {
                case "user":
                    user.setRole("user");
                    break;
                case "admin":
                    user.setRole("admin");
                    break;
            }
            System.out.println("user details from sql" + user);
        }
        return user;
    }

    public User findByRole(UserRole role) {
        SqlRowSet rs = template.queryForRowSet(SQL_RETRIEVE_USER_FROM_ROLE, role);
        User user = new User();
        while (rs.next()) {
            user.setPassword(rs.getString("password"));
            user.setEmail(rs.getString("email"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setUserName(rs.getString("username"));
            user.setUserId(rs.getString("userId"));
            user.setRole("role");
            System.out.println("user details from sql" + user);
        }
        return user;
    }

    public String findById(String userId) {
        SqlRowSet rs = template.queryForRowSet(SQL_RETRIEVE_USER_FROM_ID, userId);
        String email = rs.getString("email");
        return email;
    }

    public User findUserEmail(String userId) {
        SqlRowSet rs = template.queryForRowSet(SQL_RETRIEVE_USER_FROM_ID, userId);
        User user = new User();
        while (rs.next()) {
            user.setEmail(rs.getString("email"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setUserName(rs.getString("username"));
            user.setUserId(rs.getString("userId"));
            user.setUrl(rs.getString("userUrl"));
            System.out.println(user);
        }
        return user;
    }

    public String saveToS3(String imageId, InputStream is, String contentType, long length) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);
        metadata.setContentLength(length);

        String key = "images/%s".formatted(imageId);

        PutObjectRequest putReq = new PutObjectRequest(
                "streetontheword" // bucket name
                , key, // key
                is, metadata);

        putReq = putReq.withCannedAcl(CannedAccessControlList.PublicRead);

        // upload to s3 bucked
        PutObjectResult result = s3.putObject(putReq);
        String url = s3.getUrl("streetontheword", key).toExternalForm();
        // System.out.println("In USER repo>>> URL: " + url);

        return url;

    }

    public String updateInSql(User user) {
        System.out.println(user.getUrl());
        System.out.println(user.getUserId());
        if (template.update(SQL_UPDATE_DISPLAY_PICTURE, user.getUrl(), user.getUserId())>0){
            return "Display picture successfully updated";
        } else{
            return "Unsuccessful Update";
        }
    }
    // public String updatePetAccount(Pet pet, String petId, String userId) {

    //   if (template.update(SQL_UPDATE_PET_INFORMATION, pet.getName(), pet.getDateOfBirth(), pet.getDateOfLastVaccination(), pet.getMicrochipNumber(), pet.getGender(), pet.getComments(), pet.getBreed(),petId, userId) > 0){
    //     return "Pet's information successfully updated"; 
    //   } else {
    //     return "Unsuccessful Update";
    //   }
    
}
