package com.app.projectory.dto;

public interface SearchResultDto {
	long getSearchItemId();
	String getSearchResultType();
	String getSearchItemTitle();
	String getSearchItemDescription();
	
	
	/*
	 	SELECT *
	FROM project WHERE LOWER(title) LIKE('%dan%');
	
	
	SELECT *, 'project' as searchType
	FROM project WHERE LOWER(title) LIKE('%by%') OR LOWER(description) LIKE('%d%');
	
	SELECT * FROM users WHERE LOWER(first_name) LIKE('%dan%') OR LOWER(last_name) LIKE('%dan%') 
	OR LOWER(username) LIKE('%dan%')
	*/
}
