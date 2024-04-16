package vttp.server.Controller;

import java.io.StringReader;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import vttp.server.Model.User;
import vttp.server.Service.TokenService;
import vttp.server.Service.UserService;

@RestController
@CrossOrigin
@RequestMapping(path = "/auth")
public class UserController {
  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  UserService userService;

  @Autowired
  AuthenticationManager authManager;

  @Autowired
  TokenService tokenSvc;

  @PostMapping(path = "/saveUser")
  public ResponseEntity<String> saveUsers(@RequestBody String jsonObjectString) {

    JsonReader jsonReader = Json.createReader(new StringReader(jsonObjectString));
    JsonObject jsonObject = jsonReader.readObject();
    

    String userId = UUID.randomUUID().toString().substring(0, 8);
    User user = new User();
    user.setUserId(userId);
    user.setEmail(jsonObject.getString("email"));
    user.setFirstName(jsonObject.getString("firstName"));
    user.setLastName(jsonObject.getString("lastName"));
    user.setUserName(jsonObject.getString("username"));
    user.setPassword(passwordEncoder.encode(jsonObject.getString("password")));
    String defaultUrl = "https://cdn-icons-png.flaticon.com/512/4775/4775486.png";
    user.setRole("user");
    user.setUrl(defaultUrl);

   

    try {
      System.out.println("try");
      User savedUser = userService.createUser(user);
      System.out.println(savedUser);
      String msg = user.getEmail() + " successfully created!";
      JsonObject jsonObj = Json.createObjectBuilder().add("success", msg).build();

      return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(jsonObj.toString());

    } catch (Exception e) {
      
      return new ResponseEntity<>(HttpStatusCode.valueOf(500));
    }

  }

  @PostMapping(path = "/login")
  public ResponseEntity<String> loginWithEmail(@RequestBody String resp) {
 

    JsonReader jsonReader = Json.createReader(new StringReader(resp));
    JsonObject jsonObject = jsonReader.readObject();
    
    String email = jsonObject.getString("email");
    String password = jsonObject.getString("password");
    User loginUser = new User();
    loginUser.setEmail(email);
    loginUser.setPassword(password);

    Authentication a = authManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginUser.getEmail(), loginUser.getPassword()));
    // UserPrincipal user = (UserPrincipal) a.getPrincipal();

    String token = tokenSvc.generateToken(a);
    String userId = tokenSvc.getUserIdFromToken(token);
    String userName = userService.getUserName(loginUser);
    User user = userService.getUserByUserId(userId);
    String userUrl = user.getUrl();
    String role = tokenSvc.getRoleFromToken(token);

    // System.out.println("TOKEN>>>>" + token);
    JsonObject jsonObj = Json.createObjectBuilder().add("success", token).add("userid", userId)
        .add("username", userName).add("role", role).add("userurl", userUrl).build();

    return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(jsonObj.toString());

  }

  @GetMapping(path = "/getUser/{userId}")
  @CrossOrigin
  public ResponseEntity<User> getUser(@PathVariable("userId") String userId) {
    // System.out.println(userId);
    User user = userService.getUserByUserId(userId);
    return ResponseEntity.ok().body(user);
  }

  @PostMapping(path = "/{userId}/update")
  public ResponseEntity<String> updateDisplayPicture(
      @RequestPart(name = "imageFile", required = false) MultipartFile imageFile,
      @PathVariable("userId") String userId) {

    User user = new User();
    String urlFromS3 = userService.saveToS3(imageFile);

    user.setUserId(userId);
    user.setUrl(urlFromS3);
    String msg = userService.updateInSql(user);
    JsonObject jsonObj = Json.createObjectBuilder().add("success", msg).build();
    return ResponseEntity.ok().body(jsonObj.toString());

  }

}
