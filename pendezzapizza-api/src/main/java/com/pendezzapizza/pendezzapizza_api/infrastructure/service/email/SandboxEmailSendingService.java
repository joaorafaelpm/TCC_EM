package com.pendezzapizza.pendezzapizza_api.infrastructure.service.email;

import com.pendezzapizza.pendezzapizza_api.core.email.EmailProperties;
import freemarker.template.Configuration;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * Serviço de envio de email de testes, com um modelo oficial enviado para um email da empresa
 */
public class SandboxEmailSendingService extends SmtpEmailSendingService {

    @Autowired
    private EmailProperties emailProperties;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private Configuration freemarkerConfig;

//    Função de envio de email
    @Override
    public void send(Message message) {
        try {
//            Gera a mensagem e envia direto para o email de que vai receber
            MimeMessage mimeMessage = generateMimeMessage(message);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new EmailException("Could not send email.", e);
        }
    }

//    Função auxiliar para gerar a mensagem de email
    protected MimeMessage generateMimeMessage(Message message){
        try {
//            Recebemos a mensagem em forma de texto
            String body = processTemplate(message);
//            Criamos uma MimeMessage (que é um padrão de mensagem)
            MimeMessage mimeMessage = mailSender.createMimeMessage();
//            A partir dessa MimeMessage a gente faz um helper que é tipo um construtor para facilitar a escrita dessa mensagem
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

//            Agora a gente preenche as informações da mensagem
            helper.setFrom(emailProperties.getSender());
            helper.setTo(emailProperties.getSender());

            helper.setSubject(message.getSubject());
            helper.setText(body , true);
            return mimeMessage;
        }
        catch (Exception e) {
            throw new EmailException("Não foi possível criar a mensagem.", e);
        }
    }

}