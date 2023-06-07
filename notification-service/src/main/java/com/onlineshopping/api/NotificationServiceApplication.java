package com.onlineshopping.api;

import com.onlineshopping.api.Event.OrderPlacedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@Slf4j
public class NotificationServiceApplication {

    @Autowired
    private EmailSenderService emailSenderService;
    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(OrderPlacedEvent orderPlacedEvent){
        String message = "Thanks for the shopping, this mail confirms that your order has " +
                "been placed with ordernumber: "+orderPlacedEvent.getOrderNumber();
        List<String> mails = new ArrayList<>();
        mails.add("akchitty111@gmail.com");
        mails.add("saibaddala172@gmail.com");
        for(String mail : mails){
            emailSenderService.sendSimpleEmail(mail, message,
                    "Order Confirmation");
            log.info("Notification sent to : "+ mail);
        }
        log.info("Received Notification for order -{}", orderPlacedEvent.getOrderNumber());
    }


}
