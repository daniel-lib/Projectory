package com.app.projectory.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.app.projectory.dao.ProjectRepository;
import com.app.projectory.dao.ProjectTaskRepository;
import com.app.projectory.dao.UsersRepository;
import com.app.projectory.entity.Project;
import com.app.projectory.entity.ProjectTasks;
import com.app.projectory.service.userAccountService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;

@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {

  @Mock private ProjectRepository projectRepository;
  @Mock private ProjectTaskRepository projectTaskRepository;
  @Mock private UsersRepository usersRepository;
  @Mock private userAccountService userService;
  @InjectMocks private ProjectController controller;

  @Test
  void addingProjectTaskLeavesItUnassignedAndRefreshesTaskLists() {
    when(projectRepository.findById(12L)).thenReturn(Optional.of(new Project()));
    MockHttpServletResponse response = new MockHttpServletResponse();

    int result =
        controller.addProjectTask("New task", "Task details", 12L, "not started", null, response);

    ArgumentCaptor<ProjectTasks> taskCaptor = ArgumentCaptor.forClass(ProjectTasks.class);
    verify(projectTaskRepository).save(taskCaptor.capture());
    assertEquals(1, result);
    assertEquals(0L, taskCaptor.getValue().getAssignee());
    assertTrue(response.getHeader("HX-Trigger").contains("projectTasksChanged"));
  }
}
