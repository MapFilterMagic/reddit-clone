package com.mapfiltermagic.springredditclone.services;

import com.mapfiltermagic.springredditclone.models.NotificationEmail;

import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {
    
    // TODO: Rework this error message.
    private static final String COULD_NOT_SEND_EMAIL_MSG = "We could not send an activation email. Please try again.";

    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder; // TODO: Figure out if this is needed???

    public void sendMail(NotificationEmail notificationEmail) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("spring_reddit_clone@mail.com");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(notificationEmail.getBody());
        };

        try {
            mailSender.send(messagePreparator);

            log.info("Activation email sent");

        } catch (MailException ex) {
            log.error("Exception encountered when sending an activation email => {}", ex.getMessage());

            // TODO: Consider changing this error code.
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, COULD_NOT_SEND_EMAIL_MSG);
        }
    }
}
