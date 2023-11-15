package com.app.projectory.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.projectory.dto.ProjectDto;
import com.app.projectory.dto.ProjectMembersDto;
import com.app.projectory.dto.ProjectStatusCount;
import com.app.projectory.dto.ProjectTasksDto;
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
	
//	//count projectlabels
//		@Query(nativeQuery=true, value="SELECT STATUS as statusLabel, COUNT(STATUS) as labelCount FROM PROJECT p\n"
//				+ "LEFT JOIN project_members pm on pm.user_id = ?1\n"
//				+ "WHERE p.project_owner_user_id = ?1 OR p.project_id = pm.project_id \n"
//				+ "GROUP BY STATUS ORDER BY statusLabel")
//		List<ProjectStatusCount> countAllProjectStatus(long userId);
	
	//count project status labels
	@Query(nativeQuery=true, value="SELECT statusLabel, SUM(labelCount) AS labelCount\n"
			+ "FROM (SELECT p.status AS statusLabel, COUNT(p.status) AS labelCount\n"
			+ "FROM PROJECT p \n"
			+ "	  WHERE p.project_owner_user_id = ?1\n"
			+ "    GROUP BY statusLabel\n"
			+ "UNION ALL\n"
			+ "    SELECT p.status AS statusLabel, COUNT(p.status) AS labelCount\n"
			+ "    FROM PROJECT p\n"
			+ "    LEFT JOIN project_members pm ON p.project_id = pm.project_id\n"
			+ "    WHERE pm.user_id = ?1\n"
			+ "    GROUP BY statusLabel) AS subquery\n"
			+ "GROUP BY statusLabel\n"
			+ "ORDER BY statusLabel")
	List<ProjectStatusCount> countAllProjectStatus(long userId);
	
	
	//count project me
	
	/*
	 * @Query(value = "SELECT * FROM project p WHERE p.project_owner_user_id = ?1",
	 * nativeQuery = true) List<Project> findProjectListByUser(long userId);
	 */
	
	@Query(value = "SELECT * FROM project p \n"
			+ "	LEFT JOIN project_members m ON m.user_id = ?1\n"
			+ "	WHERE p.project_owner_user_id = ?1 OR p.project_id = m.project_id", nativeQuery = true)
	List<Project> findProjectListByUser(long userId); 
	
	@Query(value = "SELECT DISTINCT p.*, project_owner_user_id as projectOwnerUserId,\n"
			+ "creation_date as creationDate, username as projectOwnerUsername, \n"
			+ "p.project_id as projectId FROM project p \n"
			+ "LEFT JOIN project_members m ON m.user_id = ?1\n"
			+ "LEFT JOIN Users us on us.user_id = p.project_owner_user_id\n"
			+ "WHERE p.project_owner_user_id = ?1 OR p.project_id = m.project_id", nativeQuery = true)
	List<ProjectDto> findProjectListByUserIncUsername(long userId); 
	
	
//	@Query(value = "SELECT pt.task_id taskId, pt.deadline, pt.status, pt.task_description taskDescription, \n"
//			+ "pt.task_name taskName\n"
//			+ "FROM project_tasks pt\n"
//			+ "LEFT JOIN project p on p.project_id = ?1 AND p.project_owner_user_id = ?2\n"
//			+ "LEFT JOIN project_members pm on pm.project_id = ?1 AND pm.user_id = ?2\n"
//			+ "WHERE pt.project_id = ?1 AND (pm.user_id = ?2 OR p.project_owner_user_id = ?2)", nativeQuery = true)
//	List<ProjectTasksDto> findProjectTasksByProject(long projectId, long userId); 
	
	//improving the above comented query
	@Query(value = "SELECT pt.task_id taskId, pt.deadline, pt.status, pt.task_description taskDescription, \n"
			+ "pt.task_name taskName, pt.project_id projectId, pt.assignee assigneeId, p.creation_date projectCreationDate,\n"
			+ "us.username assigneeUsername\n"
			+ "FROM project_tasks pt\n"
			+ "LEFT JOIN project p on p.project_id = ?1 AND p.project_owner_user_id = ?2\n"
			+ "LEFT JOIN project_members pm on pm.project_id = ?1 AND pm.user_id = ?2\n"
			+ "LEFT JOIN users us on us.user_id = pt.assignee\n"
			+ "WHERE pt.project_id = ?1 AND (pm.user_id = ?2 OR p.project_owner_user_id = ?2)", nativeQuery = true)
	List<ProjectTasksDto> findProjectTasksByProject(long projectId, long userId); 
	
	
	//find project members
	@Query(value = "SELECT DISTINCT us.username, us.user_id projectMemberUserId,\n"
			+ "us.first_name firstName, us.last_name lastName,\n"
			+ "p.project_owner_user_id projectOwnerUserId\n"
			+ "	FROM users us\n"
			+ "	LEFT JOIN project_members pm on pm.project_id = ?1\n"
			+ "	LEFT JOIN project p ON p.project_id = ?1 \n"
			+ "	WHERE (us.user_id = pm.user_id OR us.user_id = p.project_owner_user_id)\n"
			+ "	AND (pm.user_id = ?2 OR p.project_owner_user_id = ?2)", nativeQuery = true)
	List<ProjectMembersDto> findProjectMembersByProjectId(long projectId, long userId); 
	
	
	
	//does project has members
	@Query(value = "SELECT count(*) \n"
			+ "WHERE EXISTS(SELECT 1 FROM project_members pm \n"
			+ "WHERE pm.project_id = ?1)", nativeQuery = true)
	int projectHasMembers(long projectId); 
	
	
	
	//count all projects
//	@Query(value = "Select COUNT(*) FROM(\n"
//			+ "SELECT p.*, project_owner_user_id as projectOwnerUserId,\n"
//			+ "creation_date as creationDate, username as projectOwnerUsername FROM project p \n"
//			+ "LEFT JOIN project_members m ON m.user_id = ?1\n"
//			+ "Left Join Users us on us.user_id = p.project_owner_user_id\n"
//			+ "WHERE p.project_owner_user_id = ?1 OR p.project_id = m.project_id\n"
//			+ "Group by p.project_id, us.username\n"
//			+ ") AS pCount", nativeQuery = true)
//	long countProjectForUser(long userId); 
	
	//the above commented one improved
	@Query(value = "Select DISTINCT COUNT(*) AllProjectsCount FROM(\n"
			+ "SELECT DISTINCT p.* FROM project p \n"
			+ "LEFT JOIN project_members m ON m.user_id =?1\n"
			+ "WHERE p.project_owner_user_id = ?1 OR p.project_id = m.project_id\n"
			+ ") AS pCount", nativeQuery = true)
	long countProjectForUser(long userId); 
	
	//count OWN projects
	@Query(value = "Select COUNT(*) ProjectsCreatedByUserCount FROM(\n"
			+ "SELECT DISTINCT p.* FROM project p \n"
			+ "WHERE p.project_owner_user_id = ?1\n"
			+ ") AS pCount", nativeQuery = true)
	long countProjectsCreatedByUser(long userId); 
	
	
	//count Joined projects
		@Query(value = "Select COUNT(*) JoinedProjectsCount FROM(\n"
				+ "SELECT DISTINCT p.* FROM project p \n"
				+ "LEFT JOIN project_members m ON m.user_id =?1\n"
				+ "WHERE p.project_id = m.project_id\n"
				+ ") AS pCount", nativeQuery = true)
		long countJoinedProjects(long userId); 
		
		
	
	@Query(value = "SELECT DISTINCT p.project_id projectId, p.title, p.description, \n"
			+ "p.status, p.creation_date creationDate, \n"
			+ "us.username projectOwnerUsername \n"
			+ "FROM project p\n"
			+ "LEFT JOIN users us on us.user_id = p.project_owner_user_id\n"
			+ "LEFT JOIN users us2 on  us2.username = ?1 \n"
			+ "LEFT JOIN project_members pm on pm.user_id = us2.user_id\n"
			+ "WHERE p.project_owner_user_id = us2.user_id OR p.project_id = pm.project_id", nativeQuery=true)
	public List<PublicUserProjectDetailDto> findPublicUserProjects(String username);	
	
	
	//@Query(nativeQuery=true, value="Select COUNT(projects_id) as projectCount from PROJECT ")

//	
//	//add project member
//	@Transactional
//	@Modifying
//	//add project members
//	@Query(value="INSERT INTO project_members(user_id, project_id) \n"
//			+ "SELECT ?2, ?1\n"
//			+ "WHERE NOT EXISTS(SELECT 1 \n"
//			+ "FROM project_members pm \n"
//			+ "WHERE pm.user_id =?2 \n"
//			+ " AND pm.project_id = ?1)", nativeQuery = true)
//	int addProjectMember(long projectId, long userId);
//	

	
	//add project member
	@Transactional
	@Modifying
	@Query(value="INSERT INTO project_members(user_id, project_id) \n"
			+ "SELECT ?2, ?1\n"
			+ "WHERE NOT EXISTS(SELECT 1 \n"
			+ "FROM project_members pm \n"
			+ "WHERE pm.user_id = ?2 \n"
			+ "AND pm.project_id = ?1)", nativeQuery = true)
	int addProjectMember(long projectId, long userId, long authUserId);
	
	//remove project members
	@Transactional
	@Modifying	
	@Query(value="DELETE FROM project_members\n"
			+ "	WHERE (user_id = ?2 AND project_id = ?1)\n"
			+ "AND EXISTS(SELECT 1 FROM project p\n"
			+ "LEFT JOIN project_members pm on pm.project_id = ?1\n"
			+ "WHERE p.project_owner_user_id = ?3 OR pm.user_id = ?3)", nativeQuery = true)
	int removeProjectMember(long projectId, long userId, long authUserId);
	
	
	@Transactional
	@Modifying
	@Query(value="DELETE FROM project p \n"
			+ "WHERE p.project_id = ?1\n"
			+ "AND p.project_owner_user_id = ?2", nativeQuery = true)
	int deleteProject(long projectId, long projectOwner);
	
	@Transactional
	@Modifying
	@Query(value="DELETE FROM project_members\n"
			+ "WHERE project_id = ?1\n"
			+ "AND EXISTS(SELECT 1 from project\n"
			+ "where project_owner_user_id = ?2)", nativeQuery = true)
	int deleteProjectMembersOfaProject(long projectId, long authProjectOwner);
	
	
	
	
	
}
	