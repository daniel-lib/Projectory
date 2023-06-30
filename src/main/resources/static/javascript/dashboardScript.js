function displayViewingModal() {
	var form = document.getElementById("viewing-modal");
	//document.querySelector("#viewing-modal").style.height = "600px";		
	//form.style.display = "block";
	//form.style.height = "auto";
	form.style.visibility = "visible";
	form.style.opacity = "1";

}
function closeSigninForm() {
	var form = document.getElementById("viewing-modal");
	form.style.opacity = "0";
	form.style.visibility = "hidden";
	//form.style.height = "0px";		
	//form.style.display = "none";
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
	const toggleArrow = [document.getElementById("side-nav-left-arrow"), document.getElementById("side-nav-right-arrow")];
	
	
	if (isCollapsed) {
		/*const sideNav = document.select*/
		/*sideNav.style = "padding-right: 100px !important;";*/
		sideNav.style = "width: 200px;";
		/*toggle.style.letterSpacing = "-0.1em";*/
		toggle.style.letterSpacing = "0em";
		
		

		for (let t of sideNavText) {
			t.style.display = "inline";
		}
		toggleArrow[0].style.display = "inline";
		toggleArrow[1].style.display  = "none";
		isCollapsed = false;

		/*toggle.style.setProperty("letterSpacing", "0em", "hover");
		alert("byeeee");*/
		
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
		toggleArrow[0].style.display = "none";
		toggleArrow[1].style.display  = "inline";
		isCollapsed = true;
	}
	//sideMenu.ariaLabel = "folded"


	/*sideNav.style.width = "50px"
	sideNav.style.border = "3px solid red";*/

}
