package com.app.projectory.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.app.projectory.dto.PublicUserConnectionsDetailDto;
import com.app.projectory.entity.Connections;

public interface ConnectionsRepository extends CrudRepository<Connections, Long>{
	
	@Query(value="SELECT DISTINCT con.connection_id connectionId, con.connection_status connectionStatus, \n"
			+ "us2.username connectedUserUsername, us2.profile_picture profilePicture,\n"
			+ "us2.first_name connectedUserFirstName, us2.last_name connectedUserLastName\n"
			+ "FROM connections con \n"
			+ "LEFT JOIN users us on us.username = ?1\n"
			+ "LEFT JOIN users us2 on (us2.user_id = con.request_receiver_user\n"
			+ "OR us2.user_id = con.request_sender_user) \n"
			+ "AND us2.user_id != us.user_id \n"
			+ "WHERE request_sender_user = us.user_id OR request_receiver_user = us.user_id", 
			nativeQuery = true)
	public List<PublicUserConnectionsDetailDto> findPublicConnections(String username);
	
	
	@Query(value="SELECT DISTINCT con.connection_id connectionId, con.connection_status connectionStatus, \n"
			+ "us2.username connectedUserUsername, us2.profile_picture profilePicture,\n"
			+ "us2.first_name connectedUserFirstName, us2.last_name connectedUserLastName\n"
			+ "FROM connections con \n"
			+ "LEFT JOIN users us on us.username = ?1\n"
			+ "LEFT JOIN users us2 on (us2.user_id = con.request_receiver_user\n"
			+ "OR us2.user_id = con.request_sender_user) \n"
			+ "AND us2.user_id != us.user_id \n"
			+ "WHERE (request_sender_user = us.user_id OR request_receiver_user = us.user_id) AND connection_status = 'approved'", 
			nativeQuery = true)
	public List<PublicUserConnectionsDetailDto> findApprovedConnections(String username);

}
