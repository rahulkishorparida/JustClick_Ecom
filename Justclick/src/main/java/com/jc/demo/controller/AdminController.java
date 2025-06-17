package com.jc.demo.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.jc.demo.JustclickApplication;
import com.jc.demo.config.SecurityConfig;
import com.jc.demo.dto.CategoryDto;
import com.jc.demo.dto.ProductDto;
import com.jc.demo.dto.UserDto;
import com.jc.demo.model.Cart;
import com.jc.demo.model.Coupon;
import com.jc.demo.model.Product;
import com.jc.demo.model.ProductOrder;
import com.jc.demo.model.User;
import com.jc.demo.model.WeatherResponse;
import com.jc.demo.repository.ProductOrderRepository;
import com.jc.demo.service.CategoryService;
import com.jc.demo.service.CouponService;
import com.jc.demo.service.ProductOrderExportService;
import com.jc.demo.service.ProductOrderService;
import com.jc.demo.service.ProductService;
import com.jc.demo.service.UserService;
import com.jc.demo.service.WeatherService;
import com.jc.demo.service.impl.EmailServiceImpl;
import com.jc.demo.util.CommonUtil;
import com.jc.demo.util.InvoiceGenerator;
import com.jc.demo.util.OrderStatus;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final EmailServiceImpl emailServiceImpl;

    private final SecurityConfig securityConfig;

	
	private static final String IMAGE_UPLOAD_DIR = "src/main/resources/static/uploads/category_img/";
	
	private static final String IMAGE_UPLOAD_DI = "src/main/resources/static/uploads/product_img/";

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	@Autowired
	private UserService userService;
	@Autowired
	private ProductOrderService productOrderService;
	@Autowired
	private CommonUtil mailService;
	@Autowired
	private WeatherService weatherService;
	
	@Autowired
	private CouponService couponService;

@Autowired
private JavaMailSender mailSender;
	@Autowired
	private ProductOrderExportService productOrderExportService;
	@Autowired
	private com.jc.demo.service.ProductOrderExcelExportService productOrderExcelExportService;


    AdminController(SecurityConfig securityConfig, EmailServiceImpl emailServiceImpl) {
        this.securityConfig = securityConfig;
        this.emailServiceImpl = emailServiceImpl;
    }
	
	@ModelAttribute
	public void getUserDetails(Principal p , Model model) {//currently authenticated user
		if(p!=null) {
			String email = p.getName();
			UserDto userByEmail = userService.getUserByEmail(email);
			model.addAttribute("usern", userByEmail);
		}
		   String city = "Bhubaneswar";
	        // You can make city dynamic later by user input or geo-location
	        WeatherResponse weather = weatherService.getWeatherByCity(city);
	        model.addAttribute("weatherData", weather);
	        
		List<CategoryDto> allActiveCategory = categoryService.getAllActiveCategory();
		model.addAttribute("cates",allActiveCategory);
	}
	
    @GetMapping("/")
    public String index() {
        return "admin/index";
    }

    @GetMapping("/add-product")
    public String addProduct(org.springframework.ui.Model model) {
    	List<CategoryDto> allCategory = categoryService.getAllCategory();
    	model.addAttribute("catedrop", allCategory);
        return "admin/add-product"; 
    }

    @GetMapping("/category")
    public String category(org.springframework.ui.Model model) {
    List<CategoryDto> allCategory = categoryService.getAllCategory();
    model.addAttribute("categories" ,allCategory);
        return "admin/category";
    }
    
    @PostMapping("/save-category")
    public String saveCategory(@ModelAttribute CategoryDto categoryDto,
                               @RequestParam(value = "file") MultipartFile file,
                               RedirectAttributes redirectAttributes) throws IOException {

        String filename = categoryDto.getPhoto(); // default: existing photo

        if (!file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();
            String uniqueId = UUID.randomUUID().toString();
            filename = uniqueId + "_" + originalFilename;
            String filePath = IMAGE_UPLOAD_DIR + filename;
            Files.copy(file.getInputStream(), Paths.get(filePath));
            categoryDto.setPhoto(filename);
        }//input

        boolean existCategory = categoryService.existCategory(categoryDto.getName());

        if (existCategory) {
            redirectAttributes.addFlashAttribute("errorMsg", "Already Exist");
        } else {
            boolean saveCategory = categoryService.saveCategory(categoryDto);
            if (ObjectUtils.isEmpty(saveCategory)) {
                redirectAttributes.addFlashAttribute("errorMsg", "Internal error");
                
                
                
            } else {
                redirectAttributes.addFlashAttribute("successMsg", "Saved Successfully!");
            }
        }
        return "redirect:/admin/category";
    }
    @GetMapping("/delete-cate/{id}")
    public String delete(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        
        return"redirect:/admin/category";
    }
    @GetMapping("/edit-cate/{id}")
    public String edit(@PathVariable Integer id, org.springframework.ui.Model model) {
    CategoryDto category = categoryService.findCategory(id);
    model.addAttribute("catee", category);
	return "admin/edit-cate";	
    }
    @PostMapping("/edit-cate")
    public String saveedit(@ModelAttribute CategoryDto categoryDto,
                           @RequestParam(value = "file") MultipartFile file,
                           RedirectAttributes redirectAttributes) throws IOException {

        String filename = categoryDto.getPhoto(); // default existing photo

        if (!file.isEmpty()) {
            // Upload new photo
            String originalFilename = file.getOriginalFilename();
            String uniqueId = UUID.randomUUID().toString();
            filename = uniqueId + "_" + originalFilename;
            String filePath = IMAGE_UPLOAD_DIR + filename;
            Files.copy(file.getInputStream(), Paths.get(filePath));

            categoryDto.setPhoto(filename);
        }

        boolean existCategory = categoryService.existCategory(categoryDto.getName());

        if (existCategory) {
            redirectAttributes.addFlashAttribute("errorMsg", "Already Exist");
        } else {
            boolean saveCategory = categoryService.saveCategory(categoryDto);
            if (ObjectUtils.isEmpty(saveCategory)) {
                redirectAttributes.addFlashAttribute("errorMsg", "Internal error");
            } else {
                redirectAttributes.addFlashAttribute("successMsg", "Saved Successfully!");
            }
        }
        return "redirect:/admin/edit-cate/" + categoryDto.getId();
    }
    
// --------------------Product-------------------------------------- 
    
    @PostMapping("/addproduct")
    public String saveProduct(@ModelAttribute ProductDto productDto,
    		
    	                       @RequestParam(value = "file") MultipartFile file,
    	                       RedirectAttributes redirectAttributes) throws IOException {
    	
    	   String filename = productDto.getPhoto(); // default: existing photo

           if (!file.isEmpty()) {
               String originalFilename = file.getOriginalFilename();
               String uniqueId = UUID.randomUUID().toString();
               filename = uniqueId + "_" + originalFilename;
               String filePath = IMAGE_UPLOAD_DI + filename;
               Files.copy(file.getInputStream(), Paths.get(filePath));
               productDto.setPhoto(filename);
           }
       	  if (productDto.getDiscount() < 0 || productDto.getDiscount() > 100) {
          	   redirectAttributes.addFlashAttribute("errorMsg", "Problem in you Discount amount");
          	   return "redirect:/admin/edit-product/" + productDto.getId();
          	}

    	boolean existsproduct = productService.existsproduct(productDto.getName());
    	
    	
    	if(existsproduct) {
    		 redirectAttributes.addFlashAttribute("errorMsg", "Already Exist");
    	}else {
			boolean saveProduct = productService.saveProduct(productDto);
			if(ObjectUtils.isEmpty(saveProduct)) {
				 redirectAttributes.addFlashAttribute("errorMsg", "Internal error");
			}else {
				 redirectAttributes.addFlashAttribute("successMsg", "Saved Successfully!");		
			}
		}
    	return "redirect:/admin/add-product";
    }
    @GetMapping("/view")
    public String view(org.springframework.ui.Model model) {
    	List<ProductDto> allProduct = productService.getAllProduct();
    	model.addAttribute("products",allProduct);
    	return "admin/view";
    }
   @GetMapping("/delete-product/{id}") 
    public String deleteProduct(@PathVariable Integer id) {
	    productService.deleteProduct(id);
	    return"redirect:/admin/view";
	   
   }
   @GetMapping("/edit-product/{id}")
   public String editp(@PathVariable Integer id , org.springframework.ui.Model model) {
	   ProductDto productById = productService.getProductById(id);
	   List<CategoryDto> allCategory = categoryService.getAllCategory(); 
	    model.addAttribute("editp",productById);
	    model.addAttribute("catedrop", allCategory);
	    return "admin/edit-product";
	   
   }
   
   @PostMapping("/edit-product")
   public String editProduct(@ModelAttribute ProductDto productDto,
		                    @RequestParam(value = "file") MultipartFile file,
		                    RedirectAttributes redirectAttributes) throws IOException {
	   
 	   String filename = productDto.getPhoto(); // default: existing photo

       if (!file.isEmpty()) {
           String originalFilename = file.getOriginalFilename();
           String uniqueId = UUID.randomUUID().toString();
           filename = uniqueId + "_" + originalFilename;
           String filePath = IMAGE_UPLOAD_DI + filename;
           Files.copy(file.getInputStream(), Paths.get(filePath));
           productDto.setPhoto(filename);
       }

       if (productDto.getDiscount() < 0 || productDto.getDiscount() > 100) {
    	   redirectAttributes.addFlashAttribute("errorMsg", "Problem in you Discount amount");
    	   return "redirect:/admin/edit-product/" + productDto.getId();
    	}

		boolean saveProduct = productService.saveProduct(productDto);
		if(ObjectUtils.isEmpty(saveProduct)) {
			redirectAttributes.addFlashAttribute("errorMsg", "Internal error");
		}else {
			redirectAttributes.addFlashAttribute("successMsg", "Saved Successfully!");	
		}
	
	   return "redirect:/admin/edit-product/" + productDto.getId();
}
   
	@GetMapping("/user")
	public String userdata(Model model, @RequestParam(value = "state", defaultValue = "") String state) {

	    List<UserDto> allUser;

	    if (!state.isEmpty()) {
	        allUser = userService.getAllState(state);
	    } else {
	        allUser = userService.getAllUser();
	    }
	    
	    List<String> allStates = userService.getAllStates();
	    
	    model.addAttribute("alluser", allUser);
	    model.addAttribute("states", allStates);  
	    model.addAttribute("selectedState", state); 

	    return "justclick/user";
	}
	
	@PostMapping("/user/status")
	@ResponseBody //return value will be the HTTP response body, not a view
	public ResponseEntity<String> changeUserStatus(@RequestBody Map<String, String> payload) {
	    try {
	        Integer id = Integer.parseInt(payload.get("id"));//get all youser
	        String status = payload.get("status");
	        boolean result = userService.accountStatus(id, status);
	        if (result) {
	            return ResponseEntity.ok("Updated");
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
	    }
	}
   @GetMapping("/order")
	public String ordered(Model model) {
	   List<ProductOrder> allProductOrders = productOrderService.getAllProductOrders();
	   model.addAttribute("orders", allProductOrders);
	   model.addAttribute("srch", false);
		return "admin/order";
	}
   
   @GetMapping("/update-order-status")
   public String updateOrder(@RequestParam Integer id, @RequestParam Integer st) {
	
       OrderStatus[] values = OrderStatus.values();
       String status = null;

       // status name by id
       for (OrderStatus orderStatus : values) {
           if (orderStatus.getId().equals(st)) {
               status = orderStatus.getName();
//               break;
           }
       }
       if (status == null) {
           System.out.println("Invalid order status ID: " + st);
           return "redirect:/admin/order?error=status";
       }
       // Update the order status
       Boolean updateSuccess = productOrderService.updateOrderStatus(id, status);

       if (updateSuccess) {
           // Fetch updated order for email content
           ProductOrder updatedOrder = productOrderService.getOrderById(id);

//           if (updatedOrder != null) {
//               updatedOrder.setStatus(status);
               try {
            	   
            	   mailService.sendMailForProductStatus(updatedOrder);
                   System.out.println("Status update email sent successfully.");
               } catch (Exception e) {
                   e.printStackTrace();
                   System.out.println("Error sending status update email.");
               }
//           }
       } else {
           System.out.println("Order update failed for order ID: " + id);
       }

       return "redirect:/admin/order?success=statusUpdated";
   }
//   ----------------Pdf-----------------
   @GetMapping("/order/download/pdf")
   public void downloadOrdersAsPdf(HttpServletResponse response) throws IOException {
       response.setContentType("application/pdf");
       response.setHeader("Content-Disposition", "attachment; filename=orders.pdf");

       List<ProductOrder> orders = productOrderService.getAllProductOrders();
       productOrderExportService.exportToPdf(orders, response.getOutputStream());
   }
   
   @GetMapping("/order/download/excel")
   public void downloadExcel(HttpServletResponse response) throws IOException {
       response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
       response.setHeader("Content-Disposition", "attachment; filename=orders.xlsx");

       List<ProductOrder> orders = productOrderService.getAllProductOrders(); // Or service call
       productOrderExcelExportService.exportToExcel(orders, response.getOutputStream());
      
   }
// -------------------------------search Bar------------  
   @GetMapping("/search-order")
   public String searchOrder(@RequestParam Integer  orderId, Model model) {
    ProductOrder orderByOrderId = productOrderService.getOrderByOrderId(orderId);
    if(ObjectUtils.isEmpty(orderByOrderId)) {
        model.addAttribute("erMsg", "Incorrect Order ID"); 
//    	model.addAttribute("orderDtls", null);
    }
    else {
    	model.addAttribute("orderDtls", orderByOrderId);
    }
    model.addAttribute("srch", true);
//    model.addAttribute("keyword", orderId);
       return "admin/order";  
       //Adds a flag to the model indicating that a search was performed.
       //Can be used in the view to conditionally show the results section.
   }
// -------------------filter--------  
   @GetMapping("/filter-orders")
   public String filterOrder(
           @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
           @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
           Model model) {

       List<ProductOrder> orders = productOrderService.getOrdersBetweenDates(startDate, endDate);

       if (ObjectUtils.isEmpty(orders)) {
           model.addAttribute("errorMsg", "No order in this time range");
       } else {
           model.addAttribute("orders", orders); 
       }

       model.addAttribute("startDate", startDate);
       model.addAttribute("endDate", endDate);
       model.addAttribute("srch", false);
     
       return "admin/order";
   }

	@GetMapping("/coupon")
	public String coupon( Model model) {
		List<Coupon> allCoupon = couponService.getALLCoupon();
		model.addAttribute("cpdata",allCoupon);
		return "admin/coupon";	
	}
	@PostMapping("/save-cp")
	public String savecp(@ModelAttribute Coupon coupon,Model model, RedirectAttributes redirectAttributes) {
		boolean saveCoupon = couponService.saveCoupon(coupon);
		model.addAttribute("savecp", saveCoupon);
		model.addAttribute("sucMsg", "Add coupon");
		return "redirect:/admin/coupon";
	}
	
	@GetMapping("/delete-cp/{id}")
	public String delete1(@PathVariable Integer id ) {
		couponService.deleteCoupon(id);
		return "redirect:/admin/coupon";
	}
	
	@GetMapping("/add_admin")
	public String add_admin() {
		return "admin/add_admin";
	}

	
	@PostMapping("/save_admin")
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
	        boolean saveUser = userService.saveAdmin(userDto);
	        if (saveUser) {
	            redirectAttributes.addFlashAttribute("successMsg", "User registered successfully");
	        } else {
	            redirectAttributes.addFlashAttribute("errorMsg", "User not saved");
	        }
	    }

	    return "redirect:/admin/add_admin";
	}
	


public void sendInvoiceEmail(String toEmail, ProductOrder order, List<Cart> cartItems) throws Exception {
    ByteArrayOutputStream pdfStream = InvoiceGenerator.generateInvoice(order, cartItems);

    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true); // true = multipart

    helper.setTo(toEmail);
    helper.setSubject("Your Order Invoice - Order #" + order.getId());
    helper.setText("Dear " + order.getUser().getName() + ",\n\nPlease find attached your order invoice.\n\nThanks for shopping!");

    // Attach the PDF (as byte array)
    helper.addAttachment("invoice_" + order.getId() + ".pdf", 
        new ByteArrayResource(pdfStream.toByteArray()));

    mailSender.send(message);
}
	
	
	
}
