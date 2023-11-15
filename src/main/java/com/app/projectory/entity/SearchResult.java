package com.app.projectory.entity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SearchResult {
	private long searchItemId;
	private String searchResultType;
	private String searchItemTitle;
	private String searchItemDescription;
	
	
	public SearchResult() {
		super();

	}
	public SearchResult(long searchItemId, String searchResultType, String searchItemTitle,
			String searchItemDescription) {
		super();
		this.searchItemId = searchItemId;
		this.searchResultType = searchResultType;
		this.searchItemTitle = searchItemTitle;
		this.searchItemDescription = searchItemDescription;
	}
	public long getSearchItemId() {
		return searchItemId;
	}
	public void setSearchItemId(long searchItemId) {
		this.searchItemId = searchItemId;
	}
	public String getSearchResultType() {
		return searchResultType;
	}
	public void setSearchResultType(String searchResultType) {
		this.searchResultType = searchResultType;
	}
	public String getSearchItemTitle() {
		return searchItemTitle;
	}
	public void setSearchItemTitle(String searchItemTitle) {
		this.searchItemTitle = searchItemTitle;
	}
	public String getSearchItemDescription() {
		return searchItemDescription;
	}
	public void setSearchItemDescription(String searchItemDescription) {
		this.searchItemDescription = searchItemDescription;
	}	
	
	@Bean
	public SearchResult injectableSearchResultObj() {
		return new SearchResult();
	}
}
