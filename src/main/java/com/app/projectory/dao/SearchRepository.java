package com.app.projectory.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.projectory.dto.SearchResultDto;
import com.app.projectory.entity.Project;

@Repository
//public interface SearchRepository extends CrudRepository<SearchResult, Long>{
public interface SearchRepository extends CrudRepository<Project, Long>{
	
	@Query(value="SELECT project_id AS searchItemId, 'project' AS searchResultType, \n"
			+ "	CONCAT(title, ':   ', status)\n"
			+ "	 as searchItemTitle, description AS searchItemDescription\n"
			+ "	FROM project WHERE LOWER(title) LIKE('%?1%') OR LOWER(description) LIKE('%?1%');", nativeQuery=true)
	List<SearchResultDto> searchProjects(String keyword);
	
	
	@Query(value="SELECT user_id as searchItemId, 'user' as searchResultType, \n"
			+ "	CONCAT(first_name, ' ', last_name)\n"
			+ "	as searchItemTitle, username as searchItemDescription\n"
			+ "	FROM users WHERE LOWER(first_name) LIKE('%?1%') OR LOWER(last_name) LIKE('%?1%') \n"
			+ "	OR LOWER(username) LIKE('%?1%')", nativeQuery=true)
	List<SearchResultDto> searchUsers(String keyword);
	
	@Query(value="SELECT user_id as searchItemId, 'user' as searchResultType, \n"
			+ "	CONCAT(first_name, ' ', last_name)\n"
			+ "	as searchItemTitle, username as searchItemDescription\n"
			+ "	FROM users WHERE LOWER(first_name) LIKE(CONCAT('%',?1,'%')) OR LOWER(last_name) LIKE(CONCAT('%',?1,'%')) \n"
			+ "	OR LOWER(username) LIKE(CONCAT('%',?1,'%'))", nativeQuery=true)
	List<SearchResultDto> searchEverything(String keyword);
	
	
}
