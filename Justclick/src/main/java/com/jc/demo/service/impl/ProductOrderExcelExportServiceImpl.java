package com.jc.demo.service.impl;

import com.jc.demo.model.ProductOrder;
import com.jc.demo.service.ProductOrderExcelExportService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
public class ProductOrderExcelExportServiceImpl implements ProductOrderExcelExportService {

    @Override
    public void exportToExcel(List<ProductOrder> orders, OutputStream out) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Orders");

        Row headerRow = sheet.createRow(0);
        String[] headers = {"Order ID", "Amount", "Status", "Quantity", "Payment Type", "Date"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowIdx = 1;
        for (ProductOrder order : orders) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(order.getOrderId());
            row.createCell(1).setCellValue(order.getFinalPrice());
            row.createCell(2).setCellValue(order.getStatus());
            row.createCell(3).setCellValue(order.getQuantity());
            row.createCell(4).setCellValue(order.getPaymentType() != null ? order.getPaymentType() : "N/A");
            row.createCell(5).setCellValue(order.getOrderDate().toString());
        }

        workbook.write(out);
        workbook.close();
    }
}
