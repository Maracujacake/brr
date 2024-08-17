package web.brr.service.spec;

public interface EmailServiceSpec {
    public boolean sendEmail(String to, String subject, String body);
}
