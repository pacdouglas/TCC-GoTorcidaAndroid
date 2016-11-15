package br.com.gotorcida.gotorcida.utils;

import android.app.ProgressDialog;
import android.content.Context;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class MailSender extends javax.mail.Authenticator{
    private Context context;
    private Session session;

    private ProgressDialog Dialog;

    private String email;
    private String subtitle;
    private String message;

    public MailSender(Context context, String email, String subtitle, String message) {
        this.context = context;
        this.email = email;
        this.subtitle = subtitle;
        this.message = message;
    }

    public boolean send(){
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("gotorcida8cco@gmail.com", "equipegotorcida321");
                    }
                });

        try {

            MimeMessage mm = new MimeMessage(session);

            mm.setFrom(new InternetAddress("gotorcida8cco@gmail.com"));
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            mm.setSubject(subtitle);
            mm.setText(message);

            Transport.send(mm);

        }
        catch (MessagingException e)
        {
            return false;
        }
        return true;
    }
}
