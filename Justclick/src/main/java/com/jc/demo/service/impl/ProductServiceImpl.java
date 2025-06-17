package com.jc.demo.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.demo.dto.ProductDto;
import com.jc.demo.model.Product;
import com.jc.demo.repository.CategoryRepository;
import com.jc.demo.repository.ProductRepository;
import com.jc.demo.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Override
	public boolean saveProduct(ProductDto productDto) {
	    logger.info("Saving product: {}", productDto.getName());
		try {
			Product product = new Product();
			product.setId(productDto.getId());
			product.setName(productDto.getName());
			product.setDes(productDto.getDes());
			product.setCategory(productDto.getCategory());
			product.setPrice(productDto.getPrice());
			product.setQuantity(productDto.getQuantity());
			product.setBrand(productDto.getBrand());
			product.setPhoto(productDto.getPhoto());
			product.setDiscount(productDto.getDiscount());
			product.setDiscountprice(productDto.getDiscountprice());
			product.setIsActive(productDto.getIsActive());
			
	        double discountAmount = (productDto.getPrice() * productDto.getDiscount()) /100;
	        product.setDiscountprice(productDto.getPrice() - discountAmount);
			
			Product save = productRepository.save(product);
		    logger.debug("Product saved with ID: {}", save.getId());
					
			convertDto(save);
			return true;
		} catch (Exception e) {
			  logger.error("Failed to save product: {}", productDto.getName(), e);
			return false;
		}
	}
//	@Override
//	public List<ProductDto> getAllProduct() {
//	   List<Product> all = productRepository.findAll();
//		return all.stream()
//				.map(this::convertDto).
//				collect(Collectors.toList());
//	}
	@Override
	public List<ProductDto> getAllProduct() {
	    logger.debug("Fetching all products from the database.");
	    try {
	        List<Product> all = productRepository.findAll();
	        logger.info("Fetched {} products from the database.", all.size());

	        return all.stream()
	                  .map(this::convertDto)
	                  .collect(Collectors.toList());
	    } catch (Exception e) {
	        logger.error("Failed to fetch products", e);
	        return Collections.emptyList(); // Safe fallback
	    }
	}


	@Override
	public boolean deleteProduct(Integer id) {
	    logger.info("Attempting to delete product with ID: {}", id);
	    try {
	        if (productRepository.existsById(id)) {
	            productRepository.deleteById(id);
	            logger.info("Product deleted successfully with ID: {}", id);
	            return true;
	        } else {
	            logger.warn("Product not found with ID: {}", id);
	            return false;
	        }
	    } catch (Exception e) {
	        logger.error("Error deleting product with ID: {}", id, e);
	        return false;
	    }
	}

	@Override
	public ProductDto getProductById(Integer id) {
		logger.debug("get product by id :{}" + id);
	try {
		Optional<Product> byId = productRepository.findById(id);
		ProductDto orElse = byId.map(this::convertDto).orElse(null);
		  if (orElse != null) {
	            logger.info("Found product by id: {}", id);
	        } else {
	            logger.warn("No product found with id: {}", id);
	        }
				return orElse;
	}catch (Exception e) {
		logger.error("not find " , id, e);
		return null;
	}
	}
	
//	@Override
//	public boolean existsproduct(String name) {
//		return productRepository.existsByName(name);
//	}
	@Override
	public boolean existsproduct(String name) {
	    logger.debug("Checking if product exists with name: {}", name);
	    boolean exists = productRepository.existsByName(name);
	    
	    if (exists) {
	        logger.info("Product with name '{}' exists in the database.", name);
	    } else {
	        logger.warn("Product with name '{}' does NOT exist.", name);
	    }
	    return exists;
	}

//	@Override
//	public List<ProductDto> getAllActiveProduct() {
//		logger.debug("get all active product");
//	    List<Product> activeProducts = productRepository.findByIsActiveTrue();
//	    return activeProducts.stream()
//	    		.map(this::convertDto)
//	    		.collect(Collectors.toList());
//	}
	@Override
	public List<ProductDto> getAllActiveProduct() {
	    logger.debug("Fetching all active products from the database.");
	    try {
	        List<Product> activeProducts = productRepository.findByIsActiveTrue();
	        logger.info("Fetched {} active products.", activeProducts.size());
	        
	        return activeProducts.stream()
	                .map(this::convertDto)
	                .collect(Collectors.toList());
	    } catch (Exception e) {
	        logger.error("Failed to fetch active products", e);
	        return Collections.emptyList();
	    }
	}

	
//	@Override
//	public List<ProductDto> getAllActiveProductByCategory(String category) {  //categoryName
//	    List<Product> filtered = productRepository.findByIsActiveTrueAndCategoryIgnoreCase(category);  //categoryName
//	    return filtered.stream()
//	    		       .map(this::convertDto)
//	    		       .collect(Collectors.toList());
//	}
	
	@Override
	public List<ProductDto> getAllActiveProductByCategory(String category) {
	    logger.debug("Fetching active products for category: '{}'", category);
	    try {
	        // Optional: Trim the input to avoid matching issues
//	        String normalizedCategory = category != null ? category.trim() : "";

	        List<Product> filtered = productRepository.findByIsActiveTrueAndCategoryIgnoreCase(category);
	        logger.info("Found {} active products in category '{}'", filtered.size());

	        return filtered.stream()
	                .map(this::convertDto)
	                .collect(Collectors.toList());
	    } catch (Exception e) {
	        logger.error("Failed to fetch active products for category: '{}'", category, e);
	        return Collections.emptyList(); // Safe fallback
	    }
	}

	
	private ProductDto convertDto(Product product) {
		ProductDto productDto = new ProductDto();
		productDto.setId(product.getId());
		productDto.setName(product.getName());
		productDto.setDes(product.getDes());
		productDto.setCategory(product.getCategory());
		productDto.setPrice(product.getPrice());
		productDto.setQuantity(product.getQuantity());
		productDto.setBrand(product.getBrand());
		productDto.setPhoto(product.getPhoto());
		productDto.setDiscount(product.getDiscount());
		productDto.setDiscountprice(product.getDiscountprice());
		productDto.setIsActive(product.getIsActive());
		return productDto;
		
	}
	
	@Override
	public List<String> getAllBrands() {
	    logger.debug("Fetching all distinct, non-empty product brands.");
	    try {
	        List<String> brands = productRepository.findAll().stream()
	                .map(Product::getBrand)
	                .filter(b -> b != null && !b.trim().isEmpty())
	                .map(String::trim)
	                .distinct()
	                .collect(Collectors.toList());

	        logger.info("Found {} distinct brands.", brands.size());
	        return brands;
	    } catch (Exception e) {
	        logger.error("Failed to fetch product brands", e);
	        return Collections.emptyList();
	    }
	}

	@Override
	public List<ProductDto> getAllActiveProductByBrand(String brand) {
	    List<Product> filtered = productRepository.findByIsActiveTrueAndBrandIgnoreCase(brand);
	    return filtered.stream()
	    		       .map(this::convertDto)
	    		       .collect(Collectors.toList());
	}
	@Override
	public List<Product> getAllPhotos(String photo) {
		List<Product> byphoto = productRepository.findByphoto(photo);
		return byphoto;
	}
	@Override
	public List<Product> searchProduct(String ch) {
		return productRepository.findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch, ch);
		
	}


	
	

}
