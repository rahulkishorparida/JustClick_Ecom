package com.jc.demo.service;


import com.jc.demo.model.ProductOrder;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface ProductOrderExportService {
	
	
	  void exportToPdf(List<ProductOrder> orders, OutputStream out) throws IOException;
	  
	 
}


//multi
//method 