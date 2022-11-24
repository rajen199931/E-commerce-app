package com.cgi.userprofile.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgi.userprofile.entity.Register;
import com.cgi.userprofile.exceptions.UserIdAlreadyExist;
import com.cgi.userprofile.exceptions.UserIdNotFound;
import com.cgi.userprofile.repository.UserProfileRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class UserProfileServiceImpl implements UserProfileService{
	
	@Autowired
	public UserProfileRepository userRepository;
	
	@Override
	public List<Register> getAllUser(){
		return userRepository.findAll();
	}
	
	@Override
	public Register getUserById(int id) throws UserIdNotFound{
		Optional<Register> optional=userRepository.findById(id);
		if(optional.isPresent()) {
			return optional.get();
		}
		throw new UserIdNotFound();
	}
	
	@Override
	public Register addNewUser(Register register) throws UserIdAlreadyExist{
		Optional<Register> optional=userRepository.findById(register.getUserId());
		Optional<Register> optional2=userRepository.findByUsernameAndPassword(register.getUsername(),register.getPassword());
		
		if(optional2.isEmpty()) {
			userRepository.save(register);
			return register;
		}else {
			throw new UserIdAlreadyExist();
		}
	}
	
	@Override
	public void deleteUser(int id)throws UserIdNotFound{
		Optional<Register> optional=userRepository.findById(id);
		if(optional.isPresent()) {
			optional.get();
		}
		throw new UserIdNotFound();
	}
	
	
	@Override
	public  Register updateUser(Register register) throws UserIdNotFound{
		Optional<Register> useroptional=userRepository.findById(register.getUserId());
		if(useroptional.isPresent()) {
			useroptional.get().setUserId(register.getUserId());
			useroptional.get().setUsername(register.getUsername());
			useroptional.get().setPassword(register.getPassword());
			useroptional.get().setEmail(register.getEmail());
			useroptional.get().setRole(register.getRole());
			
			userRepository.save(useroptional.get());
			return useroptional.get();
		}
		throw new UserIdNotFound();
	}
	
	public Register verifyUser(String username, String password) throws UserIdNotFound{
		Optional<Register> useroptional=userRepository.findByUsernameAndPassword(username, password);
		if(useroptional.isPresent()) {
			return useroptional.get();
		}
		throw new UserIdNotFound();
	}
	
	public String generateToken(Register user) {
		String jwtToken;
		jwtToken=Jwts.builder()
				.setSubject(Integer.toString(user.getUserId())).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+800000))
				.signWith(SignatureAlgorithm.HS256,"stackroute")
				.compact();
		return jwtToken;
	}

}
