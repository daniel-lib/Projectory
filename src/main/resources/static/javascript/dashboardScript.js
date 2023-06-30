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
document.addEventListener()
function sideMenuToggle() {
	
	const sideMenu = document.querySelector("#side-menu");
	const toggle = document.querySelector("#side-menu-toggle");
	const sideNav = document.querySelector(".side-nav");
	const menuDirection = document.querySelector("#side-menu-direction");
	const sideNavText = document.getElementsByClassName("side-nav-text");
	const left = document.getElementById("side-nav-right-arrow");
	const right = document.getElementById("side-nav-left-arrow");
	const toggleArrow = [document.getElementsByClassName("fa-caret-left"), document.getElementsByClassName("fa-caret-right")];
	
	
	if (isCollapsed) {
		/*const sideNav = document.select*/
		sideNav.style = "padding-right: 100px !important;";
		sideNav.style = "width: 200px;";
		/*toggle.style.letterSpacing = "-0.1em";*/
		toggle.style.letterSpacing = "0em";
		for (let t of sideNavText) {
			/*alert(t.textContent);*/
			t.style.display = "inline";
			t.style.color = "red !important";
		}
		left.style.display = "inline";
		right.style.display  = "none";
		/*//this two lines of code only appy padding. it ignores border change. lookup explanation
		sideNav.style.border = "3px solid red";
		sideNav.style = "padding-right: 1px !important";*/
		
		isCollapsed = false;
			
		
	}
	else {
		sideNav.style = "padding-right: 1px !important;";
		for (let t of sideNavText) {
			t.style.display = "none";
		}
		toggle.style.letterSpacing = "-0.1em"
		isCollapsed = true;
	}
	//sideMenu.ariaLabel = "folded"


	/*sideNav.style.width = "50px"
	sideNav.style.border = "3px solid red";*/

}
