package vttp.server.Repository;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.DeleteResult;

import vttp.server.Model.Comment;
import vttp.server.Model.Threads;

@Repository
public class ForumRepository {

    @Autowired
    MongoTemplate template;

    public Boolean postNewThread(Threads thread) {

        Document doc = new Document().append("_id", thread.getId())
                .append("username", thread.getUsername())
                .append("title", thread.getTitle())
                .append("text", thread.getText())
                .append("timestamp", System.currentTimeMillis())
                .append("userurl", thread.getUserurl())
                .append("comments", new ArrayList<Document>());
        template.save(doc, "forum");
        return true;
    }

    public List<Document> getThreads() {
        ProjectionOperation project = Aggregation.project()
                .andInclude("_id")
                .andInclude("username")
                .andInclude("title")
                .andInclude("userurl")
                .andInclude("timestamp")
                .and("comments").as("comments");
        // .and("comments").size().as("comments");

        Aggregation pipeline = Aggregation.newAggregation(project);

        List<Document> doc = template.aggregate(pipeline, "forum", Document.class).getMappedResults();
        System.out.println("this is from the back!!!! >>>>>>"+doc);
        return doc;

    }

    public boolean postNewComment(Comment comment, String threadId) {

        Document doc = new Document().append("_id", comment.getId())
                .append("username", comment.getUsername())
                .append("text", comment.getText())
                .append("timestamp", System.currentTimeMillis());

        Document thread = getThreadById(threadId);
        if (thread != null) {
            List<Document> listOfComments = thread.getList("comments", Document.class);
            if (listOfComments != null) {
                listOfComments.add(doc);
                thread.put("comments", listOfComments);
            } else {
                List<Document> newComments = new ArrayList<>();
                newComments.add(doc);
                thread.put("comments", newComments);
            }
            Query query = Query.query(Criteria.where("_id").is(thread.getString("_id")));
            template.findAndReplace(query, thread, "forum");

        }
        return true;
    }

    public Document getThreadById(String id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        return template.findOne(query, Document.class, "forum");
    }

    public DeleteResult deleteThread(String threadId) {
        Query query = Query.query(Criteria.where("_id").is(threadId));
        DeleteResult result = template.remove(query, Document.class, "forum");
        System.out.println("Delete result" + result);
        return result;
    }

}
