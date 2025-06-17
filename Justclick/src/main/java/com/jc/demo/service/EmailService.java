package com.jc.demo.service;

import java.io.ByteArrayOutputStream;

public interface EmailService {
    void sendInvoice(String recipientEmail, ByteArrayOutputStream pdfStream, String orderId);
}