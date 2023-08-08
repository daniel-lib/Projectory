package com.app.projectory;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.app.projectory.dao.ProjectRepository;
import com.app.projectory.dao.UsersRepository;
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

	
//	@Bean
//	CommandLineRunner runner() {
//		return args -> {
//			
//			Users user1 = new Users("testuser1", "testuser123", "Test", "User1", "test1@user.com");
//			Users user2 = new Users("testuser2", "testuser123", "Test", "User2", "test2@user.com");
//			Users user3 = new Users("testuser3", "testuser123", "Test", "User3", "test3@user.com");
//			
//			
//			Project proj1 = new Project("Project Management System", "implement a web app to manage projects", "Not started");
//			Project proj2 = new Project("File Sharing Web App", "implement a web app to share files", "Not started");
//			Project proj3 = new Project("Human Resource Management System", "implement a web app to manage userloyees", "Not started");
//			
//			
//			proj1.setProjectOwner(user1);
//			proj2.setProjectOwner(user1);
//			proj3.setProjectOwner(user2);
//			
//			user1.setJoinedProjects(Arrays.asList(proj3));
//			user2.setJoinedProjects(Arrays.asList(proj1, proj2));			
//			user3.setJoinedProjects(Arrays.asList(proj1, proj2, proj3));
//			
//			
//			proj1.setProjectMembers(Arrays.asList(user2, user3));
//			proj2.setProjectMembers(Arrays.asList(user2, user3));
//			proj3.setProjectMembers(Arrays.asList(user1, user3));
//		
//			
////			user1.setOwnedProjects(Arrays.asList(proj1, proj2));
////			user2.setOwnedProjects(Arrays.asList(proj3));
//			
//			
//									
//			
//			
//			userRepo.save(user1);
//			userRepo.save(user2);
//			userRepo.save(user3);
//			
//			projectRepo.save(proj1);
//			projectRepo.save(proj2);
//			projectRepo.save(proj3);
//			
//			
//			
//		};
//	}

}
