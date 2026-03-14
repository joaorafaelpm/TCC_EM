package com.pendezzapizza.pendezzapizza_api.core.email;

import com.pendezzapizza.pendezzapizza_api.domain.service.SendEmailService;
import com.pendezzapizza.pendezzapizza_api.infrastructure.service.email.MockSendingEmailService;
import com.pendezzapizza.pendezzapizza_api.infrastructure.service.email.SandboxEmailSendingService;
import com.pendezzapizza.pendezzapizza_api.infrastructure.service.email.SmtpEmailSendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfig {

    @Autowired
    private EmailProperties emailProperties;

    @Bean
    public SendEmailService sendEmailService() {
        switch (emailProperties.getImpl()) {
            case MOCK:
                return new MockSendingEmailService();
            case SMTP:
                return new SmtpEmailSendingService();
            case SANDBOX:
                return new SandboxEmailSendingService();
            default:
                return null;
        }
    }
}
