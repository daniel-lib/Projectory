package expArea.projectView;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectViewManufacture {
	@Bean
	public ProjectView newProjectView() {
		ViewedProjects viewedProjects = new ViewedProjects();
		ProjectView v = new ProjectView(viewedProjects);
		return v;
	}
}
