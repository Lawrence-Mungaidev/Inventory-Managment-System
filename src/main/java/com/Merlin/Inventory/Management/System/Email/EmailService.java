package com.Merlin.Inventory.Management.System.Email;


import com.resend.Resend;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${resend.api.key}")
    private String resendApiKey;

    @Async
    public void sendEmail(String to, String subject, String body) {
        try {
            Resend resend = new Resend(resendApiKey);
            CreateEmailOptions request = CreateEmailOptions.builder()
                    .from("onboarding@resend.dev")
                    .to(to)
                    .subject(subject)
                    .text(body)
                    .build();
            CreateEmailResponse response = resend.emails().send(request);
            System.out.println("Email sent successfully to: " + to);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}
