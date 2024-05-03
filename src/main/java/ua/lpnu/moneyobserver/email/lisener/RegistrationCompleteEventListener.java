package ua.lpnu.moneyobserver.email.lisener;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import ua.lpnu.moneyobserver.domain.User;
import ua.lpnu.moneyobserver.domain.VerificationToken;
import ua.lpnu.moneyobserver.email.RegistrationCompleteEvent;
import ua.lpnu.moneyobserver.service.VerificationTokenService;

import java.io.UnsupportedEncodingException;

@Slf4j
@EnableAsync
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    private final VerificationTokenService tokenService;
    private final JavaMailSender mailSender;

    @Async
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        User user = event.getUser();
        VerificationToken token = tokenService.createNewTokenForUser(user);
        String url = event.getApplicationUrl() + "/api/auth/verifyEmail?token=" + token.getToken();
        try {
            log.info("Email sending...");
            sendVerificationEmail(url,user);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Email sending problem");
            throw new RuntimeException(e);
        }
        log.info("Email has been sent. Click the link to verify your registration :  {}", url);
    }

    private void sendVerificationEmail(String url,User user) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        //String senderName = "User Registration Portal Service";
        String mailContent = "<p> Hi, " + user.getUsername() + ", </p>" +
                "<p>Thank you for registering with us," + "" +
                "Please, follow the link below to complete your registration.</p>" +
                "<a href=\"" + url + "\">Verify your email to activate your account</a>" +
                "<p> Thank you <br> Users Registration Portal Service";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);

        messageHelper.setTo(user.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }
}
