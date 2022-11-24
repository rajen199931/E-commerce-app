package com.cgi.userprofile.service;

import java.util.List;
import com.cgi.userprofile.entity.Register;
import com.cgi.userprofile.exceptions.UserIdAlreadyExist;
import com.cgi.userprofile.exceptions.UserIdNotFound;

public interface UserProfileService {
	
    List<Register> getAllUser();
    
    Register getUserById(int id) throws UserIdNotFound;
    
    Register addNewUser(Register register) throws UserIdAlreadyExist;
    
    void deleteUser(int id) throws UserIdNotFound;
    
    Register updateUser(Register register) throws UserIdNotFound;
    
    Register verifyUser(String username, String password) throws UserIdNotFound;
    
    String generateToken(Register user);


}
