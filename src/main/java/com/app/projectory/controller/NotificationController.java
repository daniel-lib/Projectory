package com.app.projectory.controller;

import com.app.projectory.service.NotificationService;
import com.app.projectory.service.userAccountService;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/notifications")
public class NotificationController {

  private final NotificationService notificationService;
  private final userAccountService userService;

  public NotificationController(
      NotificationService notificationService, userAccountService userService) {
    this.notificationService = notificationService;
    this.userService = userService;
  }

  @PostMapping("/seen")
  public String markNotificationsSeen(
      Authentication authentication, Model model, HttpServletResponse response) {
    long receiverUserId = userService.getUserId(authentication);
    notificationService.markAllSeen(receiverUserId);
    addNotificationModel(receiverUserId, model);
    response.setHeader("HX-Trigger-After-Settle", "{\"notificationCountChanged\":null}");
    return "fragments/notifications :: notification-list";
  }

  @GetMapping("/count")
  public String notificationCount(Authentication authentication, Model model) {
    model.addAttribute(
        "unreadNotificationCount",
        notificationService.getUnreadCount(userService.getUserId(authentication)));
    return "fragments/notifications :: notification-count-value";
  }

  private void addNotificationModel(long receiverUserId, Model model) {
    model.addAttribute("notifications", notificationService.getLatestNotifications(receiverUserId));
    model.addAttribute(
        "unreadNotificationCount", notificationService.getUnreadCount(receiverUserId));
  }
}
