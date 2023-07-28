package com.app.projectory;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.app.projectory.doa.ProjectRepository;
import com.app.projectory.doa.UsersRepository;
import com.app.projectory.entity.Project;
import com.app.projectory.entity.Users;

@SpringBootApplication
public class ProjectoryApp {
	@Autowired
	ProjectRepository projectRepo;
	@Autowired
	UsersRepository userRepo;

	public static void main(String[] args) {
		SpringApplication.run(ProjectoryApp.class, args);
	}
	
	@Bean
	CommandLineRunner runner() {
		return args -> {
			
			Users emp1 = new Users("testuser1", "testuser123", "Test", "User1", "test1@user.com");
			Users emp2 = new Users("testuser2", "testuser123", "Test", "User2", "test2@user.com");
			Users emp3 = new Users("testuser3", "testuser123", "Test", "User3", "test3@user.com");
			
			
			Project proj1 = new Project("Project Management System", "implement a web app to manage projects", "Not started");
			Project proj2 = new Project("File Sharing Web App", "implement a web app to share files", "Not started");
			Project proj3 = new Project("Human Resource Management System", "implement a web app to manage employees", "Not started");
			
			emp1.setJoinedProjects(Arrays.asList(proj3));
			emp2.setJoinedProjects(Arrays.asList(proj1, proj2));			
			emp3.setJoinedProjects(Arrays.asList(proj1, proj2, proj3));
			
			
			proj1.setProjectMembers(Arrays.asList(emp2, emp3));
			proj2.setProjectMembers(Arrays.asList(emp2, emp3));
			proj3.setProjectMembers(Arrays.asList(emp1, emp3));
		
			
//			emp1.setOwnedProjects(Arrays.asList(proj1, proj2));
//			emp2.setOwnedProjects(Arrays.asList(proj3));
			
			proj1.setProjectOwner(emp1);
			proj2.setProjectOwner(emp1);
			proj3.setProjectOwner(emp2);
			
				
						
			
			
			userRepo.save(emp1);
			userRepo.save(emp2);
			userRepo.save(emp3);
			
			projectRepo.save(proj1);
			projectRepo.save(proj2);
			projectRepo.save(proj3);
			
			
		};
	}

}
