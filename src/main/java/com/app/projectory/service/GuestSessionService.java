package com.app.projectory.service;

import com.app.projectory.dao.GuestParticipantRepository;
import com.app.projectory.dao.GuestWorkspaceRepository;
import com.app.projectory.entity.GuestParticipant;
import com.app.projectory.entity.GuestWorkspace;
import com.app.projectory.security.GuestPrincipal;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;

@Service
public class GuestSessionService {
  private static final SecureRandom RANDOM = new SecureRandom();
  private final Cache<String, SessionData> sessions =
      Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.HOURS).maximumSize(10000).build();
  private final GuestParticipantRepository participantRepository;
  private final GuestWorkspaceRepository workspaceRepository;

  public GuestSessionService(
      GuestParticipantRepository participantRepository,
      GuestWorkspaceRepository workspaceRepository) {
    this.participantRepository = participantRepository;
    this.workspaceRepository = workspaceRepository;
  }

  public String createSession(GuestParticipant participant, Instant workspaceExpiry) {
    byte[] tokenBytes = new byte[32];
    RANDOM.nextBytes(tokenBytes);
    String token = Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    sessions.put(
        hash(token),
        new SessionData(
            participant.getWorkspaceId(), participant.getParticipantId(), workspaceExpiry));
    return token;
  }

  public Optional<GuestPrincipal> resolve(String token) {
    if (token == null || token.trim().isEmpty()) {
      return Optional.empty();
    }
    String key = hash(token);
    SessionData session = sessions.getIfPresent(key);
    if (session == null) {
      return Optional.empty();
    }
    if (!session.expiresAt.isAfter(Instant.now())) {
      sessions.invalidate(key);
      return Optional.empty();
    }

    GuestWorkspace workspace = workspaceRepository.findById(session.workspaceId).orElse(null);
    GuestParticipant participant =
        participantRepository.findById(session.participantId).orElse(null);
    if (workspace == null
        || !workspace.getExpiresAt().isAfter(Instant.now())
        || participant == null
        || !session.workspaceId.equals(participant.getWorkspaceId())) {
      sessions.invalidate(key);
      return Optional.empty();
    }
    return Optional.of(
        new GuestPrincipal(
            participant.getParticipantId(),
            participant.getWorkspaceId(),
            participant.getDisplayName()));
  }

  public void revoke(String token) {
    if (token != null && !token.trim().isEmpty()) {
      sessions.invalidate(hash(token));
    }
  }

  public static String hash(String value) {
    try {
      byte[] digest =
          MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
      return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    } catch (NoSuchAlgorithmException error) {
      throw new IllegalStateException("SHA-256 is not available", error);
    }
  }

  private static final class SessionData {
    private final String workspaceId;
    private final String participantId;
    private final Instant expiresAt;

    private SessionData(String workspaceId, String participantId, Instant expiresAt) {
      this.workspaceId = workspaceId;
      this.participantId = participantId;
      this.expiresAt = expiresAt;
    }
  }
}
