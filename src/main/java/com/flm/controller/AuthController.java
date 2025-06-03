package com.flm.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flm.dto.LoginDto;
import com.flm.exception.InvalidCredentialsException;
import com.flm.exception.PasswordDoesntExistException;
import com.flm.model.User;
import com.flm.security.JwtService;
import com.flm.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserDetailsService userDetailsService;//UserDetails interface is responsible for retrieving user details from a database
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtService jwtUtil;
	
	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getUsername());
		
		try {
			
			if(passwordEncoder.matches(loginDto.getPassword(), userDetails.getPassword())) {// verifies passwords
				Map<String, Object> extraClaims = new HashMap();//extra claims may includes role,subject means user, expiration. For token
				extraClaims.put("roles", userDetails.getAuthorities().stream() //userDetails.getAuthorities(),retrieves the granted authorities i.e, roles assigned
						.map(GrantedAuthority::getAuthority)//extracts the role names from the GrantedAuthority objects
						.collect(Collectors.toList()));
				
				String token = jwtUtil.generateToken(extraClaims, loginDto.getUsername());
				return ResponseEntity.ok(token); 
			} else {
				throw new InvalidCredentialsException("Invalid password");
			}
		} catch (UsernameNotFoundException ex) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
	    } catch (InvalidCredentialsException | PasswordDoesntExistException ex) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
	    }
		
	}
	
	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@RequestBody LoginDto loginDto) {
		User user = userService.resetPassword(loginDto);
		return new ResponseEntity<>(user.getStaff().getStaffId(), HttpStatus.OK);
	}

}
