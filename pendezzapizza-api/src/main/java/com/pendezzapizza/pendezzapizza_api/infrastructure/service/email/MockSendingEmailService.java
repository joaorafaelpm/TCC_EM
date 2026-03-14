package com.pendezzapizza.pendezzapizza_api.infrastructure.service.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MockSendingEmailService extends SmtpEmailSendingService {

    @Override
    public void send(Message message) {
        String body = processTemplate(message);
        log.info("[MOCK] - E-mail será enviado para o :{} \n Com o assunto: {} \n Com o corpo: {}" , message.getRecipients() , message.getSubject(), body );
    }

}
