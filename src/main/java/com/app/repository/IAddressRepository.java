package com.app.repository;

import com.app.entities.Address;
import com.app.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAddressRepository extends JpaRepository<Address, Long> {
	
	Address findByUser(User userId);

}
