package com.app.projectory.dto;

import javax.persistence.Column;

public interface BlogPeekViewListDto {
	long getId();
	String getTitle();
	String getImageLink();
	String getShortDescription();
	String getTextContent();
}