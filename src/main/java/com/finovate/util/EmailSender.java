package com.finovate.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Component;

@Component
public class EmailSender {

	String from_email = "bhupatil0001@gmail.com";
	String from_password = "95520207b";

	public void sendMail(String toEmailId, String subject, String bodyContent) {

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
		props.put("mail.smtp.port", "587"); // TLS Port
		props.put("mail.smtp.auth", "true"); // enable authent
		props.put("mail.smtp.starttls.enable", "true"); // enable STARTTLS

		Authenticator authent = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from_email, from_password);
			}
		};
		Session session = Session.getInstance(props, authent);
		try {
			Transport.send(mimeMessageConfiguration(session, toEmailId, subject, bodyContent));
		} catch (MessagingException e) {
			e.printStackTrace();

		}

	}

	private MimeMessage mimeMessageConfiguration(Session session, String toEmail, String subject, String body)
			throws MessagingException {

		MimeMessage mimeMessage = new MimeMessage(session);

		try {
			mimeMessage.addHeader("Content-type", "text/HTML; charset=UTF-8");
		} catch (MessagingException e) {

			e.printStackTrace();
		}
		try {
			mimeMessage.addHeader("format", "flowed");
		} catch (MessagingException e) {

			e.printStackTrace();
		}
		try {
			mimeMessage.addHeader("Content-Transfer-Encoding", "8bit");
		} catch (MessagingException e) {

			e.printStackTrace();
		}
		try {
			mimeMessage.setFrom(new InternetAddress(from_email, "google Note Application"));
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		} catch (MessagingException e) {

			e.printStackTrace();
		}
		try {
			mimeMessage.setReplyTo(InternetAddress.parse(from_email, false));
		} catch (AddressException e) {

			e.printStackTrace();
		} catch (MessagingException e) {

			e.printStackTrace();
		}
		try {
			mimeMessage.setSubject(subject, "UTF-8");
		} catch (MessagingException e) {

			e.printStackTrace();
		}
		try {
			mimeMessage.setText(body, "UTF-8");
		} catch (MessagingException e) {

			e.printStackTrace();
		}
		mimeMessage.setSentDate(new Date());
		mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

		return mimeMessage;
	}

}