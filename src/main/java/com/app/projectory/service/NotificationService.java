package com.app.projectory.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.projectory.dao.NotificationRepository;
import com.app.projectory.dao.UsersRepository;
import com.app.projectory.entity.Notification;
import com.app.projectory.entity.Users;

@Service
public class NotificationService {

	private final NotificationRepository notificationRepository;
	private final UsersRepository usersRepository;

	public NotificationService(NotificationRepository notificationRepository, UsersRepository usersRepository) {
		this.notificationRepository = notificationRepository;
		this.usersRepository = usersRepository;
	}

	@Transactional
	public void createFriendRequestNotification(long receiverUserId, long senderUserId) {
		Users receiver = usersRepository.findById(receiverUserId)
				.orElseThrow(() -> new IllegalArgumentException("Connection request receiver does not exist"));
		Users sender = usersRepository.findById(senderUserId)
				.orElseThrow(() -> new IllegalArgumentException("Connection request sender does not exist"));

		Notification notification = new Notification();
		notification.setType("FRIEND_REQUEST");
		notification.setMessage(sender.getFirstName() + " " + sender.getLastName() + " sent you a connection request");
		notification.setRedirectLink("/user/connections");
		notification.setReceiver(receiver);
		notification.setSender(sender);
		notificationRepository.save(notification);
	}

	@Transactional(readOnly = true)
	public List<Notification> getLatestNotifications(long receiverUserId) {
		return notificationRepository.findTop25ByReceiver_UserIdOrderByCreatedAtDesc(receiverUserId);
	}

	@Transactional(readOnly = true)
	public long getUnreadCount(long receiverUserId) {
		return notificationRepository.countByReceiver_UserIdAndSeenFalse(receiverUserId);
	}

	@Transactional
	public void markAllSeen(long receiverUserId) {
		notificationRepository.markAllSeen(receiverUserId, LocalDateTime.now());
	}
}
