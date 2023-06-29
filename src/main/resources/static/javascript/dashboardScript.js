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
var isCollapsed = false;
function sideMenuToggle() {
	
	const sideMenu = document.querySelector("#side-menu");
	const sideNav = document.querySelector(".side-nav");
	const sideNavText = document.getElementsByClassName("side-nav-text");
	
	if (!isCollapsed) {
		/*const sideNav = document.select*/
		for (let t of sideNavText) {
			t.style.display = "none";
		}
		/*//this two lines of code only appy padding. it ignores border change. lookup explanation
		sideNav.style.border = "3px solid red";
		sideNav.style = "padding-right: 1px !important";*/
		sideNav.style = "padding-right: 1px !important";
		isCollapsed = true;
	}
	else {
		sideNav.style = "padding-right: 100px !important;";
		for (let t of sideNavText) {
			t.style.display = "inline";
		}
		isCollapsed = false;
	}
	//sideMenu.ariaLabel = "folded"


	/*sideNav.style.width = "50px"
	sideNav.style.border = "3px solid red";*/

}
