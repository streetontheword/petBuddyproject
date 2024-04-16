package vttp.server.Service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import vttp.server.Model.User;
import vttp.server.Repository.UserRepository;

@Service
public class UserService {
    

    @Autowired
    UserRepository userRepo; 


    public User createUser(User user){
        System.out.println("in svc");
       return userRepo.createUser(user);
    }

    public User getUserByUserId(String userId){
        return userRepo.findUserEmail(userId);
    }

    public String getUserIdByEmail(User user){

        String email = user.getEmail();
        User userFromSql = userRepo.getPasswordByEmail(email);
        String userId = userFromSql.getUserId();
        return userId; 
    }

    public String getUserName (User user){
        String email = user.getEmail();
        User userFromSql = userRepo.getPasswordByEmail(email);
        String userName = userFromSql.getUserName();
        return userName;
    }


    

     public String saveToS3(MultipartFile imageFile) {

        // String id = news.getId();
        String id = UUID.randomUUID().toString().substring(0, 8);

        String url;
        // User user = new User(); 

        try {
            url = userRepo.saveToS3(id, imageFile.getInputStream(), imageFile.getContentType(), imageFile.getSize());
          
            // user.setUrl(url);

            return url;

        } catch (IOException e) {

            e.printStackTrace();
        }
        return id;
    }

    public String updateInSql(User user){
        return userRepo.updateInSql(user);
    }


    
}
