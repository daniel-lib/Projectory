package com.app.projectory.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.projectory.dto.ProjectDto;
import com.app.projectory.dto.ProjectStatusCount;
import com.app.projectory.dto.PublicUserProjectDetailDto;
import com.app.projectory.entity.Project;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long>{
	/* for*/
	@Override
	List<Project> findAll();
	
//	@Query(nativeQuery=true, value="SELECT STATUS as statusLabel, COUNT(STATUS) as labelCount FROM PROJECT "
//			+ "GROUP BY STATUS ORDER BY statusLabel")
//	List<ProjectStatusCount> countProjectStatus();
	
	//count labels
	@Query(nativeQuery=true, value="SELECT STATUS as statusLabel, COUNT(STATUS) as labelCount FROM PROJECT "
			+ " WHERE project_owner_user_id = ?1 GROUP BY STATUS ORDER BY statusLabel")
	List<ProjectStatusCount> countProjectStatus(long userId);
	
	//count project me
	
	/*
	 * @Query(value = "SELECT * FROM project p WHERE p.project_owner_user_id = ?1",
	 * nativeQuery = true) List<Project> findProjectListByUser(long userId);
	 */
	
	@Query(value = "SELECT * FROM project p \n"
			+ "	LEFT JOIN project_members m ON m.user_id = ?1\n"
			+ "	WHERE p.project_owner_user_id = ?1 OR p.project_id = m.project_id", nativeQuery = true)
	List<Project> findProjectListByUser(long userId); 
	
	@Query(value = "SELECT p.*, project_owner_user_id as projectOwnerUserId,\n"
			+ "creation_date as creationDate, username as projectOwnerUsername, \n"
			+ "p.project_id as projectId FROM project p \n"
			+ "LEFT JOIN project_members m ON m.user_id = ?1\n"
			+ "Left Join Users us on us.user_id = p.project_owner_user_id\n"
			+ "WHERE p.project_owner_user_id = ?1 OR p.project_id = m.project_id", nativeQuery = true)
	List<ProjectDto> findProjectListByUserIncUsername(long userId); 
	
	
	@Query(value = "Select COUNT(*) FROM(\n"
			+ "SELECT p.*, project_owner_user_id as projectOwnerUserId,\n"
			+ "creation_date as creationDate, username as projectOwnerUsername FROM project p \n"
			+ "LEFT JOIN project_members m ON m.user_id = 1\n"
			+ "Left Join Users us on us.user_id = p.project_owner_user_id\n"
			+ "WHERE p.project_owner_user_id = 1 OR p.project_id = m.project_id\n"
			+ "Group by p.project_id, us.username\n"
			+ ") AS pCount", nativeQuery = true)
	long countProjectForUser(long userId); 
	
	
	
	@Query(value = "SELECT DISTINCT p.project_id projectId, p.title, p.description, \n"
			+ "p.status, p.creation_date creationDate, \n"
			+ "us.username projectOwnerUsername \n"
			+ "FROM project p\n"
			+ "LEFT JOIN users us on us.user_id = p.project_owner_user_id\n"
			+ "LEFT JOIN users us2 on  us2.username = '?1' \n"
			+ "LEFT JOIN project_members pm on pm.user_id = us2.user_id\n"
			+ "WHERE p.project_owner_user_id = us2.user_id OR p.project_id = pm.project_id", nativeQuery=true)
	public List<PublicUserProjectDetailDto> findPublicUserProjects(String username);	
	
	
	//@Query(nativeQuery=true, value="Select COUNT(projects_id) as projectCount from PROJECT ")
	
	
}
	