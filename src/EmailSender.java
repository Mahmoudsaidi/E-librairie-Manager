import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.JOptionPane;

public class EmailSender {

    public static void sendEmail(String username, String password, String toEmail, String subject, String body) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587"); 


        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
    public static void Send(String BookName,String email)
    {
            JOptionPane.showMessageDialog(null, BookName + " has Been returned and email has been send to "+ email );
            System.out.println(BookName + " has Been returned and email has been send to "+email );
    }

   /*public static void main(String[] args) {
                ***this is a working email***
        String username1 = "e.librairie.manager@gmail.com"; 
        String password1 = "Midous 1234567890";
                    ***************
                    
        String toEmail1 = "gamershome2@gmail.com";
        String subject1 = "Subject for Email 1";

        String body1 = "Body of Email 1";

        sendEmail(username1, password1, toEmail1, subject1, body1);
    }*/ 



}
