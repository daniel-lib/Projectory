package com.app.projectory.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.projectory.dto.BlogPeekViewListDto;
import com.app.projectory.entity.Blog;

@Repository
public interface BlogRepository extends CrudRepository<Blog, Long>{
	List<Blog> findAll();
	
	Blog findByTitle(String title);
	
	@Query(value="SELECT id, title, short_description ShortDescription FROM blog", nativeQuery=true)
	List<BlogPeekViewListDto> findBlogPeakViewList();
	@Query(value="SELECT id, title, short_description shortDescription, \n"
			+ "text_content textContent, image_link imageLink \n"
			+ "	FROM blog\n"
			+ "	ORDER BY ratings DESC\n"
			+ "	LIMIT ?1", nativeQuery=true)
	List<BlogPeekViewListDto> findLatestBlogsPeekView(int numberOfBlogs);
	
	

	@Query(value="SELECT id, title, short_description shortDescription, \n"
			+ "text_content textContent, image_link imageLink \n"
			+ "	FROM blog\n"
			+ "	ORDER BY ratings DESC", nativeQuery=true)
	List<BlogPeekViewListDto> findAllBlogsPeekView();
	
	@Query(value="SELECT id, title, short_description shortDescription, \n"
			+ "text_content textContent, image_link imageLink \n"
			+ "	FROM public.blog\n"
			+ "	Order by ratings DESC\n"
			+ "	offset ?1", nativeQuery=true)
	List<BlogPeekViewListDto> findAllExceptMostViewedBlogsPeekView(int offset);
	
}
