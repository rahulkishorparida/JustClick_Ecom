package com.jc.demo.service.impl;

import com.jc.demo.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendInvoice(String recipientEmail, ByteArrayOutputStream pdfStream, String orderId) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(recipientEmail);
            helper.setSubject(buildSubject(orderId));
            helper.setText(buildBodyText(), false); // false = plain text.//open

            helper.addAttachment(buildFilename(orderId), new ByteArrayResource(pdfStream.toByteArray()));

            mailSender.send(message);
        } catch (MessagingException e) {
            // Log the error (you can integrate a logger like Logback/SLF4J)
            System.err.println("Failed to send invoice email: " + e.getMessage());
            throw new RuntimeException("Failed to send invoice email", e);
        }
    }

    private String buildSubject(String orderId) {
        return "Your Invoice - Order #" + orderId;
    }

    private String buildBodyText() {
        return "Thank you for your purchase!\nPlease find your invoice attached as a PDF.";
    }

    private String buildFilename(String orderId) {
        return "invoice_" + orderId + ".pdf";
    }
}
