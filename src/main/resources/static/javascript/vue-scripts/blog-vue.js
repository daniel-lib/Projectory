const getFullBlogContent = "/link"

Vue.createApp({
	
		data(){
		return{
			currentBlogBody: ''
		}
	},
	created(){
		
	},
	methods:{
		expandBlogPost(event){
			const blogId = event.target.id;
			const blogPostContainer = document.getElementById("blog-post-container-"+blogId);
			const triggerBtn = document.querySelector("#blog-post-container-"+blogId+" .blog-post-container-expanded-toggle");
			const blogFullContent = document.querySelector("#blog-post-container-"+blogId+" .blog-full-content")
			if(blogPostContainer.classList.contains("expanded")){
				this.collapseBlogPost(blogPostContainer, triggerBtn, blogFullContent)	
			}
			else{		
			blogPostContainer.classList.add("expanded");
			blogFullContent.classList.add("expanded");
			document.body.classList.add("body-fixed");			
			triggerBtn.textContent = "Exit";	
			fetch(getFullBlogContent)
				.then(response => response.json())
				.then(data => this.currentBlogBody = data)
				.catch(this.currentBlogBody = "Something went wrong while trying to load the blog. please try again.")
						
			}
		
		},
		collapseBlogPost(expandedBlogPostContainer, triggerBtn, blogFullContent){
			triggerBtn.textContent = "Continue Reading";
			expandedBlogPostContainer.classList.remove("expanded");
			blogFullContent.classList.remove("expanded");
			document.body.classList.remove("body-fixed");
		}
	}
}
).mount("#blog-post-area")