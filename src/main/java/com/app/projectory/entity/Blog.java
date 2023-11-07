package com.app.projectory.entity;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Blog {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String title;
	private String imageLink;
	@Column(columnDefinition = "TEXT")
	private String shortDescription;
	@Column(columnDefinition = "TEXT")
	private String textContent;
	private long ratings;
	
	
	
	public Blog() {
		super();
	}


	public Blog(String title, String imageLink, String shortDescription, String textContent, long ratings) {
		super();
		this.title = title;
		this.imageLink = imageLink;
		this.shortDescription = shortDescription;
		this.textContent = textContent;
		this.ratings = ratings;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageLinks() {
		return imageLink;
	}


	public void setImageLinks(String imageLink) {
		this.imageLink = imageLink;
	}


	public String getTextContent() {
		return textContent;
	}


	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

	public String getImageLink() {
		return imageLink;
	}



	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}


	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public long getRatings() {
		return ratings;
	}


	public void setRatings(long ratings) {
		this.ratings = ratings;
	}
	
	
}
