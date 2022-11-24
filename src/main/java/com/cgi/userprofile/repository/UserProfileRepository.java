package com.cgi.userprofile.repository;

import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cgi.userprofile.entity.Register;

//@Repository:It will provides the storage,search,update and delete operations.
// @Transactional: It is metadata that specifies that an interface, class, or method
@Repository
@Transactional

	//JpaRepository:spring recognize the repositories by the fact that they extend one of the predefined repository interfaces.
public interface UserProfileRepository extends JpaRepository<Register,Integer> {
	
	//Optional:It is avoid working with null values and program can execute without crashing.

	Optional<Register> findByUsernameAndPassword(String username, String password);
	}




	
