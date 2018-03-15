package com.knoldus.aws.utils.mailhelper;

import com.knoldus.aws.utils.confighelper.ConfigReader;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailerHelper {

    private static final ConfigReader configReader = ConfigReader.getConfigReader("mail");

    public static boolean sendMail(String to, String subject, String body) throws Exception {
        final String FROM = configReader.getProperty("from");
        final String FROM_NAME = configReader.getProperty("fromname");
        final String HOST = configReader.getProperty("host");
        final String USERNAME = configReader.getProperty("username");
        final String PASSWORD = configReader.getProperty("password");
        final int PORT = Integer.parseInt(configReader.getProperty("port"));

        boolean success;

        Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);

        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(FROM, FROM_NAME));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setSubject(subject);
        msg.setContent(body, "text/html");

        Transport transport = session.getTransport();

        try {
            transport.connect(HOST, USERNAME, PASSWORD);
            transport.sendMessage(msg, msg.getAllRecipients());

            success = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            success = false;
        } finally {
            transport.close();
        }

        return success;
    }
}
