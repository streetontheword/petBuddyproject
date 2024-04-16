package vttp.server.Controller;

import java.io.StringReader;


import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.server.Service.EmailService;

@RestController
@RequestMapping(path = "/api")
@CrossOrigin
public class EmailController {

    @Autowired
    EmailService emailSvc;

    @PostMapping(path = "/sendemail")
    public ResponseEntity<String> sendEmail(@RequestBody String jsonString) {
      
         JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
        JsonObject jsonObject = jsonReader.readObject();
        String email = jsonObject.getString("email");
        String name = jsonObject.getString("name");
        String appointmentDate = jsonObject.getString("appointmentDate");
  
        String msg = "inquiry successfully sent";
      
        try {
            emailSvc.sendEmail(email, name, appointmentDate);
            JsonObject jsonObj = Json.createObjectBuilder().add("success", msg).build();

            return ResponseEntity.ok().body(jsonObj.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Failed to send email");
        }
    }
    
        @PostMapping(path="/confirmation")
        public ResponseEntity<String> sendEmailConfirmation(@RequestBody String jsonResp){
    
            JsonReader jsonReader = Json.createReader(new StringReader(jsonResp));
            JsonObject jsonObject = jsonReader.readObject();
            String email = jsonObject.getString("email");
            String name = jsonObject.getString("name");
            String appointmentDate = jsonObject.getString("appointmentDate");
            String msg = "Confirmation email successfully sent";
            try {
                emailSvc.sendConfirmationEmail(email, name, appointmentDate);
                JsonObject jsonObj = Json.createObjectBuilder().add("success", msg).build();
    
                return ResponseEntity.ok().body(jsonObj.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Failed to send email");
            }

        }

        @PostMapping(path="/decline")
        public ResponseEntity<String> sendEmailDeclination(@RequestBody String jsonResp){

           
            JsonReader jsonReader = Json.createReader(new StringReader(jsonResp));
            JsonObject jsonObject = jsonReader.readObject();
            String email = jsonObject.getString("email");
            String name = jsonObject.getString("name");
            String appointmentDate = jsonObject.getString("appointmentDate");
            String msg = "Declination email successfully sent";
            try {
                emailSvc.sendDeclinationEmail(email, name, appointmentDate);
                JsonObject jsonObj = Json.createObjectBuilder().add("success", msg).build();
    
                return ResponseEntity.ok().body(jsonObj.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Failed to send email");
            }

        }
}
