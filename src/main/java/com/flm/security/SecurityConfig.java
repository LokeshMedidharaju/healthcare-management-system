package com.flm.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
	
	@Autowired
	SecurityUser securityUser;
	
	@Autowired
	JwtAuthenticationFilter jwtFilter;
	
    @Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http
		.csrf(csrf -> csrf.disable())
		.cors(cors -> cors.disable())
		.authorizeHttpRequests(request -> request.requestMatchers("/auth/**").permitAll())
		.authorizeHttpRequests(request -> request.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll())
        .authorizeHttpRequests(request -> request.requestMatchers("/h2-console/**").permitAll())
        .authorizeHttpRequests(request -> request.requestMatchers("/staff/**").hasAnyAuthority("SUPERADMIN","ADMIN"))
        .authorizeHttpRequests(request -> request.requestMatchers("/doctor-schedule/**").hasAnyAuthority("SUPERADMIN","ADMIN","DOCTOR"))
		.authorizeHttpRequests(request -> request.anyRequest().authenticated())
		.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authenticationProvider(authenticationProvider())
		.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
		.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); 
		
		return http.build();
	}
    
	
	@Bean
	public PasswordEncoder passwordEncoder() {	//	hashes passwords before storing them in the database
		return new BCryptPasswordEncoder(); 
	}
	
	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();	//	During login, it authenticates users with credentials.

    }
	
	@Bean
    public AuthenticationProvider authenticationProvider() {	//	Fetches user details from DB, validates them
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(securityUser);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

}