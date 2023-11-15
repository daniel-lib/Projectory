package com.app.projectory.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.app.projectory.dto.ConnectionSenderReceiverDto;
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
			+ "WHERE (request_sender_user = us.user_id OR request_receiver_user = us.user_id)\n"
			+ "AND con.connection_status = 'approved'", 
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
			+ "WHERE request_sender_user = us.user_id OR request_receiver_user = us.user_id", 
			nativeQuery = true)
	public List<PublicUserConnectionsDetailDto> findAllConnections(String username);
	
	//find sent requests
	@Query(value="SELECT DISTINCT con.connection_id connectionId, con.connection_status connectionStatus,\n"
			+ "us.username connectedUserUsername, us.profile_picture profilePicture,\n"
			+ "us.first_name connectedUserFirstName, us.last_name connectedUserLastName\n"
			+ "FROM connections con \n"
			+ "LEFT JOIN users us on us.user_id = con.request_receiver_user\n"
			+ "WHERE con.request_sender_user = ?1 AND con.connection_status = 'pending'", nativeQuery = true)
	public List<PublicUserConnectionsDetailDto> findSentConnectionRequests(long senderUserId);
		
	
	//find received requests
		@Query(value="SELECT DISTINCT con.connection_id connectionId, con.connection_status connectionStatus,\n"
				+ "us.username connectedUserUsername, us.profile_picture profilePicture,\n"
				+ "us.first_name connectedUserFirstName, us.last_name connectedUserLastName\n"
				+ "FROM connections con \n"
				+ "LEFT JOIN users us on us.user_id = con.request_sender_user\n"
				+ "WHERE con.request_receiver_user = ?1 AND con.connection_status = 'pending'", nativeQuery = true)
		public List<PublicUserConnectionsDetailDto> findReceivedConnectionRequests(long senderUserId);
	
	
	
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
	
	@Transactional
	@Modifying
	@Query(value="INSERT INTO connections(\n"
			+ "connection_date, connection_status, request_receiver_user, request_sender_user)\n"
			+ "SELECT ?3,'pending', ?1, ?2\n"
			+ "WHERE NOT EXISTS(SELECT 1 FROM connections\n"
			+ "WHERE (request_receiver_user=?1 AND request_sender_user=?2)\n"
			+ "OR (request_receiver_user=?2 AND request_sender_user=?1))", nativeQuery = true)
	int sendConnectionRequest(long receiverUserId, long senderUserId, LocalDate dateToday);
	
	
	@Transactional
	@Modifying
	@Query(value="DELETE FROM connections \n"
			+ "WHERE request_sender_user = ?2\n"
			+ "	AND request_receiver_user = ?1", nativeQuery = true)
	int cancelConnectionRequest(long receiverUserId, long senderUserId);
	
	
//	@Transactional
//	@Modifying
//	@Query(value="DELETE FROM connections \n"
//			+ "WHERE request_sender_user = ?2\n"
//			+ "	AND request_receiver_user = ?1", nativeQuery = true)
//	int cancelConnectionRequestByUsername(String receiverUserIdUsername, long senderUserId);
	
	
	@Transactional
	@Modifying
	@Query(value="DELETE FROM connections \n"
			+ "WHERE (request_receiver_user = ?1 AND request_sender_user = ?2)\n"
			+ "OR (request_receiver_user = ?2 AND request_sender_user = ?1) \n"
			+ "AND EXISTS (SELECT 1 FROM connections\n"
			+ "WHERE (request_receiver_user = ?1 AND request_sender_user = ?2)\n"
			+ "OR (request_receiver_user = ?2 AND request_sender_user = ?1)) AND connection_status = 'approved'", nativeQuery = true)
	int terminateConnection(long receiverUserId, long senderUserId);
	
	
	@Transactional
	@Modifying
	@Query(value="DELETE FROM connections \n"
			+ "WHERE connection_id = ?1"
			+ " AND connection_status = 'approved'", nativeQuery = true)
	int terminateConnectionByConnectionId(long connectionId);
	
	@Transactional
	@Modifying
	@Query(value="UPDATE connections\n"
			+ "	SET connection_status = 'approved'\n"
			+ "	WHERE connection_id = ?1", nativeQuery = true)
	int acceptConnectionRequest(long connectionId);
	
	@Transactional
	@Modifying
	@Query(value="DELETE FROM connections\n"
			+"WHERE connection_id = ?1", nativeQuery = true)
	int rejectConnectionRequest(long connectionId);
	
	
	
	@Query(value = "SELECT connection_status\n"
			+ "	FROM connections\n"
			+ "	WHERE (request_receiver_user = ?1 AND request_sender_user = ?2)\n"
			+ "	OR (request_receiver_user = ?2 AND request_sender_user = ?1)", nativeQuery=true)
	public String getConnectionStatus(long receiverUserId, long senderUserId);
	
	
	@Query(value = "SELECT connection_id connectionId, request_receiver_user requestReceiver, request_sender_user requestSender\n"
			+ "FROM connections\n"
			+ "WHERE (request_receiver_user = ?1 AND request_sender_user = ?2)\n"
			+ "OR (request_receiver_user = ?2 AND request_sender_user = ?1)", nativeQuery=true)
	public ConnectionSenderReceiverDto getConnectionSenderReceiverId(long receiverUserId, long senderUserId);

}
