package com.cgi.userprofile.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgi.userprofile.entity.Login;
import com.cgi.userprofile.entity.Register;
import com.cgi.userprofile.exceptions.UserIdAlreadyExist;
import com.cgi.userprofile.exceptions.UserIdNotFound;
import com.cgi.userprofile.service.UserProfileService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    private UserProfileService userService;

    @GetMapping(value ="/users")
    public ResponseEntity<List<Register>> getAllUserAccounts(){
        ResponseEntity<List<Register>> responseEntity;
        List<Register> register = userService.getAllUser();
        responseEntity = new ResponseEntity<List<Register>>(register, HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserByIdHandler(@PathVariable("userId") int id) throws UserIdNotFound {
        ResponseEntity<?> responseEntity;
        try {
            Register register = userService.getUserById(id);
            responseEntity = new ResponseEntity<Register>(register, HttpStatus.OK);
        }catch (UserIdNotFound e){
            responseEntity = new ResponseEntity<String>("User with ID not found", HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }
    @PostMapping("/user/signup")
    public ResponseEntity<?> addUserAccountHandler(@RequestBody Register register){
        ResponseEntity<?> responseEntity;
        try {
            Register newUser = userService.addNewUser(register);
            responseEntity = new ResponseEntity<Register>(newUser, HttpStatus.CREATED);

        } catch (UserIdAlreadyExist e) {
            responseEntity = new ResponseEntity<String>("Failed to store the user account: Duplicate Resource", HttpStatus.CONFLICT);
        }
        return responseEntity;
    }
    
    @PostMapping("/user/login")
    public ResponseEntity<?> loginHandler(@RequestBody Login login ){

    	ResponseEntity<?> responseEntity;

    	Map<String, String> tokenMap = new HashMap<>();

    	try {
        	Register register = userService.verifyUser(login.getUsername(),login.getPassword());

            
        	// 
        	String token = userService.generateToken(register);
        	tokenMap.put("token", token);
        	responseEntity = new ResponseEntity<Map<String, String>>(tokenMap,HttpStatus.OK);
    	} catch(UserIdNotFound e) {
    	tokenMap.clear();
    	tokenMap.put("token", null);
    	tokenMap.put("message", "Invalid User Credentials");
    	responseEntity = new ResponseEntity<Map<String,String>>(tokenMap,HttpStatus.FORBIDDEN);
    	}

    	return responseEntity;
    	}
    
    @PostMapping("/user/isAuthenticated")
	public ResponseEntity<Map<String,Object>> verifyToken(@RequestHeader("Authorization") String authHeader){
		System.out.println("Request received");
		
		ResponseEntity<Map<String, Object>> responseEntity;
		HashMap<String, Object> map = new HashMap<>();
		map.clear();
		System.out.println(authHeader);
		String token = authHeader.split(" ")[1];
		try {
			Claims claims =  Jwts.parser()
			.setSigningKey("stackroute")
			.parseClaimsJws(token)
			.getBody();
			map.put("isAuthenticated", true);
			map.put("userId", claims.getSubject());
			responseEntity = new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
			
		}catch(Exception e) {
			map.put("isAuthenticated", false);
			responseEntity = new ResponseEntity<Map<String,Object>>(map,HttpStatus.FORBIDDEN);
		}
		
		return responseEntity;
		
	}


    @DeleteMapping("/user/{userId}")
    public ResponseEntity<String> deleteUserHandler(@PathVariable("userId") int id )throws UserIdNotFound{
        ResponseEntity<String> responseEntity;
        try {
            userService.deleteUser(id);
            responseEntity = new ResponseEntity<String>("User Account Deleted", HttpStatus.NO_CONTENT);
        }catch (UserIdNotFound e){
            responseEntity = new ResponseEntity<String>("User Account with ID not found", HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<?> updateUserHandler(@PathVariable("userId") int id) throws UserIdNotFound{
        ResponseEntity<?> responseEntity;
        try {
            Register register = userService.updateUser(userService.getUserById(id));
            responseEntity = new ResponseEntity<Register>(register, HttpStatus.OK);
        }catch (UserIdNotFound e){
            responseEntity = new ResponseEntity<String>("User Account with ID not found", HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }
}
