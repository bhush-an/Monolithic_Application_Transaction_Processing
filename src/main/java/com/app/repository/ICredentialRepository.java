package com.app.repository;

import com.app.entities.Credential;
import com.app.entities.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICredentialRepository extends JpaRepository<Credential, String> {

	Optional<Credential> findByUsername(String username);
	
	List<Credential> findByRole(RoleEnum role);

}
