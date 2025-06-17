package com.jc.demo.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jc.demo.JustclickApplication;
import com.jc.demo.dto.UserDto;
import com.jc.demo.model.Product;
import com.jc.demo.model.User;
import com.jc.demo.repository.UserRepository;
import com.jc.demo.service.UserService;
import com.jc.demo.util.AppConstant;

@Service
public class UserServiceImpl implements UserService {

    private final JustclickApplication justclickApplication;


	@Autowired
	private UserRepository userRepository;
    @Autowired
	private PasswordEncoder passwordEncoder;

    UserServiceImpl(JustclickApplication justclickApplication) {
        this.justclickApplication = justclickApplication;
    }
	@Override
	public boolean saveUser(UserDto userDto) {
		
   try {
	   
	     User user = new User();
	        user.setId(userDto.getId());
	        user.setName(userDto.getName());
	        user.setPhone(userDto.getPhone());
	        user.setEmail(userDto.getEmail());
	        user.setCity(userDto.getCity());
	        user.setAddress(userDto.getAddress());
	        user.setState(userDto.getState());
	        user.setPin(userDto.getPin());
	        user.setPhoto(userDto.getPhoto());
	        user.setPassword(userDto.getPassword());
	        user.setRole(userDto.getRole());
	        user.setIsEnable(userDto.getIsEnable());
	        
	        user.setRole("ROLE_USER");
	        user.setIsEnable(true);
	        user.setAcccountNonLock(true);
	        user.setFailAttempt(0);
	       
	        String encodePassword =passwordEncoder.encode(user.getPassword());
	
	        user.setPassword(encodePassword);
	        
	       User save = userRepository.save(user);
	       convertoDto(save);
	       return true;
	   
   } catch (Exception e) {
	e.printStackTrace();
	return false;
}
       	
	}
	
	private UserDto convertoDto (User user) {
		UserDto userDto = new UserDto();
		userDto.setId(user.getId());
		userDto.setName(user.getName());
		userDto.setPhone(user.getPhone());
		userDto.setEmail(user.getEmail());
		userDto.setCity(user.getCity());
		userDto.setAddress(user.getAddress());
		userDto.setState(user.getState());
		userDto.setPin(user.getPin());
		userDto.setPhoto(user.getPhoto());
		userDto.setPassword(user.getPassword());
		userDto.setIsEnable(user.getIsEnable());
		userDto.setAcccountNonLock(user.getAcccountNonLock());
		return userDto;
	}
//	public User convertToEntity(UserDto userDto) {
//    User user = new User();
//    user.setId(userDto.getId());
//    user.setName(userDto.getName());
//    user.setEmail(userDto.getEmail());
//    user.setPhone(userDto.getPhone());
//    user.setCity(userDto.getCity());
//    user.setAddress(userDto.getAddress());
//    user.setState(userDto.getState());
//    user.setPin(userDto.getPin());
//    user.setPassword(userDto.getPassword());
//    user.setIsEnable(userDto.getIsEnable());  // Assuming it's a boolean
//    user.setPhoto(userDto.getPhoto());
//
//    return user;
//}

  



	@Override
	public List<UserDto> getAllUser() {
		List<User> all = userRepository.findAll();
		return all.stream()
				  .map(this::convertoDto)
				  .collect(Collectors.toList());
	}

	@Override
	public List<String> getAllStates() {
	return	userRepository.findAll()
		              .stream()
		              .map(User::getState)
		              .filter(s -> s != null && !s.trim().isEmpty())
		              .distinct()
		              .collect(Collectors.toList());		
	}

	@Override
	public List<UserDto> getAllState(String state) {
		 List<User> users = userRepository.findByStateIgnoreCase(state);
	        return users.stream()
	        	         .map(this::convertoDto)
	        	         .collect(Collectors.toList());
	}



	@Override
	public boolean existsByEmail(String email) {
		User byEmail = userRepository.findByEmail(email);
		return byEmail != null;
		//return true;
		
	}



	@Override
	public UserDto getUserByEmail(String email) {
	    User byEmail = userRepository.findByEmail(email);
	    if (byEmail == null) {
//	        throw new RuntimeException("User not found with email: " + email);
	    	return null;
	    }
	    return convertoDto(byEmail);
	}

	@Override
	public boolean accountStatus(Integer id, String status) {
	    Optional<User> optionalUser = userRepository.findById(id);
	    if (optionalUser.isPresent()) {
	        User user = optionalUser.get();
	        user.setIsEnable(Boolean.parseBoolean(status));
	        userRepository.save(user);
	        return true;
	    }
	    return false;
	}

//------------------------------- Login

	@Override
	public void increasefailedAttempt(User user) {
	 int attempt = user.getFailAttempt()+1;	

	 user.setFailAttempt(attempt);;
	 userRepository.save(user);
		
	}
	
	@Override
	public void userAcountLock(User user) {
		user.setAcccountNonLock(false);
		user.setLockTime(new Date());
		userRepository.save(user);
		
	}

	@Override
	public boolean unlockAcountTimeExpired(User user) {
		long lockTime = user.getLockTime().getTime();
		
		long unLockTime = lockTime+AppConstant.UNLOCK_DURATION_TIME;
		
		long curreentTime = System.currentTimeMillis();
		
		if(unLockTime<curreentTime) {
			user.setAcccountNonLock(true);;
			user.setFailAttempt(0);
			user.setLockTime(null);
			userRepository.save(user);
			return true;	
		}
		return false;
	}
	
//	@Override
//	public boolean isAccountNonLocked(User user) {
//	    if (user == null) {
//	        return false; // or throw new RuntimeException("User not found");
//	    }
//	    return Boolean.TRUE.equals(user.getAccountNonLocked());
//	}

	




	
//	----------------send mail----------
	
	@Override
	public void upsateUserResetToken(String email, String resetToken) {
	User byEmail = userRepository.findByEmail(email);
	byEmail.setResetToken(resetToken);	
	userRepository.save(byEmail);
		
	}
	@Override
	public User getUserByToken(String token) {
		return userRepository.findByResetToken(token);
		
	}
	@Override
	public User updateUser(User user) {
		 return userRepository.save(user);
		
	}
	@Override
	public UserDto getUserById(Integer id) {
		Optional<User> byId = userRepository.findById(id);
		return byId.map(this::convertoDto).orElse( null);
				        
	
	}
	@Override
	public List<User> getAllUser(User user) {
		List<User> all = userRepository.findAll();
		return all;
	}
	@Override
	public boolean saveAdmin(User user) {
        user.setRole("ROLE_Admin");
        user.setIsEnable(true);
        user.setAcccountNonLock(true);
        user.setFailAttempt(0);
       
        String encodePassword =passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        
       User save = userRepository.save(user);
       convertoDto(save);
       return true;
	}
	
	@Override
	public boolean updateUserPhoto(MultipartFile file, String email) {
	    try {
	        if (file.isEmpty()) return false;

	        User user = userRepository.findByEmail(email);

	        String uploadDir = new ClassPathResource("static/uploads/user_img").getFile().getAbsolutePath();
	        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
	        Path filePath = Paths.get(uploadDir, filename);
	        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

	        user.setPhoto(filename);
	        userRepository.save(user);

	        return true;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	@Override
	public void resetAttempt(int userDtoId) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean saveAdmin(UserDto userDto) {
		   try {
			   
			     User user = new User();
			        user.setId(userDto.getId());
			        user.setName(userDto.getName());
			        user.setPhone(userDto.getPhone());
			        user.setEmail(userDto.getEmail());
			        user.setCity(userDto.getCity());
			        user.setAddress(userDto.getAddress());
			        user.setState(userDto.getState());
			        user.setPin(userDto.getPin());
			        user.setPhoto(userDto.getPhoto());
			        user.setPassword(userDto.getPassword());
			        user.setRole(userDto.getRole());
			        user.setIsEnable(userDto.getIsEnable());
			        
			        user.setRole("ROLE_ADMIN");
			        user.setIsEnable(true);
			        user.setAcccountNonLock(true);
			        user.setFailAttempt(0);
			       
			        String encodePassword =passwordEncoder.encode(user.getPassword());
			
			        user.setPassword(encodePassword);
			        
			       User save = userRepository.save(user);
			       convertoDto(save);
			       return true;
			   
		   } catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	



	



}
//model.addAttribute("brandParam", brand);
