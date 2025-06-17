package com.jc.demo.service;

import java.util.List;
import java.util.Optional;

import com.jc.demo.dto.CategoryDto;
import com.jc.demo.model.Category;

public interface CategoryService {
	
	public boolean saveCategory(CategoryDto categoryDto);
	public List<CategoryDto> getAllCategory();
	
	public boolean deleteCategory(Integer id);
	
//	public Optional<CategoryDto> deleteCategory(Integer id);
	
	public CategoryDto findCategory(Integer id);
	
	public boolean existCategory(String name);
	public List<CategoryDto> getAllActiveCategory();

    public void clearAll();

}
