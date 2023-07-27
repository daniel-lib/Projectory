package com.app.projectory.doa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.app.projectory.entity.Users;

public interface UsersRepository extends CrudRepository<Users, Long> {
	
	 List<Users> findAll();
}
