package vttp.server.Service;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import vttp.server.Repository.UserRepository;

@Service
public class EmailService {

  @Value("${sendgrid.email.apiKey}")
  String sendgridKey;

  @Autowired
  UserRepository userRepo;

  private final static String fromEmail = "petbuddy2024@gmail.com";

  public String sendEmail(String email, String name, String appointmentDate) throws Exception {

    ZonedDateTime dateTime = ZonedDateTime.parse(appointmentDate);

    String formattedDate = dateTime.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy", Locale.ENGLISH));
    //System.out.println(formattedDate);

    Email from = new Email(fromEmail, "PetBuddy");
    String emailTo = email;
    Email to = new Email(emailTo);
    String subject = "🐾 Your Doggie Date Inquiry is Received!";
    String emailContent = "<html><body>"
    + "<p>Hey " + name + "!</p>"
    + "<p>Thank you for your inquiry! We've received your request to meet our furry friends on " + formattedDate 
    + "<p>Get ready for some tail-wagging excitement! Keep an eye on your inbox for the official paw-printed confirmation!</p>"
    + "<p>If you have any questions or need to make any changes, feel free to reach out to us at <a href='mailto:"
    + fromEmail + "'>" + fromEmail + "</a>.</p>"
    + "<p>Warm regards,</p>"
    + "<p>The PetBuddy Team</p>"
    + "</body></html>";
Content content = new Content("text/html", emailContent);

    Mail mail = new Mail(from, subject, to, content);
    SendGrid sg = new SendGrid(sendgridKey);

    Request request = new Request();

    try {
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());
      Response response = sg.api(request);
      return response.getBody();
    } catch (IOException ex) {
      throw new Exception("SendGrid Email API error" + ex);
    }

  }

  public String sendConfirmationEmail(String email, String name, String appointmentDate) throws Exception {

    ZonedDateTime dateTime = ZonedDateTime.parse(appointmentDate);

    String formattedDate = dateTime.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy", Locale.ENGLISH));
    // System.out.println(formattedDate);

    Email from = new Email(fromEmail, "PetBuddy");
    String emailTo = email;
    Email to = new Email(emailTo);
    String subject = "Pawsitively Exciting News! 🎉 Your Doggie Date is Confirmed!";
    String emailContent = "<html><body>"
        + "<p>Hey " + name + "!</p>"
        + "<p>Get ready to embark on a pawsitively amazing adventure at Pet Buddy as you meet and greet our adorable four-legged pals.</p>"
        + "<p><strong>Appointment Details:</strong></p>"
        + "<ul>"
        + "<li>📅 Date: " + formattedDate + "</li>"
        + "</ul>"
        + "<p>Our furry friends can't wait to shower you with cuddles and kisses, so be sure to bring your biggest smile.</p>"
        + "<p>If you have any questions or need to make any changes, feel free to reach out to us at <a href='mailto:"
        + fromEmail + "'>" + fromEmail + "</a>.</p>"
        + "<p>Warm regards,</p>"
        + "<p>The PetBuddy Team</p>"
        + "</body></html>";

    Content content = new Content("text/html", emailContent);

    Mail mail = new Mail(from, subject, to, content);
    SendGrid sg = new SendGrid(sendgridKey);

    Request request = new Request();
    // request.setMethod(Method.POST);
    // request.setEndpoint("mail/send");

    // request.setBody(mail.build());
    // // System.out.println(request.getBody());
    // Response response = sg.api(request);

    // System.out.println( response.getBody());

    try {
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());
      Response response = sg.api(request);
      return response.getBody();
    } catch (IOException ex) {
      throw new Exception("SendGrid Email API error" + ex);
    }

  }


  public String sendDeclinationEmail(String email, String name, String appointmentDate) throws Exception {
   
    ZonedDateTime dateTime = ZonedDateTime.parse(appointmentDate);

    String formattedDate = dateTime.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy", Locale.ENGLISH));
    // System.out.println(formattedDate);

    Email from = new Email(fromEmail, "PetBuddy");
    String emailTo = email;
    Email to = new Email(emailTo);
    String subject = "We're Sorry! Your Doggie Date Request Has Been Declined 😢";
    String emailContent = "<html><body>"
        + "<p>Hey " + name + "!</p>"
        + "<p>We regret to inform you that your doggie date request has been declined.</p>"
        + "<p><strong>Reason for Decline:</strong></p>"
        + "<ul>"
        + "<li>Your request did not meet our current availability.</li>"
        + "</ul>"
        + "<p>We apologize for any inconvenience this may cause. If you have any questions or would like to inquire about future availability, feel free to reach out to us at <a href='mailto:"
        + fromEmail + "'>" + fromEmail + "</a>.</p>"
        + "<p>Warm regards,</p>"
        + "<p>The PetBuddy Team</p>"
        + "</body></html>";

    Content content = new Content("text/html", emailContent);

    Mail mail = new Mail(from, subject, to, content);
    SendGrid sg = new SendGrid(sendgridKey);

    Request request = new Request();
 
    try {
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());
      Response response = sg.api(request);
      return response.getBody();
    } catch (IOException ex) {
      throw new Exception("SendGrid Email API error" + ex);
    }

  }
}
