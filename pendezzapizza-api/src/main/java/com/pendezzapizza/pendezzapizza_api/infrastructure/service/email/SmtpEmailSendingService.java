package com.pendezzapizza.pendezzapizza_api.infrastructure.service.email;

import com.pendezzapizza.pendezzapizza_api.core.email.EmailProperties;
import com.pendezzapizza.pendezzapizza_api.domain.service.SendEmailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

public class SmtpEmailSendingService implements SendEmailService {

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
            helper.setFrom(emailProperties.getSender());
            helper.setTo(message.getRecipients().toArray(new String[0]));
            helper.setSubject(message.getSubject());
            helper.setText(body, true);
            return mimeMessage;
        }
        catch (Exception e) {
            throw new EmailException("Could not create email message.", e);
        }
    }


    protected String processTemplate (Message message) {
        try {
            Template template = freemarkerConfig.getTemplate(message.getBody());

            return FreeMarkerTemplateUtils.processTemplateIntoString(template, message.getVariables());
        } catch (Exception e) {
            throw new EmailException("Could not build the email template.", e);
        }
    }
}