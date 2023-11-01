package com.app.projectory.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.app.projectory.entity.ProjectTasks;

@Repository
public interface ProjectTaskRepository extends CrudRepository<ProjectTasks, Long>{
	/* public Project findById(long projId); */
	List<ProjectTasks> findAll();
	
	@Modifying
	@Transactional
	@Query(value="UPDATE project_tasks\n"
			+ "SET status=?2\n"
			+ "WHERE project_tasks.task_id = ?1 AND EXISTS(\n"
			+ "SELECT 1 FROM project p \n"
			+ "	LEFT JOIN project_tasks pt on pt.task_id = ?1\n"
			+ "	LEFT JOIN project_members pm on pm.project_id = pt.project_id\n"
			+ "	WHERE (p.project_owner_user_id = ?3 AND p.project_id = pt.project_id) OR pm.user_id = ?3\n"
			+ ");", nativeQuery = true)
	int updateProjectStaus(long taskId, String newStatus, long userId);
}
