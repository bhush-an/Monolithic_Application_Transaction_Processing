package com.app.repository;

import com.app.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRespository extends JpaRepository<User, String> {

	Optional<User> findByUserId(String userId);
	
	User findByEmailId(String emailId);

}
