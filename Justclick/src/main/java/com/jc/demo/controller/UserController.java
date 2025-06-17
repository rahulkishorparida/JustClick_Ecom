package com.jc.demo.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.jc.demo.config.SecurityConfig;
import com.jc.demo.dto.CategoryDto;
import com.jc.demo.dto.OrderRequestDto;
import com.jc.demo.dto.UserDto;
import com.jc.demo.model.Cart;
import com.jc.demo.model.Coupon;
import com.jc.demo.model.MasterAddress;
import com.jc.demo.model.Product;
import com.jc.demo.model.ProductOrder;
import com.jc.demo.model.User;
import com.jc.demo.service.CartService;
import com.jc.demo.service.CategoryService;
import com.jc.demo.service.CouponService;
import com.jc.demo.service.MasterAddressService;
import com.jc.demo.service.ProductOrderService;
import com.jc.demo.service.ProductService;
import com.jc.demo.service.UserService;
import com.jc.demo.service.impl.EmailServiceImpl;
import com.jc.demo.service.CouponService;
import com.jc.demo.util.CommonUtil;
import com.jc.demo.util.InvoiceGenerator;
import com.jc.demo.util.OrderStatus;

import jakarta.persistence.criteria.Order;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private final PasswordEncoder passwordEncoder;

 
	@Autowired
	private UserService userService;
	@Autowired
    private CategoryService categoryService;
	@Autowired
	private CartService cartService;
	@Autowired
	private ProductOrderService productOrderService;
	@Autowired
	private MasterAddressService masterAddressService;
	@Autowired
	private ProductService productService;
	@Autowired
	private CommonUtil mailService;
	@Autowired
	private EmailServiceImpl emailService;

	@Autowired
	private CouponService couponService;
	@Autowired
	private JavaMailSender mailSender;

    UserController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
	
	@GetMapping("/")
	public String home() {
		return "user/home";
	}
   
	@ModelAttribute
	public void getUserDetails(Principal p , Model model) {
		if(p!=null) {
			String email = p.getName();
			UserDto userByEmail = userService.getUserByEmail(email);
			model.addAttribute("usern", userByEmail);
			
			Integer countCart = cartService.getCountCart(userByEmail.getId());
			model.addAttribute("countcart", countCart);
		}
		List<CategoryDto> allActiveCategory = categoryService.getAllActiveCategory();
		model.addAttribute("cates",allActiveCategory);
	}
	
	@GetMapping("/adtocate")
	public String addCart(@RequestParam("pid") Integer pid,
	                      @RequestParam("uid") Integer uid,
	                      RedirectAttributes redirectAttributes) {
	    Cart saveCart = cartService.saveCart(pid, uid);
	    
	    if (ObjectUtils.isEmpty(saveCart)) {
	        redirectAttributes.addFlashAttribute("errorMsg", "Product add to cart failed");
	    } else {
	        redirectAttributes.addFlashAttribute("successMsg", "Product added to cart");
	    }
	    return "redirect:/in-detail/" + pid;
	}

	@GetMapping("/cart")
	public String loadCart(Principal p, Model model) {
	    String email = p.getName();
	    UserDto userDtls = userService.getUserByEmail(email);
	    List<Cart> carts = cartService.getCartsByUser(userDtls.getId());

	    double totalOrderPrice = carts.stream()
	        .mapToDouble(Cart::getTotalPrice)
	        .sum();
        
	    model.addAttribute("carts", carts);
	    model.addAttribute("totalOrderPrice", totalOrderPrice);

	    return "user/cart";
	}
	
	@GetMapping("/cartQuantityUpdate")
	public String updatecartQuantity(@RequestParam String sy,@RequestParam Integer cid ) {
		cartService.updateQuantity(sy,cid);
		return "redirect:/user/cart";
		
	}
	
	@GetMapping("/order/{id}")
	public String orderaddress(@PathVariable("id")int id, Model model) {
	    UserDto userById = userService.getUserById(id);
	    List<Cart> cartsByUser = cartService.getCartsByUser(userById.getId());
	    
	   List<MasterAddress> allMasterAddresses = masterAddressService.getAllMasterAddresses();
	    List<MasterAddress> addressesByUserId = masterAddressService.getAddressesByUserId(id);
	    
	    List<Coupon> allCoupon = couponService.getALLCoupon();
	
	    double totalOrderPrice = cartsByUser.stream()
	        .mapToDouble(Cart::getTotalPrice)
	        .sum();
	    
       double finalPrice = totalOrderPrice +30 +100;
	
	    model.addAttribute("ocate", cartsByUser);

	    model.addAttribute("totalOrderPrice", totalOrderPrice); 
	    model.addAttribute("finalp",finalPrice);
	    model.addAttribute("allcup",allCoupon);
	    
	    model.addAttribute("udrs", addressesByUserId); 
	  
	    return "user/order";
	}
	
//	@PostMapping("/order")
//	public String saveorder(@ModelAttribute OrderRequestDto orderRequestDto, Principal p ) {
//		
//	    String email = p.getName();
//	    UserDto userDtls = userService.getUserByEmail(email);
//	    List<Cart> carts = cartService.getCartsByUser(userDtls.getId());
//	    
//		productOrderService.saveOrder(userDtls.getId(), orderRequestDto);
//		
//		cartService.clearCartByUserId(userDtls.getId());
//	    return "redirect:/user/success";
//		
//	}
	
	@PostMapping("/order")
	public String saveorder(@ModelAttribute OrderRequestDto orderRequestDto, Principal p) throws Exception {
	    String email = p.getName();
	    UserDto userDtls = userService.getUserByEmail(email);
	    List<Cart> carts = cartService.getCartsByUser(userDtls.getId());

	    // Save order and get saved order object
	    ProductOrder savedOrder = productOrderService.saveOrder(userDtls.getId(), orderRequestDto);

	    // Generate invoice PDF
	    ByteArrayOutputStream pdfStream = InvoiceGenerator.generateInvoice(savedOrder, carts);

	    // Send invoice email
	    emailService.sendInvoice(email, pdfStream, String.valueOf(savedOrder.getId()));

	    // Clear cart after order
	    cartService.clearCartByUserId(userDtls.getId());
	   

	    return "redirect:/user/success";
	}

	@GetMapping("/success")
	public String loadsuccess(){
		 return "user/success";
	}
	
	@GetMapping("/myorder")
	public String usserorder(  Model model) {
		List<ProductOrder> allProductOrders = productOrderService.getAllProductOrders();
	
		model.addAttribute("myorder", allProductOrders);
		 return "user/myorder";
	}
	
	@GetMapping("/address")
	public String address(Model model, Principal p) {
		
		String email = p.getName();
		UserDto userByEmail = userService.getUserByEmail(email);
//		int id = userByEmail.getId();
		
		List<MasterAddress> addressesByUserId = masterAddressService.getAddressesByUserId(userByEmail.getId());
		
	model.addAttribute("maddress", addressesByUserId);
		
	    return "user/address";
	}

	@PostMapping("/save-address")
	public String address(Principal p, 
	                      @ModelAttribute MasterAddress masterAddress, 
	                      RedirectAttributes redirectAttributes) throws IOException {

	    String email = p.getName();
	    UserDto userdtl = userService.getUserByEmail(email); 
        int id = userdtl.getId();
	    try {
	        masterAddressService.saveAddress(masterAddress, id);
	        redirectAttributes.addFlashAttribute("success", "Address saved successfully.");
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("error", "Failed to save address: " + e.getMessage());
	    }

	    return "redirect:/user/address";
	}
	
	@GetMapping("/delete-ad/{id}")
	public String  deleteAdress(@PathVariable Integer id, Model model) {
		boolean deleteAddress = masterAddressService.deleteAddress(id);
		
		if(deleteAddress) {
			model.addAttribute("cusSMS","Address Deleted");
		}else{
			model.addAttribute("erSMS","Address was not deleted");
		}
		
		 return "redirect:/user/address";
	}

	@GetMapping("/update-status")
	public String UpdateOrder(@RequestParam Integer id, @RequestParam Integer st) {
	    OrderStatus[] values = OrderStatus.values();
	    String status = null;

	    for (OrderStatus order : values) {
	        if (order.getId().equals(st)) {
	            status = order.getName();
	        }
	    }

	    Boolean updateOrderStatus = productOrderService.updateOrderStatus(id, status);

	    if (updateOrderStatus) {
	        // fetch the updated order to send email
	        ProductOrder updatedOrder = productOrderService.getOrderById(id);
	        if (updatedOrder != null) { 
	            updatedOrder.setStatus(status); // set the updated status
	            try {
	            	   mailService.sendMailForProductStatus(updatedOrder); // send the mail
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    }

	    return "redirect:/user/myorder";
	}

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        String email = principal.getName();  
        UserDto userByEmail = userService.getUserByEmail(email);
        model.addAttribute("usern", userByEmail);
        return "user/profile";  
    }
    
    @PostMapping("/savep")
    public String saveProfile(@ModelAttribute("usern") UserDto userDto) {
        userService.saveUser(userDto);
        return "redirect:/user/profile";
    }
    
    @PostMapping("/upload-photo")
    public String updatePhoto(@RequestParam("photo") MultipartFile photo,
                              Principal principal,
                              RedirectAttributes redirectAttributes) {
    	
        boolean updated = userService.updateUserPhoto(photo, principal.getName());
    	
        if (updated) {
            redirectAttributes.addFlashAttribute("message", "Profile photo updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to update profile photo.");
        }
        return "redirect:/user/profile";
    }
    @PostMapping("/changed-pw")
    public String changedpw(@RequestParam("newpw") String newpw,
                            @RequestParam("confirmpw") String confirmpw,
                            @RequestParam("currentpw") String currentpw,
                            RedirectAttributes redirectAttributes,
                            Principal principal) {
        String email = principal.getName();
        UserDto user = userService.getUserByEmail(email);

        if (!newpw.equals(confirmpw)) {
            redirectAttributes.addFlashAttribute("error", "New password and confirm password do not match.");
            return "redirect:/user/profile";
        }

        boolean matches = passwordEncoder.matches(currentpw, user.getPassword());

        if (!matches) {
            redirectAttributes.addFlashAttribute("error", "Current password is incorrect.");
            return "redirect:/user/profile";
        }

        String encoded = passwordEncoder.encode(newpw);
        user.setPassword(encoded);

        User userEntity = convertToEntity(user);
        User updatedUser = userService.updateUser(userEntity);

        if (ObjectUtils.isEmpty(updatedUser)) {
            redirectAttributes.addFlashAttribute("error", "Failed to update password.");
        } else {
            redirectAttributes.addFlashAttribute("message", "Password updated successfully!");
            try {
                mailService.sendAccountStatus(updatedUser);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return "redirect:/user/profile";
    }

	public User convertToEntity(UserDto userDto) {
	    User user = new User();
	    user.setId(userDto.getId());
	    user.setName(userDto.getName());
	    user.setEmail(userDto.getEmail());
	    user.setPhone(userDto.getPhone());
	    user.setCity(userDto.getCity());
	    user.setAddress(userDto.getAddress());
	    user.setState(userDto.getState());
	    user.setPin(userDto.getPin());
	    user.setPassword(userDto.getPassword());
	    user.setIsEnable(userDto.getIsEnable());  // Assuming it's a boolean
	    user.setPhoto(userDto.getPhoto());
//	    user.setRole(userDto.getRole());
	    String role = userDto.getRole();
	    if (role == null || role.trim().isEmpty()) {
	        role = "ROLE_USER"; // default role
	    }
	    user.setRole(role);
	    user.setAcccountNonLock(userDto.getAcccountNonLock() != null ? userDto.getAcccountNonLock() : true);

	    return user;
	}

}
