package com.jc.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.jc.demo.dto.UserDto;
import com.jc.demo.model.User;
 
public interface UserService {
		
	public List<User> getAllUser(User user);
	
	public boolean saveUser(UserDto userDto);
	
	public boolean saveAdmin(UserDto userDto);

	public boolean saveAdmin(User user);
	
	public boolean updateUserPhoto(MultipartFile file, String email);

	public List<UserDto> getAllUser();
    public boolean existsByEmail(String email);
 
    List<String> getAllStates();
    public List<UserDto> getAllState(String state);
    
    UserDto getUserById(Integer id);
  
    public UserDto getUserByEmail(String email);
    
    public boolean accountStatus(Integer id, String status);
    
//    
    public void increasefailedAttempt(User user);
    public void userAcountLock(User user);
    public boolean unlockAcountTimeExpired(User user);
    
    
    
    
    public void resetAttempt(int userDtoId);
//    
	public void upsateUserResetToken(String email, String resetToken);
	public User getUserByToken(String token);
	public User updateUser(User user);
}




