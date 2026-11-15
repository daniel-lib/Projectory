package com.app.projectory.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.app.projectory.entity.Project;
import com.app.projectory.entity.ProjectTasks;
import com.app.projectory.entity.Users;
import java.util.Arrays;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest(
    properties = {"spring.jpa.hibernate.ddl-auto=create-drop", "spring.sql.init.mode=never"})
@AutoConfigureTestDatabase(replace = Replace.ANY)
class ProjectTaskRepositoryTest {

  @Autowired private EntityManager entityManager;

  @Autowired private ProjectTaskRepository taskRepository;

  private Users owner;
  private Users assignee;
  private Users otherMember;
  private Users outsider;
  private Project project;

  @BeforeEach
  void setUp() {
    owner = persistUser("task-owner");
    assignee = persistUser("task-assignee");
    otherMember = persistUser("other-member");
    outsider = persistUser("task-outsider");

    project = new Project();
    project.setTitle("Assignment test project");
    project.setStatus("In Progress");
    project.setProjectOwner(owner);
    project.setProjectMembers(Arrays.asList(assignee, otherMember));
    entityManager.persist(project);
    entityManager.flush();
  }

  @Test
  void onlyAssignedProjectMemberCanMoveTask() {
    ProjectTasks task = persistTask("not started", assignee.getUserId());

    assertEquals(
        0, taskRepository.updateProjectStatus(task.getTaskId(), "done", owner.getUserId()));
    assertEquals(
        0, taskRepository.updateProjectStatus(task.getTaskId(), "done", otherMember.getUserId()));
    assertEquals(
        1, taskRepository.updateProjectStatus(task.getTaskId(), "done", assignee.getUserId()));
    assertEquals("done", taskRepository.findById(task.getTaskId()).get().getStatus());
  }

  @Test
  void unassignedTaskCannotBeMoved() {
    ProjectTasks task = persistTask("not started", null);

    assertEquals(
        0, taskRepository.updateProjectStatus(task.getTaskId(), "done", owner.getUserId()));
    assertEquals("not started", taskRepository.findById(task.getTaskId()).get().getStatus());
  }

  @Test
  void assignmentCanOnlyTargetAProjectMemberOrOwner() {
    ProjectTasks task = persistTask("not started", null);

    assertEquals(
        0,
        taskRepository.updateProjectTaskAssignee(
            task.getTaskId(), outsider.getUserId(), owner.getUserId()));
    assertEquals(
        1,
        taskRepository.updateProjectTaskAssignee(
            task.getTaskId(), otherMember.getUserId(), owner.getUserId()));
    assertEquals(
        0, taskRepository.updateProjectStatus(task.getTaskId(), "done", assignee.getUserId()));
    assertEquals(
        1, taskRepository.updateProjectStatus(task.getTaskId(), "done", otherMember.getUserId()));
  }

  private Users persistUser(String username) {
    Users user = new Users();
    user.setUsername(username);
    user.setPassword("test-password");
    user.setRole("USER");
    user.setEnabled(true);
    entityManager.persist(user);
    return user;
  }

  private ProjectTasks persistTask(String status, Long assignedUserId) {
    ProjectTasks task = new ProjectTasks();
    task.setTaskName("Assignment test task");
    task.setTaskDescription("Test assignment authorization");
    task.setStatus(status);
    task.setAssignee(assignedUserId);
    task.setContainerProject(project);
    entityManager.persist(task);
    entityManager.flush();
    return task;
  }
}
