package com.app.projectory.service;

import com.app.projectory.dao.SystemAccessTrackerRepository;
import com.app.projectory.entity.SystemAccessTracker;
import java.time.Instant;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SystemAccessTrackerService {
  private final SystemAccessTrackerRepository repository;

  public SystemAccessTrackerService(SystemAccessTrackerRepository repository) {
    this.repository = repository;
  }

  @Transactional
  public void record(HttpServletRequest request, boolean guestAccess, String actorIdentifier) {
    SystemAccessTracker entry = new SystemAccessTracker();
    populateEntry(entry, request, guestAccess, actorIdentifier);
    repository.save(entry);
  }

  @Transactional
  public void recordUnauthenticatedVisitor(HttpServletRequest request, UUID visitorId) {
    String actorIdentifier = "UNAUTHENTICATED_" + visitorId;
    SystemAccessTracker entry =
        repository.findByActorIdentifier(actorIdentifier).orElseGet(SystemAccessTracker::new);
    populateEntry(entry, request, false, actorIdentifier);
    repository.save(entry);
  }

  private void populateEntry(
      SystemAccessTracker entry,
      HttpServletRequest request,
      boolean guestAccess,
      String actorIdentifier) {
    String ipAddress = request.getRemoteAddr();
    entry.setIpAddress(
        ipAddress == null ? "unknown" : ipAddress.substring(0, Math.min(ipAddress.length(), 45)));
    String userAgent = request.getHeader("User-Agent");
    entry.setUserAgent(
        userAgent == null ? null : userAgent.substring(0, Math.min(userAgent.length(), 1024)));
    entry.setBrowserType(detectBrowser(userAgent));
    entry.setOperatingSystem(detectOperatingSystem(userAgent));
    entry.setGuestAccess(guestAccess);
    entry.setActorIdentifier(
        actorIdentifier == null
            ? null
            : actorIdentifier.substring(0, Math.min(actorIdentifier.length(), 100)));
    entry.setAccessedAt(Instant.now());
  }

  private String detectBrowser(String userAgent) {
    if (userAgent == null) {
      return "Unknown";
    }
    if (userAgent.contains("Edg/")) {
      return "Edge";
    }
    if (userAgent.contains("OPR/") || userAgent.contains("Opera")) {
      return "Opera";
    }
    if (userAgent.contains("Firefox/")) {
      return "Firefox";
    }
    if (userAgent.contains("Chrome/") && !userAgent.contains("Chromium/")) {
      return "Chrome";
    }
    if (userAgent.contains("Safari/") && userAgent.contains("Version/")) {
      return "Safari";
    }
    return "Other";
  }

  private String detectOperatingSystem(String userAgent) {
    if (userAgent == null) {
      return "Unknown";
    }
    if (userAgent.contains("Windows")) {
      return "Windows";
    }
    if (userAgent.contains("Android")) {
      return "Android";
    }
    if (userAgent.contains("iPhone") || userAgent.contains("iPad")) {
      return "iOS";
    }
    if (userAgent.contains("Mac OS X") || userAgent.contains("Macintosh")) {
      return "macOS";
    }
    if (userAgent.contains("Linux")) {
      return "Linux";
    }
    return "Other";
  }
}
