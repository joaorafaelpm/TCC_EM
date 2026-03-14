package com.pendezzapizza.pendezzapizza_api.infrastructure.service.email;

import com.pendezzapizza.pendezzapizza_api.core.email.EmailProperties;
import freemarker.template.Configuration;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;


public class SandboxEmailSendingService extends SmtpEmailSendingService {

    @Autowired
    private EmailProperties emailProperties;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private Configuration freemarkerConfig;

    @Override
    public void send(Message message) {
        try {
            MimeMessage mimeMessage = generateMimeMessage(message);
            mailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new EmailException("Could not send email.", e);
        }
    }

    protected MimeMessage generateMimeMessage(Message message){
        try {
            String body = processTemplate(message);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

            // Note: In a Sandbox environment, the recipient is forced to the sender's address (or a specific test address).
            // This is done by setting setTo to the value of emailProperties.getSender()
            helper.setFrom(emailProperties.getSender());
            helper.setTo(emailProperties.getSender());

            helper.setSubject(message.getSubject());
            helper.setText(body , true);
            return mimeMessage;
        }
        catch (Exception e) {
            throw new EmailException("Could not create email message.", e);
        }
    }

}