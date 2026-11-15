package com.app.projectory.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

class GuestAccessRateLimiterTest {
  @Test
  void limitsWorkspaceCreationByNetworkAddress() {
    GuestAccessRateLimiter limiter = new GuestAccessRateLimiter();
    for (int attempt = 0; attempt < 5; attempt++) {
      limiter.checkWorkspaceCreation("192.0.2.10");
    }

    ResponseStatusException error =
        assertThrows(
            ResponseStatusException.class, () -> limiter.checkWorkspaceCreation("192.0.2.10"));

    assertEquals(429, error.getStatus().value());
  }

  @Test
  void inviteRedemptionsHaveAnIndependentLimit() {
    GuestAccessRateLimiter limiter = new GuestAccessRateLimiter();
    for (int attempt = 0; attempt < 20; attempt++) {
      limiter.checkInviteRedemption("192.0.2.11");
    }

    ResponseStatusException error =
        assertThrows(
            ResponseStatusException.class, () -> limiter.checkInviteRedemption("192.0.2.11"));

    assertEquals(429, error.getStatus().value());
  }

  @Test
  void capsVisitorTrackingRequestsByNetworkAddress() {
    GuestAccessRateLimiter limiter = new GuestAccessRateLimiter();
    for (int attempt = 0; attempt < 300; attempt++) {
      limiter.checkVisitorTracking("192.0.2.12");
    }

    ResponseStatusException error =
        assertThrows(
            ResponseStatusException.class, () -> limiter.checkVisitorTracking("192.0.2.12"));

    assertEquals(429, error.getStatus().value());
  }
}
