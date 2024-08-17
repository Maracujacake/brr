package web.brr.service.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import web.brr.service.spec.EmailServiceSpec;

@Service
public class SendGridEmailService implements EmailServiceSpec {
    private SendGrid sendGrid;
    private String fromEmail;

    public SendGridEmailService(SendGrid sendGrid, @Value("${FROM_EMAIL}") String fromEmail) {
        this.sendGrid = sendGrid;
        this.fromEmail = fromEmail;
    }

    public boolean sendEmail(String toEmail, String subject, String body) {
        Email from = new Email(fromEmail);
        Email to = new Email(toEmail);

        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, to, content);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
}
