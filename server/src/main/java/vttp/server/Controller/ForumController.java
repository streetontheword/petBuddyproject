package vttp.server.Controller;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp.server.Configuration.ThreadReplyWebSocketHandler;
import vttp.server.Model.Comment;
import vttp.server.Model.Threads;
import vttp.server.Service.ForumService;

@RestController
@RequestMapping(path = "/api")
@CrossOrigin
public class ForumController {

    @Autowired
    ForumService forumSvc;
   
    @Autowired
    ThreadReplyWebSocketHandler webSocketHandler; 

    @PostMapping(path = "/forum/postNew")
    public ResponseEntity<String> postNewThread(@RequestBody Threads thread) {
 
        String threadId = forumSvc.postNewThread(thread);
      
        JsonObject response = Json.createObjectBuilder()
                .add("success", "New thread created with ID %s".formatted(threadId))
                .build();
        return ResponseEntity.ok(response.toString());

    }

    @GetMapping(path = "/forum/threads")
    public ResponseEntity<List<Document>> getThreads() {
        List<Document> listOfDocs = forumSvc.getThreads();
     
        return ResponseEntity.ok(listOfDocs);
    }

    @GetMapping(path = "/forum/thread")
    public ResponseEntity<Document> getThreadById(@RequestParam String id) {
        return ResponseEntity.ok(forumSvc.getThreadById(id));
    }

    @PostMapping(path="/forum/thread/{threadId}")
    public ResponseEntity<String> postNewComment(@PathVariable String threadId, @RequestBody Comment comment){
       
        
        String commentUsername = forumSvc.postNewComment(comment, threadId);
   

        JsonObject response = Json.createObjectBuilder()
        .add("success", "New comment posted by %s".formatted(commentUsername))
        .build();


        String notificationMessage = "New Comment posted in thread " + threadId; 

        //needs to be username 
         webSocketHandler.notifyUsersAboutNewComment(commentUsername, notificationMessage);
      

        
        return ResponseEntity.ok().body(response.toString());
    }

    @DeleteMapping (path="/forum/thread/{threadId}/delete")
    public ResponseEntity<String> deleteThread(@PathVariable String threadId){

       
        JsonObject response = Json.createObjectBuilder()
        .add("deleted", "thread with ID %s deleted".formatted(forumSvc.deleteThread(threadId)))
        .build();
        return ResponseEntity.ok().body(response.toString());

    }

    @GetMapping (path="/forum/thread-reply")
    public ResponseEntity<String> getReply(){
      
        return ResponseEntity.ok().body("null");
    }
}
