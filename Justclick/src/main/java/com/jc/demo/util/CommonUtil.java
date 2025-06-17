package com.jc.demo.util;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.jc.demo.model.ProductOrder;
import com.jc.demo.model.User;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Component
public class CommonUtil {
	@Autowired
	private  JavaMailSender mailSender;
	
	public Boolean sendMail(String url, String reciepentEmail) throws UnsupportedEncodingException, MessagingException {
		
		MimeMessage message = mailSender.createMimeMessage();
		
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom("rahulkishorparida789@gmail.com", "jaustClick");
		helper.setTo(reciepentEmail);
		
	      String content = "<p>Hello,</p>"
	                + "<p>You have requested to reset your password.</p>"
	                + "<p>Click the link below to change your password:</p>"
	                + "<p><a href=\"" + url + "\">Change my password</a></p>"
	                + "<br><p>Ignore this email if you did not make this request.</p>";

	        helper.setSubject("Password Reset");
	        helper.setText(content, true);
              
	        mailSender.send(message);

	        return true;
	}

	public static String generateUrl(HttpServletRequest request) {
		String siteUrl = request.getRequestURL().toString();
		
		return siteUrl.replace(request.getServletPath(), "");
		
//		return generateUrl(request);
		
		//extracts and returns the base URL of your web application,
		//without the servlet path (like /reset-password or /login).	
	}
	

	public String extractEmailFromAddress(String address) {
	    String emailRegex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
	    Pattern pattern = Pattern.compile(emailRegex);

	    Matcher matcher = pattern.matcher(address);

	    if (matcher.find()) {
	        return matcher.group();
	    }
	    return null;
	}
		
	public Boolean sendMailForProductStatus(ProductOrder order) throws UnsupportedEncodingException, MessagingException {
		
	    String address = order.getMasterAddress().getAddress();
	    String recipientEmail = extractEmailFromAddress(address);
           
	    if (recipientEmail == null) {
	        System.out.println("No email found in address.");
	        return false;
	    }

	    try {
	        MimeMessage message = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);

	        helper.setFrom("rahulkishorparida789@gmail.com", "JustClick");
	        helper.setTo(recipientEmail);
	        helper.setSubject("Your Order Status Update - Order #" + order.getOrderId());

	        // Email template with placeholders
	        String msgTemplate = "<p>Dear Customer,</p>"
	                + "<p>Thank you for shopping with <strong>JustClick</strong>!</p>"
	                + "<p>Your order status has been updated to: <strong>[[orderStatus]]</strong></p>"
	                + "<hr/>"
	                + "<p><strong>Product Details:</strong></p>"
	                + "<ul>"
	                + "<li><strong>Name:</strong> [[ProductName]]</li>"
	                + "<li><strong>Quantity:</strong> [[quantity]]</li>"
	                + "<li><strong>Price:</strong> ₹[[price]]</li>"
	                + "<li><strong>Payment Type:</strong> [[paymentType]]</li>"
	                + "</ul>"
	                + "<hr/>"
	                + "<p>We will notify you as your order progresses.</p>"
	                + "<p><em>~ JustClick Team</em></p>";

	        // Replace placeholders
	        String finalMsg = msgTemplate
	                .replace("[[orderStatus]]", order.getStatus())
	                .replace("[[ProductName]]", order.getProduct().getName())
	                .replace("[[quantity]]", String.valueOf(order.getQuantity()))
	                .replace("[[price]]", String.valueOf(order.getFinalPrice()))
	                .replace("[[paymentType]]", order.getPaymentType());

	        helper.setText(finalMsg, true); // true = HTML
	        mailSender.send(message);

	        return true;

	    } catch (MessagingException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	
	public boolean sendAccountStatus(User user) {
		
	    if (user == null || user.getEmail() == null || user.getEmail().isEmpty()) {
	        return false;
	    }

	    try {
	        MimeMessage message = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

	        helper.setFrom("rahulkishorparida789@gmail.com", "JustClick");
	        helper.setTo(user.getEmail());
	        helper.setSubject("Your Password Has Been Changed");

	        String htmlContent = buildAccountStatusEmailContent(user);
	        helper.setText(htmlContent, true);

	        mailSender.send(message);
	        return true;

	    } catch (Exception ex) {
	        ex.printStackTrace();
	        return false;
	    }
	}
	private String buildAccountStatusEmailContent(User user) {
	    return "<html>"
	            + "<body>"
	            + "<p>Dear " + user.getName() + ",</p>"
	            + "<p>Your password has been successfully changed.</p>"
	            + "<p>If you did not initiate this change, please contact our support team immediately.</p>"
	            + "<br><p>Regards,<br>JustClick Team</p>"
	            + "</body>"
	            + "</html>";
	}
	
	




}
