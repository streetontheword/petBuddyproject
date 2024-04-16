package vttp.server.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp.server.Model.Comment;
import vttp.server.Model.Threads;
import vttp.server.Repository.ForumRepository;

@Service
public class ForumService {

    @Autowired
    ForumRepository forumRepo;

    public String postNewThread(Threads thread) {
        String threadId = UUID.randomUUID().toString().substring(0, 8);
        thread.setId(threadId);
        forumRepo.postNewThread(thread);
        return threadId;
    }

    public String postNewComment(Comment comment, String threadId) {

        String commentId = UUID.randomUUID().toString().substring(0, 8);
        comment.setId(commentId);
        if (forumRepo.postNewComment(comment, threadId)) {
            String commentUserName = comment.getUsername();
            return commentUserName;
        }
        return "unable to post new comment";
    }

    public List<Document> getThreads() {
        List<Document> retrievedList = forumRepo.getThreads();

        for (Document document : retrievedList) {
            if (document.containsKey("timestamp") && document.get("timestamp") != null) {
                long timestampMillis = document.getLong("timestamp");

                document.remove("timestamp");

                Instant instant = Instant.ofEpochMilli(timestampMillis);

                ZonedDateTime timestamp = instant.atZone(ZoneOffset.ofHours(8));
                ZonedDateTime now = ZonedDateTime.now(ZoneOffset.ofHours(8));
                LocalDate timestampDate = timestamp.toLocalDate();
                LocalDate nowDate = now.toLocalDate();

                if (timestampDate.isEqual(nowDate)) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'Today at' h:mm a");
                    String formattedTimestamp = timestamp.format(formatter);
                    document.append("timestamp", formattedTimestamp);

                } else if (timestampDate.isEqual(nowDate.minusDays(1))) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'Yesterday at' h:mm a");
                    String formattedTimestamp = timestamp.format(formatter);
                    document.append("timestamp", formattedTimestamp);

                } else {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd 'at' h:mm a");
                    String formattedTimestamp = timestamp.format(formatter);
                    document.append("timestamp", formattedTimestamp);

                }
            }

        }
        return retrievedList;
    }

    public Document getThreadById(String id) {
        Document retrievedDoc = forumRepo.getThreadById(id);

        if (retrievedDoc.containsKey("timestamp") && retrievedDoc.get("timestamp") != null) {
            long timestampMillis = retrievedDoc.getLong("timestamp");

            retrievedDoc.remove("timestamp");

            Instant instant = Instant.ofEpochMilli(timestampMillis);
            ZonedDateTime timestamp = instant.atZone(ZoneOffset.ofHours(8));
            ZonedDateTime now = ZonedDateTime.now(ZoneOffset.ofHours(8));

            LocalDate timestampDate = timestamp.toLocalDate();
            LocalDate nowDate = now.toLocalDate();

            if (timestampDate.isEqual(nowDate)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'Today at' h:mm a");
                String formattedTimestamp = timestamp.format(formatter);
                retrievedDoc.append("timestamp", formattedTimestamp);
            } else if (timestampDate.isEqual(nowDate.minusDays(1))) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'Yesterday at' h:mm a");
                String formattedTimestamp = timestamp.format(formatter);
                retrievedDoc.append("timestamp", formattedTimestamp);
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd 'at' h:mm a");
                String formattedTimestamp = timestamp.format(formatter);
                retrievedDoc.append("timestamp", formattedTimestamp);
            }

            for (Document comment : retrievedDoc.getList("comments", Document.class)) {
                long commentTimestampMillis = comment.getLong("timestamp");
                comment.remove("timestamp");

                Instant commentInstant = Instant.ofEpochMilli(commentTimestampMillis);

                ZonedDateTime commentTimeStamp = commentInstant.atZone(ZoneOffset.ofHours(8));
                ZonedDateTime commentNow = ZonedDateTime.now(ZoneOffset.ofHours(8));

                LocalDate commentTimestampDate = commentTimeStamp.toLocalDate();
                LocalDate commentNowDate = commentNow.toLocalDate();

                if (commentTimestampDate.isEqual(commentNowDate)) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'Today at' h:mm a");
                    String formattedTimestamp = commentTimeStamp.format(formatter);
                    comment.append("timestamp", formattedTimestamp);
                } else if (commentTimestampDate.isEqual(commentNowDate.minusDays(1))) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'Yesterday at' h:mm a");
                    String formattedTimestamp = commentTimeStamp.format(formatter);
                    comment.append("timestamp", formattedTimestamp);
                } else {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd 'at' h:mm a");
                    String formattedTimestamp = commentTimeStamp.format(formatter);
                    comment.append("timestamp", formattedTimestamp);
                }
            }

        }
        return retrievedDoc;

    }

    public String deleteThread(String threadId) {
        forumRepo.deleteThread(threadId);
        return threadId;
    }

}
