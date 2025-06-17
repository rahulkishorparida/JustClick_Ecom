package com.jc.demo.service;

import java.util.List;
import java.util.Set;

import com.jc.demo.dto.ProductDto;
import com.jc.demo.model.Product;

public interface ProductService {
	
	public boolean saveProduct(ProductDto productDto);
	public List<ProductDto> getAllProduct();
	public boolean deleteProduct(Integer id);
	public ProductDto getProductById(Integer id);
	
	public boolean existsproduct(String name);
	public List<ProductDto> getAllActiveProduct();
	
	public List<ProductDto> getAllActiveProductByCategory(String category); //categoryName
	public List<ProductDto> getAllActiveProductByBrand(String brand);
	
	List<String> getAllBrands();
	
	public List<Product> getAllPhotos(String photo);
	
	public List<Product> searchProduct(String ch);
	


	 
}
