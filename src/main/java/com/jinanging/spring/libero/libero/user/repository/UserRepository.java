package com.jinanging.spring.libero.libero.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jinanging.spring.libero.libero.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	int countByLoginId(String loginId); 
	
	Optional<User> findByLoginId(String loginId);
	

	

}
