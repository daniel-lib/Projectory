package com.app.projectory.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentMap;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class GuestAccessRateLimiter {
  private static final Duration WINDOW = Duration.ofHours(1);
  private static final int MAX_WORKSPACE_CREATIONS = 5;
  private static final int MAX_INVITE_REDEMPTIONS = 20;
  private static final int MAX_VISITOR_TRACKING_REQUESTS = 300;

  private final Cache<String, Bucket> buckets =
      Caffeine.newBuilder().expireAfterAccess(WINDOW).maximumSize(100000).build();

  public void checkWorkspaceCreation(String ipAddress) {
    check("workspace:", ipAddress, MAX_WORKSPACE_CREATIONS);
  }

  public void checkInviteRedemption(String ipAddress) {
    check("invite:", ipAddress, MAX_INVITE_REDEMPTIONS);
  }

  public void checkVisitorTracking(String ipAddress) {
    check("visitor:", ipAddress, MAX_VISITOR_TRACKING_REQUESTS);
  }

  private void check(String action, String ipAddress, int limit) {
    Instant now = Instant.now();
    String key = action + GuestSessionService.hash(ipAddress == null ? "unknown" : ipAddress);
    ConcurrentMap<String, Bucket> map = buckets.asMap();
    Bucket bucket =
        map.compute(
            key,
            (ignored, current) ->
                current == null || !current.startedAt.plus(WINDOW).isAfter(now)
                    ? new Bucket(now)
                    : current);
    synchronized (bucket) {
      if (bucket.count >= limit) {
        throw new ResponseStatusException(
            HttpStatus.TOO_MANY_REQUESTS,
            "Too many requests from this network. Please try again later.");
      }
      bucket.count++;
    }
  }

  private static final class Bucket {
    private final Instant startedAt;
    private int count;

    private Bucket(Instant startedAt) {
      this.startedAt = startedAt;
    }
  }
}
