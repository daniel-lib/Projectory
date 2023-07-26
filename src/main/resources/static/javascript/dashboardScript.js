function displayViewingModal(contentType) {
	/*const modalContainer = document.getElementById("viewing-modal");*/
	const modalContainer = document.getElementById("viewing-modal-container");
	const contents = [document.getElementById("todo-list-modal-content"), document.getElementById("project-list-modal-content")];

	modalContainer.style.visibility = "visible";
	modalContainer.style.opacity = "1";

	if (contentType >= 0 || contentType <= (contents.length) - 1) {
		contents[contentType].style.display = "block";
		document.body.classList.add('overflow-hidden');
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
	/*todoItemForm.style.height = 0;
	todoItemForm.style.padding = "0";*/
	for(let btn of AddTodoItemBtn){
		btn.value="Add Item";
	}
	
	
	/*var form = document.getElementById("viewing-modal");*/
	const modalContainer = document.getElementById("viewing-modal-container");
	modalContainer.style.opacity = "0";
	modalContainer.style.visibility = "hidden";
	const modalContents = [document.getElementById("todo-list-modal-content"), document.getElementById("project-list-modal-content")];

	for (let c of modalContents) {
		c.style.display = "none";
	}
	document.body.classList.remove('overflow-hidden');
}

//close modal with esc key
document.onkeydown = function(evt) {
    evt = evt || window.event;
    var isEscape = false;
    if ("key" in evt) {
        isEscape = (evt.key === "Escape" || evt.key === "Esc");
    } else {
        isEscape = (evt.keyCode === 27);
    }
    if (isEscape) {
        closeViewingModal();
    }
};




var isCollapsed = true;
function sideMenuToggle() {

	const sideMenu = document.querySelector("#side-menu");
	const toggle = document.querySelector("#side-menu-toggle");
	const sideNav = document.querySelector(".side-nav");
	const sideNavText = document.getElementsByClassName("side-nav-text");
	const toggleArrow = document.getElementById("side-nav-arrow");
	const toggleArrowClass = ["fa-solid fa-caret-left", "fa-solid fa-caret-right"];


	if (isCollapsed) {
		sideNav.style = "width: 200px;";
		toggle.style.letterSpacing = "0em";



		for (let t of sideNavText) {
			t.style.display = "inline";
		}
		toggleArrow.setAttribute("class", toggleArrowClass[0]);
		isCollapsed = false;
	}
	else {
		sideNav.style = "width: 45px;";
		for (let t of sideNavText) {
			t.style.display = "none";
		}
		toggle.style.letterSpacing = "-0.1em";
		toggleArrow.setAttribute("class", toggleArrowClass[1]);
		isCollapsed = true;
	}

}



//display message when there is nothing( no project, project task, & todo list) on card & modal 
function contentEmptyMessage(){
	
	const itemCount = document.getElementsByClassName("item-count-check");
	const msg = ["project", "todo list item", "project task"];
for (let count of itemCount) {
	//alert("hello");
	if (count.textContent == "0") {
		count.classList.add("mt-2");
		count.classList.add("d-block");
		count.innerHTML = "No content to display at the moment.";
	}
	else {
		count.textContent = "";
	}
}
}


/*//todo list item count checker
let itemCount = document.getElementsByClassName("todo-card-item-count");
for (let ic of itemCount) {
	if (ic.textContent == 0) {
		ic.innerHTML = "<br/><br/>Todo list is empty at the moment."
	}
	else {
		ic.textContent = " ";
	}
}*/





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




//display-item-count-script
function displayItemCount() {
	/*alert("oooollll");*/
	//let items = document.getElementsByClassName("itemNumber");
	const element1 = document.querySelectorAll(".card-body #itemNumber");
	const element2 = document.querySelectorAll(".item-display-table #itemNumber");
	const element3 = document.querySelectorAll(".card-body #projectNumber");
	const element4 = document.querySelectorAll(".project-display-table #itemNumber");
	let itemElement = [element1, element2, element3, element4];
	for (let items of itemElement) {
		var counter = 0;
		for (let item of items) {
			item.innerHTML = ++counter;
		}
	}
}
	

/*//Notication bar popup(NEW) script
const notification = document.querySelector('.notification');
const button = document.querySelector('.trigger-button');

const toggleNotification = () => {
    notification.classList.remove('none');
    notification.classList.toggle('hide');
}

document.addEventListener("click", (event) => {
    const isClickInsideNotification = notification.contains(event.target);
    const isButtonClicked = button.contains(event.target);

    if (!isClickInsideNotification && !isButtonClicked) {
        notification.classList.add('hide');
    }
});*/








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

