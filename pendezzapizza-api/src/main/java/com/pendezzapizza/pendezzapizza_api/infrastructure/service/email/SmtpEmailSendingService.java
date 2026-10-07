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

/**
 * Serviço de envio de email para cliente, com um modelo oficial enviado para um email dos clientes
 */
public class SmtpEmailSendingService implements SendEmailService {

    @Autowired
    private EmailProperties emailProperties;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private Configuration freemarkerConfig;

//    Função para enviar email
    @Override
    public void send(Message message) {
        try {
            MimeMessage mimeMessage = generateMimeMessage(message);
            mailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new EmailException("Could not send email.", e);
        }

    }
//    Gera a mensagem
    protected MimeMessage generateMimeMessage(Message message){
        try {
//            Recebe a mensagem em forma de texto
            String body = processTemplate(message);

//            Forma a mensagem no padrão do MimeMessage
            MimeMessage mimeMessage = mailSender.createMimeMessage();
//            Cria um helper para facilitar na escrita da mensagem
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

//            Preenche as informações da mensagem
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

//    Gera o template para o MimeMessage a partir de uma mensagem e transformando-a em texto
    protected String processTemplate (Message message) {
        try {
//            Formamos um template do freemarker a partir do corpo da mensagem
            Template template = freemarkerConfig.getTemplate(message.getBody());

//            Transformamos o template em um texto para formar o corpo compatível com o modelo MimeMessage
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, message.getVariables());
        } catch (Exception e) {
            throw new EmailException("Could not build the email template.", e);
        }
    }
}