package com.flm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.flm.dao.UserRepository;
import com.flm.dto.LoginDto;
import com.flm.exception.InvalidCredentialsException;
import com.flm.model.User;
import com.flm.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public User resetPassword(LoginDto loginDto) {
		User user = userRepository.findUserByEmail(loginDto.getUsername());
		
		if (user == null) {
			throw new InvalidCredentialsException("Invalid Email.");
		}
		
		user.setPassword(passwordEncoder.encode(loginDto.getPassword()));
		User savedUser = userRepository.save(user);
		return savedUser;
	}

}
