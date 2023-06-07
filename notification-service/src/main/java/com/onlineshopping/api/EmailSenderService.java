package com.onlineshopping.api;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;
  public void sendSimpleEmail(String toEmail, String body, String subject){
      SimpleMailMessage message = new SimpleMailMessage();
      message.setFrom("naliniprakash950@gmail.com");
      message.setTo(toEmail);
      message.setText(body);
      message.setSubject(subject);

          mailSender.send(message);

      log.info("Notification Mail Sent Successfully");

  }
}
