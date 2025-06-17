package com.jc.demo.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jc.demo.dto.CategoryDto;
import com.jc.demo.dto.ProductDto;
import com.jc.demo.dto.UserDto;
import com.jc.demo.model.Product;
import com.jc.demo.model.User;
import com.jc.demo.model.WeatherResponse;
import com.jc.demo.repository.ProductRepository;
import com.jc.demo.service.CartService;
import com.jc.demo.service.CategoryService;
import com.jc.demo.service.OtpService;
import com.jc.demo.service.ProductService;
import com.jc.demo.service.UserService;
import com.jc.demo.service.WeatherService;
import com.jc.demo.util.CommonUtil;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	
	private static final String IMAGE_UPLOAD_DIR = "src/main/resources/static/uploads/user_img/";
	
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ProductService productService;
	@Autowired
	private UserService userService;
	@Autowired
	private CartService cartService;
    @Autowired
    private WeatherService weatherService;
	
	@Autowired
	private CommonUtil commonUtil;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private OtpService otpService;
	
	@ModelAttribute
	public void getUserDetails(Principal p , Model model) {
		if(p!=null) {
			String email = p.getName();
			UserDto userByEmail = userService.getUserByEmail(email);
			model.addAttribute("usern", userByEmail);
			
			Integer countCart = cartService.getCountCart(userByEmail.getId());
			model.addAttribute("countcart", countCart);
		}
		
	        String city = "Bhubaneswar";
	        // You can make city dynamic later by user input or geo-location
	        WeatherResponse weather = weatherService.getWeatherByCity(city);
	        model.addAttribute("weatherData", weather);
	        
		List<CategoryDto> allActiveCategory = categoryService.getAllActiveCategory();
		model.addAttribute("cates",allActiveCategory);
	}
	
	  @GetMapping("/")
	    public String index(org.springframework.ui.Model model, @RequestParam(value = "category", defaultValue = "") String category) {
	    	List<CategoryDto> allActiveCategory = categoryService.getAllActiveCategory();
	    	model.addAttribute("indexc" ,allActiveCategory);
	        return "justclick/index";
	    }
	
	@GetMapping("/signin")
	public String login() {
		return "justclick/login";
	}
	@GetMapping("/register")
	public String register() {
		return "justclick/register";
	}
	
//
//	@PostMapping("/save-user")
//	public String saveusser(@ModelAttribute UserDto userDto,
//	                        @RequestParam("file") MultipartFile file,
//	                        RedirectAttributes redirectAttributes) throws IOException {
//
//	    boolean existsByEmail = userService.existsByEmail(userDto.getEmail());
//
//	    if (existsByEmail) {
//	        redirectAttributes.addFlashAttribute("errorMsg", "Email already exists. User not saved.");
//	        return "redirect:/justclick/register";
//	    }
//
//	 
//	    if (!file.isEmpty()) {
//	        String originalFilename = file.getOriginalFilename();
//	        String uniqueId = UUID.randomUUID().toString();
//	        String filename = uniqueId + "_" + originalFilename;
//	        Files.copy(file.getInputStream(), Paths.get(IMAGE_UPLOAD_DIR + filename));
//	        userDto.setPhoto(filename);
//	    }
//
//	    boolean saveUser = userService.saveUser(userDto);
//	    if (saveUser) {
//	        redirectAttributes.addFlashAttribute("successMsg", "User registered successfully");
//	    } else {
//	        redirectAttributes.addFlashAttribute("errorMsg", "User not saved");
//	    }
//
//	    return "redirect:/justclick/register";
//	}



	@GetMapping("/products")
	public String products(Model model,
	                       @RequestParam(value = "category", defaultValue = "") String category,
	                       @RequestParam(value = "brand", defaultValue = "") String brand) {
//You did not tell Spring what to do if the parameter is missing.
//Use defaultValue = "" to simplify the logic and avoid null checks.n
		
	    List<ProductDto> allProducts;
	    if (!brand.isEmpty()) {
	        allProducts = productService.getAllActiveProductByBrand(brand);
	    } else if (!category.isEmpty()) {
	        allProducts = productService.getAllActiveProductByCategory(category);
	    } else {
	        allProducts = productService.getAllActiveProduct();
	    }

	    List<String> allBrands = productService.getAllBrands();
	    List<CategoryDto> allCategories = categoryService.getAllActiveCategory();

	    model.addAttribute("allpro", allProducts);
	    model.addAttribute("allbrands", allBrands);
	    model.addAttribute("allcate", allCategories);
	    model.addAttribute("paramValue", category);
	    model.addAttribute("param", brand);
	    
	    return "justclick/products";
	}

	@PostMapping("/save-user")
	public String register(@ModelAttribute UserDto userDto,
	                       @RequestParam("file") MultipartFile file,
	                       RedirectAttributes redirectAttributes) throws IOException {

	    String filename = userDto.getPhoto();

	    if (!file.isEmpty()) {
	    	
	        String originalFilename = file.getOriginalFilename();
	        String uniqueId = UUID.randomUUID().toString();
	        filename = uniqueId + "_" + originalFilename;
	        String filePath = IMAGE_UPLOAD_DIR + filename;
	        Files.copy(file.getInputStream(), Paths.get(filePath));
	        userDto.setPhoto(filename);
	    }

	    boolean existsByEmail = userService.existsByEmail(userDto.getEmail());

	    if (existsByEmail) {
	        redirectAttributes.addFlashAttribute("errorMsg", "Email already exists. User not saved.");
	    } else {
	        boolean saveUser = userService.saveUser(userDto);
	        if (saveUser) {
	            redirectAttributes.addFlashAttribute("successMsg", "User registered successfully");
	        } else {
	            redirectAttributes.addFlashAttribute("errorMsg", "User not saved");
	        }
	    }

	    return "redirect:/register";
	}

//	@GetMapping("/in-detail/{id}")
//	public String indetails(@PathVariable Integer id, Model model) {
//		ProductDto productById = productService.getProductById(id);
//		model.addAttribute("product", productById);
//		return "justclick/in-detail";
//	}
	@GetMapping("/in-detail/{id}")
	public String indetails(@PathVariable("id") Integer id, Model model) {
	    ProductDto productById = productService.getProductById(id);
	    model.addAttribute("product", productById);
	    model.addAttribute("p",productById);
	    return "justclick/in-detail";
	    
	}


	
//	
    @GetMapping("/forgot_pas")
	public String forgotpas() {
    	return "forgot_pas";
    }
    
    @PostMapping("/forgot_pas")
    public String getemailfornewpas(@RequestParam String email, RedirectAttributes redirectAttributes,
    		             HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
        UserDto userByEmail = userService.getUserByEmail(email);

        if (ObjectUtils.isEmpty(userByEmail)) {
            redirectAttributes.addFlashAttribute("errorMsg", "Invalid Mail");
        } else {
        
        	String resetToken = UUID.randomUUID().toString();

        userService.upsateUserResetToken(email, resetToken);
        
        String url =CommonUtil.generateUrl(request)+"/reset_pas?token="+resetToken;
        	
            Boolean sendMail = commonUtil.sendMail(url, email);
       
            if (sendMail) {
                redirectAttributes.addFlashAttribute("successMsg", "Please check your mail");
            } else {
                redirectAttributes.addFlashAttribute("errorMsg", "Server error. Mail not sent");
            }
        }

        return "redirect:/forgot_pas";
    }
    
    @GetMapping("/reset_pas")
 	public String showrestpas(@RequestParam String token, Model model) {
    	
    	User userByToken = userService.getUserByToken(token);
    	
    	if(userByToken == null) {
    		model.addAttribute("msg", "Invalid link");
    		return "message";
    	}
    	
    	model.addAttribute("token", token);
     	return "reset_pas";
     }
    
    @PostMapping("/reset_pas")
 	public String restpas(@RequestParam String token,@RequestParam String password,
 			                                RedirectAttributes redirectAttributes, Model model) {
    	User userByToken = userService.getUserByToken(token);
    	if(userByToken == null) {
    		model.addAttribute("errorMsg", "Invalid linkkk");
    		return "message";
    	}else {
    		userByToken.setPassword(passwordEncoder.encode(password));
    		userByToken.setResetToken(null);
    		userService.updateUser(userByToken);
    	//	redirectAttributes.addFlashAttribute("successMsg", "Password Changed");
    		model.addAttribute("msg","Password Changed");
    		return "message";
    	}
    	
     }
//    ---------------search----------
    
    @GetMapping("/search")
    public String searchProduct(@RequestParam String ch, Model model) {
        List<Product> searchProduct = productService.searchProduct(ch);
//        List<ProductDto> allActiveProduct = productService.getAllActiveProduct();
        
        List<CategoryDto> allActiveCategory = categoryService.getAllActiveCategory();
        model.addAttribute("allpro", searchProduct); 
        model.addAttribute("allcate",allActiveCategory);
        return "justclick/products";  
    }
//-----------------api---
//    @GetMapping("/view")
//    public String showWeatherByQuery(@RequestParam String city, Model model) {
//        WeatherResponse weather = weatherService.getWeatherByCity(city);
//        model.addAttribute("weather", weather);
//        return "weather";
//    }


}
