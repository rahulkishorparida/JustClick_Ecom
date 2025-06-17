package com.jc.demo.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.jc.demo.model.ProductOrder;
import com.jc.demo.service.ProductOrderExportService;

@Service
public class ProductOrderExportServiceImpl implements ProductOrderExportService {

    @Override
    public void exportToPdf(List<ProductOrder> orders, OutputStream out) throws IOException {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, out);

            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("All Orders Summary Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // 7 columns
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2, 2, 2, 3, 2, 3, 3}); 
            // Table headers
            table.addCell("Order ID");
            table.addCell("Amount");
            table.addCell("Status");
            table.addCell("Photo");
            table.addCell("Quantity");
            table.addCell("Payment Type");
            table.addCell("Date");

            // Data rows
            for (ProductOrder order : orders) {
                table.addCell(String.valueOf(order.getOrderId()));
                table.addCell(String.valueOf(order.getFinalPrice()));
                table.addCell(order.getStatus());

                if (order.getProduct().getPhoto() != null) {
                    String imageName = order.getProduct().getPhoto();
                    
                    try {
                        ClassPathResource imgFile = new ClassPathResource("static/uploads/product_img/" + imageName);
                        Image image = Image.getInstance(imgFile.getURL());
                        image.scaleToFit(50, 50);
                        PdfPCell imageCell = new PdfPCell(image, true);
                        table.addCell(imageCell);
                    } catch (BadElementException | IOException e) {
                        e.printStackTrace();
                        table.addCell("Image not found");
                    }
                } else {
                    table.addCell("No Image");
                }

                table.addCell(String.valueOf(order.getQuantity()));
                table.addCell(order.getPaymentType() != null ? order.getPaymentType() : "N/A");
                table.addCell(order.getOrderDate().toString());
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            throw new IOException("Error generating PDF", e);
        }
    }
}
