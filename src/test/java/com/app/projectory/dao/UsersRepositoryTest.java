package com.app.projectory.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.app.projectory.dto.PublicUserPersonalDetailDto;
import com.app.projectory.entity.Connections;
import com.app.projectory.entity.Users;

@DataJpaTest(properties = {
		"spring.jpa.hibernate.ddl-auto=create-drop",
		"spring.sql.init.mode=never"
})
@AutoConfigureTestDatabase(replace = Replace.ANY)
class UsersRepositoryTest {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private UsersRepository usersRepository;

	@Test
	void suggestedConnectionsExcludeCurrentUserAndExistingRequests() {
		Users currentUser = persistUser("suggest-current");
		Users connectedUser = persistUser("suggest-connected");
		Users pendingUser = persistUser("suggest-pending");
		Users suggestion = persistUser("suggest-new");

		persistConnection(currentUser, connectedUser, "approved");
		persistConnection(pendingUser, currentUser, "pending");
		entityManager.flush();

		assertEquals(1, usersRepository.findSuggestedConnections(currentUser.getUserId()).size());
		PublicUserPersonalDetailDto result =
				usersRepository.findSuggestedConnections(currentUser.getUserId()).get(0);
		assertEquals(suggestion.getUsername(), result.getUsername());
	}

	private Users persistUser(String username) {
		Users user = new Users();
		user.setUsername(username);
		user.setFirstName(username);
		user.setLastName("Test");
		entityManager.persist(user);
		return user;
	}

	private void persistConnection(Users sender, Users receiver, String status) {
		Connections connection = new Connections();
		connection.setUserWithConnection(sender);
		connection.setConnectedUser(Long.valueOf(receiver.getUserId()));
		connection.setConnectionStatus(status);
		entityManager.persist(connection);
	}
}
