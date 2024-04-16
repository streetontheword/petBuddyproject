package vttp.server.Controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vttp.server.Service.NotificationService;

@RestController
@RequestMapping(path = "/notification")
@CrossOrigin
public class NotificationController {


    @Autowired
    private NotificationService notificationSvc;


    @GetMapping(path="/get")
    public ResponseEntity<String> getNotifications(@RequestParam String username) {
        String body = notificationSvc.getNotifications(username);
        // System.out.println("get controllet  "+body);
        ResponseEntity<String> response = ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON)
                .body(body);
        return response;
    }

    @PostMapping(path="/add")
    public ResponseEntity<String> addNotifications(@RequestBody String requestBody){
        // System.out.println("in notifications controller" + requestBody);
        notificationSvc.addNewNotifications(requestBody);
        ResponseEntity<String> response = ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON)
                                        .body("{}");
        return response;

    }

    @GetMapping(path = "/unreadnumber")
    public ResponseEntity<String> getNumberOfUnreadNotifs(@RequestParam String username){
        String body = notificationSvc.getNumbeOfUnreadNotifications(username);
        ResponseEntity<String> response = ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON)
        .body(body);
       
        return response;
    }

    @GetMapping(path = "/read")
    public ResponseEntity<String> readNotification(@RequestParam String notifId ) throws IOException{
        System.out.println(notifId);
        // int id = Integer.parseInt(idString);
        notificationSvc.readNotification(notifId);
        ResponseEntity<String> response = ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON)
        .body(("{}"));
        return response;
    }
    

}
