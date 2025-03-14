package com.youshi.zebra.core.utils;

import java.util.Collection;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.StringUtils;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public class MailUtils {

    private static final String DEFAULT_ENCODING = "utf-8";

    private static final String CONTENT_TYPE = "text/html;charset=" + DEFAULT_ENCODING;

    private static final String FALSE = "false";

    private static final String SMTP_SERVER = "127.0.0.1";

    private static final String MAIL_FULLUSER = "doradoapp@lbt.im";

    static {
        System.setProperty("mail.mime.charset", DEFAULT_ENCODING);
    }

    public static void sendMailSeparately(String message, final String title,
            final String... emails) {
        for (String email : emails) {
            sendMail(message, title, email);
        }
    }

    public static void sendMail(String message, final String title, final String... emails) {
        try {
            if (StringUtils.isEmpty(message)) {
                message = "EMPTY BODY";
            }
            final Properties mailProps = new Properties();
            mailProps.put("mail.smtp.user", MAIL_FULLUSER);
            mailProps.put("mail.smtp.host", SMTP_SERVER);
            mailProps.put("mail.smtp.auth", FALSE);

            final Session mailSession = Session.getDefaultInstance(mailProps);
            final MimeMessage mimeMessage = new MimeMessage(mailSession);
            final Address[] emailAddresses = new Address[emails.length];
            int i = 0;
            for (final String email : emails) {
                emailAddresses[i++] = new InternetAddress(email);
            }
            mimeMessage.setFrom(new InternetAddress(MAIL_FULLUSER));
            mimeMessage.setRecipients(Message.RecipientType.TO, emailAddresses);
            mimeMessage.setSubject(title, DEFAULT_ENCODING);
            final MimeMultipart multi = new MimeMultipart();
            final BodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setContent(message, CONTENT_TYPE);
            textBodyPart.setText(message);
            multi.addBodyPart(textBodyPart);
            mimeMessage.setContent(multi, CONTENT_TYPE);
            mimeMessage.saveChanges();
            Transport.send(mimeMessage);
        } catch (final Throwable e) {
            e.printStackTrace();
        }
    }

    public static void sendMailVia(String fromEmail, String message, final String title,
            final String... emails) {
        try {
            if (StringUtils.isEmpty(message)) {
                message = "EMPTY BODY";
            }
            final Properties mailProps = new Properties();
            mailProps.put("mail.smtp.user", MAIL_FULLUSER);
            mailProps.put("mail.smtp.host", SMTP_SERVER);
            mailProps.put("mail.smtp.auth", FALSE);

            final Session mailSession = Session.getDefaultInstance(mailProps);
            final MimeMessage mimeMessage = new MimeMessage(mailSession);
            final Address[] emailAddresses = new Address[emails.length];
            int i = 0;
            for (final String email : emails) {
                emailAddresses[i++] = new InternetAddress(email);
            }
            mimeMessage.setFrom(new InternetAddress(fromEmail));
            mimeMessage.setRecipients(Message.RecipientType.TO, emailAddresses);
            mimeMessage.setSubject(title, DEFAULT_ENCODING);
            final MimeMultipart multi = new MimeMultipart();
            final BodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setContent(message, CONTENT_TYPE);
            textBodyPart.setText(message);
            multi.addBodyPart(textBodyPart);
            mimeMessage.setContent(multi, CONTENT_TYPE);
            mimeMessage.saveChanges();
            Transport.send(mimeMessage);
        } catch (final Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param message
     * @param title
     * @param emails
     */
    public static void sendMailHtmlSeparately(String message, final String title,
            final String... emails) {
        for (String email : emails) {
            sendMailHtml(message, title, email);
        }
    }

    /**
     * 
     * @param message
     * @param title
     * @param emails
     */
    public static void sendMailHtml(String message, final String title, final String... emails) {
        try {
            if (StringUtils.isEmpty(message)) {
                message = "EMPTY BODY";
            }
            final Properties mailProps = new Properties();
            mailProps.put("mail.smtp.user", MAIL_FULLUSER);
            mailProps.put("mail.smtp.host", SMTP_SERVER);
            mailProps.put("mail.smtp.auth", FALSE);

            final Session mailSession = Session.getDefaultInstance(mailProps);
            final MimeMessage mimeMessage = new MimeMessage(mailSession);
            final Address[] emailAddresses = new Address[emails.length];
            int i = 0;
            for (final String email : emails) {
                emailAddresses[i++] = new InternetAddress(email);
            }
            mimeMessage.setFrom(new InternetAddress(MAIL_FULLUSER));
            mimeMessage.setRecipients(Message.RecipientType.TO, emailAddresses);
            mimeMessage.setSubject(title, DEFAULT_ENCODING);
            final MimeMultipart multi = new MimeMultipart();
            final BodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setContent(message, CONTENT_TYPE);
            multi.addBodyPart(textBodyPart);
            mimeMessage.setContent(multi, CONTENT_TYPE);
            mimeMessage.saveChanges();
            Transport.send(mimeMessage);
        } catch (final Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param message
     * @param title
     * @param emails
     */
    public static void sendMailHtmlVia(String fromEmail, String message, final String title,
            final String... emails) {
        try {
            if (StringUtils.isEmpty(message)) {
                message = "EMPTY BODY";
            }
            final Properties mailProps = new Properties();
            mailProps.put("mail.smtp.user", MAIL_FULLUSER);
            mailProps.put("mail.smtp.host", SMTP_SERVER);
            mailProps.put("mail.smtp.auth", FALSE);

            final Session mailSession = Session.getDefaultInstance(mailProps);
            final MimeMessage mimeMessage = new MimeMessage(mailSession);
            final Address[] emailAddresses = new Address[emails.length];
            int i = 0;
            for (final String email : emails) {
                emailAddresses[i++] = new InternetAddress(email);
            }
            mimeMessage.setFrom(new InternetAddress(fromEmail));
            mimeMessage.setRecipients(Message.RecipientType.TO, emailAddresses);
            mimeMessage.setSubject(title, DEFAULT_ENCODING);
            final MimeMultipart multi = new MimeMultipart();
            final BodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setContent(message, CONTENT_TYPE);
            multi.addBodyPart(textBodyPart);
            mimeMessage.setContent(multi, CONTENT_TYPE);
            mimeMessage.saveChanges();
            Transport.send(mimeMessage);
        } catch (final Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * @param message
     * @param title
     * @param emails
     */
    public static void sendMailWithAttachment(String message, final String title,
            final Collection<String> fileNames, final String... emails) {
        try {
            if (StringUtils.isEmpty(message)) {
                message = "EMPTY BODY";
            }
            final Properties mailProps = new Properties();
            mailProps.put("mail.smtp.user", MAIL_FULLUSER);
            mailProps.put("mail.smtp.host", SMTP_SERVER);
            mailProps.put("mail.smtp.auth", FALSE);

            final Session mailSession = Session.getDefaultInstance(mailProps);
            final MimeMessage mimeMessage = new MimeMessage(mailSession);
            final Address[] emailAddresses = new Address[emails.length];
            int i = 0;
            for (final String email : emails) {
                emailAddresses[i++] = new InternetAddress(email);
            }
            mimeMessage.setFrom(new InternetAddress(MAIL_FULLUSER));
            mimeMessage.setRecipients(Message.RecipientType.TO, emailAddresses);
            mimeMessage.setSubject(title, DEFAULT_ENCODING);
            final MimeMultipart multi = new MimeMultipart();
            final BodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setContent(message, CONTENT_TYPE);
            if (fileNames != null) {
                for (final String fileName : fileNames) {
                    try {
                        final BodyPart attachment = new MimeBodyPart();
                        final FileDataSource fds = new FileDataSource(fileName);
                        attachment.setDataHandler(new DataHandler(fds));
                        attachment.setFileName(fds.getName());
                        multi.addBodyPart(attachment);
                    } catch (final Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
            multi.addBodyPart(textBodyPart);
            mimeMessage.setContent(multi, CONTENT_TYPE);
            mimeMessage.saveChanges();
            Transport.send(mimeMessage);
        } catch (final Throwable e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        sendMail("测试", "测试", "vela@longbeach-inc.com");
    }
}
