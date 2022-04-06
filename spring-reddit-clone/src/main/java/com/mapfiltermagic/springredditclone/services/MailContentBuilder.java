package com.mapfiltermagic.springredditclone.services;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MailContentBuilder {
    
    private final TemplateEngine templateEngine;

    /**
     * Puts together the email template message
     *
     * @param message
     * @return
     */
    public String build(String message) {
        Context context = new Context();
        context.setVariable("message", message);

        return templateEngine.process("mailTemplate", context);
    }

}
