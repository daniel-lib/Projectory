package com.app.projectory.dao;

import com.app.projectory.dto.CurrentUserDetailDto;
import com.app.projectory.dto.PublicUserPersonalDetailDto;
import com.app.projectory.entity.Users;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends CrudRepository<Users, Long> {

  // String findByUsername(String username);
  Users findByUsername(String username);

  List<Users> findAll();

  @Query(value = "SELECT * FROM users u WHERE u.username = ?1", nativeQuery = true)
  Users findUserByUsername(String username);

  @Query(
      value =
          "SELECT first_name firstName, last_name lastName, user_id userId, online_status onlineStatus, \n"
              + "username, profile_picture profilePicture\n"
              + "FROM users WHERE username = ?1",
      nativeQuery = true)
  PublicUserPersonalDetailDto getPublicUserDetail(String username);

  @Query(
      value =
          "SELECT user_id userId, email_address emailAddress, enabled, \n"
              + "first_name firstName, last_name lastName, online_status onlineStatus, \n"
              + "role, username, profile_picture profilePicture\n"
              + "FROM users\n"
              + "WHERE user_id = ?1",
      nativeQuery = true)
  CurrentUserDetailDto getCurrentUserDetail(long userId);

  @Query(
      value =
          "SELECT u.user_id userId, u.first_name firstName, u.last_name lastName,\n"
              + "u.username username, u.profile_picture profilePicture, u.online_status onlineStatus\n"
              + "FROM users u\n"
              + "WHERE u.user_id <> ?1\n"
              + "AND NOT EXISTS (\n"
              + "  SELECT 1 FROM connections c\n"
              + "  WHERE (c.request_sender_user = ?1 AND c.request_receiver_user = u.user_id)\n"
              + "     OR (c.request_receiver_user = ?1 AND c.request_sender_user = u.user_id)\n"
              + ")\n"
              + "ORDER BY u.user_id DESC\n"
              + "LIMIT 8",
      nativeQuery = true)
  List<PublicUserPersonalDetailDto> findSuggestedConnections(long userId);
}
