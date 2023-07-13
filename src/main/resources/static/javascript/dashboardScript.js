function displayViewingModal(contentType) {
	const modalContainer = document.getElementById("viewing-modal");
	const contents = [document.getElementById("todo-list-modal-content"), document.getElementById("project-list-modal-content")];

	modalContainer.style.visibility = "visible";
	modalContainer.style.opacity = "1";

	if (contentType >= 0 || contentType <= (contents.length) - 1) {
		contents[contentType].style.display = "block";
	}

}
function closeViewingModal() {
	
	//change the state of "Create Project" button to default state
	//createItemButtonToDefault();
	const projectForm = document.getElementById("add-project-form");
	const createProjectBtn = document.getElementsByClassName("create-project-btn");
	projectForm.style.height = 0;
	projectForm.style.padding = "0";
	for(let btn of createProjectBtn){
		btn.value="Create Project";
	}
	
	//change the state of "Add Item" button to default state
	const todoItemForm = document.getElementById("add-todo-item-form");
	const AddTodoItemBtn = document.getElementsByClassName("add-todo-item-btn");
	todoItemForm.style.height = 0;
	todoItemForm.style.padding = "0";
	for(let btn of AddTodoItemBtn){
		btn.value="Add Item";
	}
	
	
	var form = document.getElementById("viewing-modal");
	form.style.opacity = "0";
	form.style.visibility = "hidden";
	const modalContents = [document.getElementById("todo-list-modal-content"), document.getElementById("project-list-modal-content")];

	for (let c of modalContents) {
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
		//toggleArrow.style.rotate = "180deg";
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
		//toggleArrow.style.rotate = "0deg";
		isCollapsed = true;
	}
	//sideMenu.ariaLabel = "folded"


	/*sideNav.style.width = "50px"
	sideNav.style.border = "3px solid red";*/

}



//project list item count checker
const projectCount = document.getElementsByClassName("project-count");
for (let prcnt of projectCount) {
	if (prcnt.textContent == "0") {
		prcnt.innerHTML = "<br/><br/>There are no projects at the moment.";
	}
	else {
		prcnt.textContent = "";
	}
}

//top menu show/hide script
const topMenuContainer = document.getElementById("top-collapsable-menu-container");
const specificContentIds = ["notification-menu-content", "message-menu-content", "setting-menu-content"];

function showTopCollapsableMenu(type) {
	const selectedContent = document.getElementById(specificContentIds[type]);

	for (let specCont = 0; specCont < specificContentIds.length; specCont++) {
		topMenuContainer.style.padding = "0px";
		topMenuContainer.style.height = "0px";
		document.getElementById(specificContentIds[specCont]).style.display = "none";
	}
	topMenuContainer.style.padding = "30px";
	topMenuContainer.style.height = "300px";

	selectedContent.style.display = "block";

}

function collapseTopMenu() {

	topMenuContainer.style.padding = "0px";
	topMenuContainer.style.height = "0px";
	document.getElementById(specificContentIds[specCont]).style.display = "none";
}
	




/*
const containers = document.querySelectorAll(".project-modal-boxes");
//alert("heeeee");
const buttons = document.querySelectorAll(".expand-btn");


for (let container of containers) {
  let button = container.querySelector(".expand-btn");
  container.addEventListener("mouseover", function() {
	  //alert("heeeee");
	  button.style.backgroundColor = "rgb(118, 187, 118)";
	  button.style.color = "white";
	  button.style.boxShadow = "0 0 10px rgb(118, 187, 118)";
  });
  container.addEventListener("click", function() {
	  alert(this.id);
  });


 container.addEventListener("mouseout", function() {
    button.style.backgroundColor = "rgb(246, 245, 244)";
    button.style.color = "black";
    button.style.boxShadow = "none";
  });
}*/









/*container.addEventListener("mouseleave", function() {
  button.style.backgroundColor = "blue";
});
*/




/*const modalBoxes= document.getElementsByClassName("project-modal-boxes");
let count = 0;
for(let b of modalBoxes){
	
	window.localStorage.setItem("hovered", JSON.stringify(b));
	b.addEventListener("mouseover", modalBoxesHoverEffect);	
}

function modalBoxesHoverEffect(){
	//alert("hollering");
	const h = localStorage.getItem("hovered");
	alert(h.textContent);
	h.style.backgroundColor = "red";
	alert(h);
	
}*/



/*const modalBoxes= document.querySelectorAll(".project-modal-boxes");
const modalBoxesHoverEffect = (click) => {
	alert("boommmmmmm");
};
for(let b of modalBoxes){
modalBoxes.addEventListener('click', myFunction.bind(null, "hhh"));
}
*/

/*const button = document.getElementById('myButton');

const myFunction = (event) => {
  console.log('The button was clicked.');
};

button.addEventListener('click', myFunction.bind(null, 'myParameter'));*/

