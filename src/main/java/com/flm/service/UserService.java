package com.flm.service;

import org.springframework.stereotype.Service;

import com.flm.dto.LoginDto;
import com.flm.model.User;

@Service
public interface UserService {
	public User resetPassword(LoginDto loginDto);
}
