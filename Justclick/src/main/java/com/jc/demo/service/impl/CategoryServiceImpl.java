package com.jc.demo.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.demo.dto.CategoryDto;
import com.jc.demo.model.Category;
import com.jc.demo.repository.CategoryRepository;
import com.jc.demo.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public boolean saveCategory(CategoryDto categoryDto) {
      try {
          Category category = new Category();
          category.setId(categoryDto.getId());
          category.setName(categoryDto.getName());
          category.setPhoto(categoryDto.getPhoto());
          category.setIsActive(categoryDto.getIsActive());


          Category saved = categoryRepository.save(category);
       
          categoryToDto(saved);
          return true;
          
      }catch (Exception e) {
		e.printStackTrace();
		return false;
	}
    }

    @Override
    public List<CategoryDto> getAllCategory() {
        try {
        	List<Category> categories = categoryRepository.findAll();
            return categories.stream()
                             .map(this::categoryToDto)
                             .collect(Collectors.toList());
        }catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    @Override
    public boolean deleteCategory(Integer id) {
        try {
            if (categoryRepository.existsById(id)) {
                categoryRepository.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
	@Override
	public CategoryDto findCategory(Integer id) {
		Optional<Category> byId = categoryRepository.findById(id);
		return byId.map(this::categoryToDto).orElse(null);
	}
	
    @Override
    public boolean existCategory(String name) {
        return categoryRepository.existsByName(name);
    }
	
	@Override
	public List<CategoryDto> getAllActiveCategory() {
	    List<Category> byIsActiveTrue = categoryRepository.findByIsActiveTrue();
	    return byIsActiveTrue
	               .stream()
	               .map(this::categoryToDto)
	               .collect(Collectors.toList());
	}



    private CategoryDto categoryToDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setPhoto(category.getPhoto());
        categoryDto.setIsActive(category.getIsActive());

        return categoryDto;
    }

	@Override
	public void clearAll() {
	categoryRepository.deleteAll();
		
	}









}
