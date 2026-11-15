package com.app.projectory.controller;

import com.app.projectory.service.GuestAccessRateLimiter;
import com.app.projectory.service.SystemAccessTrackerService;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/system-access")
public class SystemAccessTrackerController {
  private final SystemAccessTrackerService accessTrackerService;
  private final GuestAccessRateLimiter rateLimiter;

  public SystemAccessTrackerController(
      SystemAccessTrackerService accessTrackerService, GuestAccessRateLimiter rateLimiter) {
    this.accessTrackerService = accessTrackerService;
    this.rateLimiter = rateLimiter;
  }

  @PostMapping("/visit")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void recordVisit(
      @RequestHeader("X-Visitor-Id") String visitorId, HttpServletRequest request) {
    rateLimiter.checkVisitorTracking(request.getRemoteAddr());
    UUID parsedVisitorId;
    try {
      parsedVisitorId = UUID.fromString(visitorId);
    } catch (IllegalArgumentException exception) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid visitor identifier.");
    }
    if (!parsedVisitorId.toString().equalsIgnoreCase(visitorId)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid visitor identifier.");
    }
    accessTrackerService.recordUnauthenticatedVisitor(request, parsedVisitorId);
  }
}
