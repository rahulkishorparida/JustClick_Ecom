package com.jc.demo.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.jc.demo.model.ProductOrder;

public interface ProductOrderExcelExportService {
    void exportToExcel(List<ProductOrder> orders, OutputStream out) throws IOException;

}
