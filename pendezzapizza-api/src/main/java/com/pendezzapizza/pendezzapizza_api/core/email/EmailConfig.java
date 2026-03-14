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
    public SendEmailService envioEmailService() {
        return switch (emailProperties.getImpl()) {
            case MOCK -> new MockSendingEmailService();
            case SMTP -> new SmtpEmailSendingService();
            case SANDBOX -> new SandboxEmailSendingService();
            default -> new MockSendingEmailService();
        };
    }
}
