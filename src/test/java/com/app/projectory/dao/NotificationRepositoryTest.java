package com.app.projectory.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.app.projectory.entity.Notification;
import com.app.projectory.entity.Users;
import com.app.projectory.service.NotificationService;

@DataJpaTest(properties = {
		"spring.jpa.hibernate.ddl-auto=create-drop",
		"spring.sql.init.mode=never"
})
@AutoConfigureTestDatabase(replace = Replace.ANY)
@Import(NotificationService.class)
class NotificationRepositoryTest {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private NotificationService notificationService;

	@Test
	void friendRequestCreatesNotificationForTheReceiver() {
		Users receiver = persistUser("friend-request-receiver");
		Users sender = persistUser("friend-request-sender");
		entityManager.flush();

		notificationService.createFriendRequestNotification(receiver.getUserId(), sender.getUserId());

		assertEquals(1, notificationRepository.countByReceiver_UserIdAndSeenFalse(receiver.getUserId()));
		Notification notification = notificationRepository
				.findTop25ByReceiver_UserIdOrderByCreatedAtDesc(receiver.getUserId()).get(0);
		assertEquals("FRIEND_REQUEST", notification.getType());
		assertEquals("/user/connections", notification.getRedirectLink());
		assertEquals("friend-request-sender Test sent you a connection request", notification.getMessage());
	}

	@Test
	void notificationTracksUnreadCountAndSeenTimestampForReceiver() {
		Users receiver = persistUser("notification-receiver");
		Users sender = persistUser("notification-sender");
		Notification notification = new Notification();
		notification.setType("FRIEND_REQUEST");
		notification.setMessage("A user sent you a connection request");
		notification.setRedirectLink("/user/connections");
		notification.setReceiver(receiver);
		notification.setSender(sender);
		notificationRepository.saveAndFlush(notification);

		assertEquals(1, notificationRepository.countByReceiver_UserIdAndSeenFalse(receiver.getUserId()));
		assertEquals(1, notificationRepository
				.findTop25ByReceiver_UserIdOrderByCreatedAtDesc(receiver.getUserId()).size());

		LocalDateTime seenAt = LocalDateTime.now();
		assertEquals(1, notificationRepository.markAllSeen(receiver.getUserId(), seenAt));
		entityManager.clear();

		Notification updated = notificationRepository.findById(notification.getNotificationId()).orElseThrow();
		assertTrue(updated.isSeen());
		assertNotNull(updated.getSeenAt());
		assertEquals(seenAt.withNano(0), updated.getSeenAt().withNano(0));
		assertEquals(0, notificationRepository.countByReceiver_UserIdAndSeenFalse(receiver.getUserId()));
	}

	private Users persistUser(String username) {
		Users user = new Users();
		user.setUsername(username);
		user.setFirstName(username);
		user.setLastName("Test");
		entityManager.persist(user);
		entityManager.flush();
		return user;
	}
}
