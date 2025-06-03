package com.flm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flm.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	public User findUserByEmail(String email);
}
