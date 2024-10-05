package org.encode.libraryprojectapi.service;
import java.io.StringWriter;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailService {

    private JavaMailSender javaMailSender;

    private VelocityEngine velocityEngine;



    public void sendEmail(String to, String subject, Map<String,Object> model){
        MimeMessage message=javaMailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(to);
                helper.setSubject(subject);

                Template template = velocityEngine.getTemplate("borrow-book.vm");
                VelocityContext context = new VelocityContext();
                context.put("memberName", model.get("memberName"));
                context.put("books", model.get("books"));
                context.put("dueDate", model.get("dueDate"));

                StringWriter writer = new StringWriter();

                template.merge(context, writer);

                helper.setText(writer.toString(), true);
                javaMailSender.send(message);
            }
            catch (MessagingException e){
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
    }
}


