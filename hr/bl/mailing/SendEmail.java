import java.util.Properties;
import jakarta.mail.*; // Changed from javax.mail to jakarta.mail
import jakarta.mail.internet.*; // Changed from javax.mail.internet to jakarta.mail.internet

public class SendEmail {
    public static void main(String[] args) {
        // Sender's email ID and password
        final String username = "mridulgehlot230827@acropolis.in";
        final String password = "mridul@1234";

        // Recipient's email IDs
        String[] recipients = {
            "mridulgehlot03@gmail.com",
            "mgcasioseriesandgaming@gmail.com",
            "ymadeeasy@gmail.com"
        };

        // SMTP server properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.example.com"); // e.g., smtp.gmail.com
        props.put("mail.smtp.port", "587");

        // Create a session with an authenticator
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create a MimeMessage object
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setSubject("Test Email Subject");

            // Add multiple recipients
            Address[] toAddresses = new InternetAddress[recipients.length];
            for (int i = 0; i < recipients.length; i++) {
                toAddresses[i] = new InternetAddress(recipients[i]);
            }
            message.addRecipients(Message.RecipientType.TO, toAddresses);

            // Set the email body
            message.setText("This is a test email sent to multiple recipients.");

            // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
