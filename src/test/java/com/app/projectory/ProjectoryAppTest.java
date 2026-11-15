package com.app.projectory;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.app.projectory.dto.ProjectDto;
import com.app.projectory.dto.ProjectMembersDto;
import com.app.projectory.dto.PublicUserConnectionsDetailDto;
import com.app.projectory.entity.Notification;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import javax.servlet.ServletContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.TemplateSpec;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;

@SpringBootTest
class ProjectoryAppTest {

  @Autowired private SpringTemplateEngine templateEngine;
  @Autowired private ServletContext servletContext;

  @Test
  void contextLoads() {}

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

  @Test
  void redesignedErrorTemplatesRenderStatusSpecificContent() {
    WebContext context =
        new WebContext(
            new MockHttpServletRequest(), new MockHttpServletResponse(), servletContext, Locale.US);
    context.setVariable("errorCode", 405);
    String methodNotAllowed = templateEngine.process("pageError/error-default", context);
    assertTrue(methodNotAllowed.contains("class=\"public-site-page error-404-page\""));
    assertTrue(methodNotAllowed.contains("This action is not available here."));
    assertTrue(methodNotAllowed.contains(">405</p>"));

    context.setVariable("errorCode", 500);
    String serverError = templateEngine.process("pageError/error-default", context);
    assertTrue(serverError.contains("An unexpected detour interrupted your work."));
    assertTrue(serverError.contains(">500</p>"));

    String forbidden =
        templateEngine.process(
            "pageError/error-403",
            new WebContext(
                new MockHttpServletRequest(),
                new MockHttpServletResponse(),
                servletContext,
                Locale.US));
    assertTrue(forbidden.contains("This space is for signed-in members."));
    assertTrue(forbidden.contains(">403</p>"));
  }

  @Test
  void projectMemberPickerRendersConnectionAddAction() {
    PublicUserConnectionsDetailDto connection =
        new PublicUserConnectionsDetailDto() {
          @Override
          public long getConnectionId() {
            return 7;
          }

          @Override
          public String getConnectionStatus() {
            return "approved";
          }

          @Override
          public String getProfilePicture() {
            return "";
          }

          @Override
          public String getConnectedUserUsername() {
            return "teammate";
          }

          @Override
          public String getConnectedUserFirstName() {
            return "Team";
          }

          @Override
          public String getConnectedUserLastName() {
            return "Mate";
          }
        };
    ProjectMembersDto owner =
        new ProjectMembersDto() {
          @Override
          public String getFirstName() {
            return "Project";
          }

          @Override
          public String getLastName() {
            return "Owner";
          }

          @Override
          public String getUsername() {
            return "owner";
          }

          @Override
          public long getProjectMemberUserId() {
            return 1;
          }

          @Override
          public long getProjectOwnerUserId() {
            return 1;
          }
        };
    ProjectMembersDto member =
        new ProjectMembersDto() {
          @Override
          public String getFirstName() {
            return "Current";
          }

          @Override
          public String getLastName() {
            return "Member";
          }

          @Override
          public String getUsername() {
            return "currentmember";
          }

          @Override
          public long getProjectMemberUserId() {
            return 2;
          }

          @Override
          public long getProjectOwnerUserId() {
            return 1;
          }
        };
    WebContext context =
        new WebContext(
            new MockHttpServletRequest(), new MockHttpServletResponse(), servletContext, Locale.US);
    context.setVariable("projectId", 42L);
    context.setVariable("connectionsList", Collections.singletonList(connection));
    context.setVariable("projectMembers", Arrays.asList(owner, member));

    String html =
        templateEngine.process(
            new TemplateSpec(
                "user/project/project-layouts",
                Collections.singleton("add-member-checkbox-list"),
                TemplateMode.HTML,
                Collections.emptyMap()),
            context);

    assertTrue(html.contains("Team Mate"));
    assertTrue(html.contains(">teammate</span>"));
    assertTrue(html.contains("/project/addProjectMember?project=42"));
    assertTrue(html.contains("user=teammate"));
    assertTrue(html.contains("hx-trigger=\"change\""));
    assertTrue(html.contains("Current Member"));
    assertTrue(html.contains("/project/removeProjectMember?project=42"));
    assertTrue(html.contains("user=currentmember"));
  }

  @Test
  void projectDashboardCardNumbersProjectsStartingAtOne() {
    ProjectDto firstProject = projectDto(1L, "First project");
    ProjectDto secondProject = projectDto(2L, "Second project");
    WebContext context =
        new WebContext(
            new MockHttpServletRequest(), new MockHttpServletResponse(), servletContext, Locale.US);
    context.setVariable("project", Arrays.asList(firstProject, secondProject));
    context.setVariable("projectCount", 2);

    String html =
        templateEngine.process(
            new TemplateSpec(
                "user/project/project-layouts",
                Collections.singleton("project-dashboard-card"),
                TemplateMode.HTML,
                Collections.emptyMap()),
            context);

    assertTrue(html.contains("itemNumber\">1</span>"));
    assertTrue(html.contains("itemNumber\">2</span>"));
  }

  private ProjectDto projectDto(Long id, String title) {
    return new ProjectDto() {
      @Override
      public Long getProjectId() {
        return id;
      }

      @Override
      public String getTitle() {
        return title;
      }

      @Override
      public String getDescription() {
        return title + " description";
      }

      @Override
      public String getStatus() {
        return "In Progress";
      }

      @Override
      public String getCreationDate() {
        return "today";
      }

      @Override
      public String getProjectOwnerUsername() {
        return "owner";
      }
    };
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
