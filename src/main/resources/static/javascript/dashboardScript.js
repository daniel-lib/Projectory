function displayViewingModal(contentType) {	
	const modalContainer = document.getElementById("viewing-modal");
	const contents = [document.getElementById("todo-list-modal-content"), document.getElementById("project-list-modal-content")];
	modalContainer.style.visibility = "visible";
	modalContainer.style.opacity = "1";
	
	if(contentType >=0 || contentType<=1){
		contents[contentType].style.display = "block";
	}
	

}
function closeSigninForm() {
	var form = document.getElementById("viewing-modal");
	form.style.opacity = "0";
	form.style.visibility = "hidden";
	const contents = [document.getElementById("todo-list-modal-content"), document.getElementById("project-list-modal-content")];
	
	for(let c of contents){
		c.style.display = "none";
	}
}

var isCollapsed = true;
/*const toggleHover = document.querySelector("#side-menu-toggle");
toggleHover.addEventListener("mouseenter", alert("applied"));
function applyHover(){
	
	if (!isCollapsed){
		toggleHover.style.backgroundColor = "red";
		toggleHover.style.letterSpacing = "0em";
	}
}*/
function sideMenuToggle() {
	
	const sideMenu = document.querySelector("#side-menu");
	const toggle = document.querySelector("#side-menu-toggle");
	const sideNav = document.querySelector(".side-nav");
	const sideNavText = document.getElementsByClassName("side-nav-text");
	const toggleArrow = document.getElementById("side-nav-arrow");
	const toggleArrowClass = ["fa-solid fa-caret-left", "fa-solid fa-caret-right"];
	
	
	if (isCollapsed) {
		/*const sideNav = document.select*/
		/*sideNav.style = "padding-right: 100px !important;";*/
		sideNav.style = "width: 200px;";
		/*toggle.style.letterSpacing = "-0.1em";*/
		toggle.style.letterSpacing = "0em";
		
		

		for (let t of sideNavText) {
			t.style.display = "inline";
		}
		toggleArrow.setAttribute("class", toggleArrowClass[0]);
		isCollapsed = false;

		/*toggle.style.setProperty("letterSpacing", "0em", "hover");*/
				
		/*//this two lines of code only appy padding. it ignores border change. lookup explanation
		sideNav.style.border = "3px solid red";
		sideNav.style = "padding-right: 1px !important";*/		
	}
	else {
		/*sideNav.style = "padding-right: 1px !important;";*/
		sideNav.style = "width: 45px;";
		for (let t of sideNavText) {
			t.style.display = "none";
		}
		toggle.style.letterSpacing = "-0.1em";
		toggleArrow.setAttribute("class", toggleArrowClass[1]);
		isCollapsed = true;
	}
	//sideMenu.ariaLabel = "folded"


	/*sideNav.style.width = "50px"
	sideNav.style.border = "3px solid red";*/

}
