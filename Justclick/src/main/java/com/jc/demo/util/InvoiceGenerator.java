//package com.jc.demo.util;
//
//import com.jc.demo.model.Cart;
//import com.jc.demo.model.ProductOrder;
//import com.lowagie.text.*;
//import com.lowagie.text.pdf.*;
//
//import java.io.ByteArrayOutputStream;
//import java.util.List;
//
//public class InvoiceGenerator {
//
//    public static ByteArrayOutputStream generateInvoice(ProductOrder order, List<Cart> cartItems) throws Exception {
//        Document document = new Document(PageSize.A4);
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        PdfWriter.getInstance(document, out);
//        document.open();
//
//        // Fonts
//        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
//        Font headerFont = new Font(Font.HELVETICA, 14, Font.BOLD);
//        Font normalFont = new Font(Font.HELVETICA, 12);
//        Font boldFont = new Font(Font.HELVETICA, 12, Font.BOLD);
//
//        // Title
//        Paragraph title = new Paragraph("Invoice", titleFont);
//        title.setAlignment(Element.ALIGN_CENTER);
//        document.add(title);
//        document.add(new Paragraph(" "));
//
//        // Order & Customer Info
//        document.add(new Paragraph("Order ID: " + order.getId(), normalFont));
//        document.add(new Paragraph("Customer: " + order.getUser().getName(), normalFont));
//        document.add(new Paragraph(" "));
//
//        // Table
//        PdfPTable table = new PdfPTable(4); // 4 columns
//        table.setWidthPercentage(100f);
//        table.setWidths(new float[]{3.5f, 2f, 1.5f, 2f});
//
//        addTableHeader(table, boldFont);
//        for (Cart cart : cartItems) {
//            table.addCell(new Phrase(cart.getProduct().getName(), normalFont));
//            table.addCell(new Phrase(String.format("%.2f", cart.getProduct().getDiscountprice()), normalFont));
//            table.addCell(new Phrase(String.valueOf(cart.getQuantity()), normalFont));
//            table.addCell(new Phrase(String.format("%.2f", cart.getTotalPrice()), normalFont));
//        }
//
//        document.add(table);
//
//        document.add(new Paragraph(" "));
//        document.add(new Paragraph("------------------------------------------------------------", boldFont));
//        document.add(new Paragraph("Final Total: " + String.format("%.2f", order.getFinalPrice()), headerFont));
//        document.add(new Paragraph(" "));
//
//        // Footer
//        Paragraph thanks = new Paragraph("Thank you for shopping with us!", normalFont);
//        thanks.setAlignment(Element.ALIGN_CENTER);
//        document.add(thanks);
//
//        document.close();
//        return out;
//    }
//
//    private static void addTableHeader(PdfPTable table, Font font) {
//        table.addCell(new Phrase("Product", font));
//        table.addCell(new Phrase("Price", font));
//        table.addCell(new Phrase("Qty", font));
//        table.addCell(new Phrase("Total", font));
//    }
//}
package com.jc.demo.util;

import com.jc.demo.model.Cart;
import com.jc.demo.model.ProductOrder;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class InvoiceGenerator {

    public static ByteArrayOutputStream generateInvoice(ProductOrder order, List<Cart> cartItems) throws Exception {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        // Fonts
        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
        Font headerFont = new Font(Font.HELVETICA, 14, Font.BOLD);
        Font normalFont = new Font(Font.HELVETICA, 12);
        Font lineFont = new Font(Font.HELVETICA, 12, Font.BOLD);

        // Title
        Paragraph title = new Paragraph("Invoice", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        // Order Details
        document.add(new Paragraph("Order ID: " + order.getId(), normalFont));
        document.add(new Paragraph("Customer: " + order.getUser().getName(), normalFont));
        document.add(new Paragraph(" "));

        // Item header
        document.add(new Paragraph("Items Purchased:", headerFont));
        document.add(new Paragraph(" "));

        // Column Headings
        document.add(new Paragraph(String.format("%-30s %-10s %-10s %-10s", "Product", "Price", "Qty", "Total"), lineFont));
        document.add(new Paragraph("------------------------------------------------------------", lineFont));

        // Items
        for (Cart cart : cartItems) {
            String line = String.format(
                "%-30s %-10s %-10d %-10s",
                cart.getProduct().getName(),
                String.format("%.2f", cart.getProduct().getDiscountprice()),
                cart.getQuantity(),
                String.format("%.2f", cart.getTotalPrice())
            );
            document.add(new Paragraph(line, normalFont));
        }

        document.add(new Paragraph(" "));
        document.add(new Paragraph("------------------------------------------------------------", lineFont));
        document.add(new Paragraph("Final Total: " + String.format("%.2f", order.getFinalPrice()), headerFont));
        document.add(new Paragraph(" "));

        // Footer
        Paragraph thanks = new Paragraph("Thank you for shopping with us!", normalFont);
        thanks.setAlignment(Element.ALIGN_CENTER);
        document.add(thanks);

        document.close();
        return out;
    }
}
