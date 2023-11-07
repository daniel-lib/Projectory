//get current page
(()=>{
	//console.log("dddd")
	const currentPage = window.location.pathname;
	let page = '';
	//alert("currently: "+currentPage)
	if(currentPage == "/features")
		page = "features";
	else if(currentPage == "/blog")
		page = "blog";
	else if(currentPage == "/about")
		page = "about";
	else if(currentPage == "/")
		page = "home";
	
	//alert(page);
	const selectedPage = document.querySelector("#home-top-nav ."+page);
	selectedPage.classList.add("selected");
	
}
)();