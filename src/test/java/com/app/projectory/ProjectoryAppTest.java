package com.app.projectory;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.app.projectory.entity.Notification;

@SpringBootTest
class ProjectoryAppTest {

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Test
	void contextLoads() {
	}

	@Test
	void notificationFragmentRendersInternalAndExternalLinks() {
		Notification internal = notification("Internal message", "/user/connections");
		Notification external = notification("External message", "www.example.com");
		Context context = new Context(Locale.US);
		context.setVariable("notifications", Arrays.asList(internal, external));
		context.setVariable("unreadNotificationCount", 2L);

		String html = templateEngine.process("fragments/notifications", context);

		assertTrue(html.contains("href=\"/user/connections\""));
		assertTrue(html.contains("href=\"https://www.example.com\""));
		assertTrue(html.contains("target=\"_blank\""));
		assertTrue(html.contains("Internal message"));
		assertTrue(html.contains("External message"));
		assertTrue(html.contains(String.valueOf(LocalDateTime.now().getYear())));
	}

	private Notification notification(String message, String redirectLink) {
		Notification notification = new Notification();
		notification.setMessage(message);
		notification.setRedirectLink(redirectLink);
		notification.setType("FRIEND_REQUEST");
		ReflectionTestUtils.setField(notification, "createdAt", LocalDateTime.now());
		return notification;
	}
}
