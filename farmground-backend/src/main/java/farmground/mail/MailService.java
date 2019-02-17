package farmground.mail;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class MailService {

	private Properties emailProperties;
	
	@PostConstruct
	public void init() {
		String emailPort = "587";//gmail's smtp port

		emailProperties = System.getProperties();
		emailProperties.put("mail.smtp.port", emailPort);
		emailProperties.put("mail.smtp.auth", "true");
		emailProperties.put("mail.smtp.starttls.enable", "true");
		emailProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");


	}

	public void send(String subject, String body, String ... toEmails) throws AddressException, MessagingException {
		MimeMessage emailMessage;
		
		Session mailSession = Session.getDefaultInstance(emailProperties, null);
		emailMessage = new MimeMessage(mailSession);

		for (int i = 0; i < toEmails.length; i++) {
			emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmails[i]));
		}

		emailMessage.setSubject(subject);
		emailMessage.setText(body, "UTF-8", "html");
		
		String emailHost = "smtp.gmail.com";
		String fromUser = "farmgroundmanager@gmail.com";//just the id alone without @gmail.com
		String fromUserEmailPassword = "farmground1234";

		Transport transport = mailSession.getTransport("smtp");

		transport.connect(emailHost, fromUser, fromUserEmailPassword);
		transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
		transport.close();
		System.out.println("Email sent successfully.");
	}

}