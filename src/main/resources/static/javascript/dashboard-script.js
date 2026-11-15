(function displayProfilePicBtn() {
	const profilePicBtn = document.querySelectorAll('.profile-pic-btn.own-profile-pic');
	if (profilePicBtn ) {
		Array.from(profilePicBtn).forEach((element) => element.textContent = username[0].toUpperCase());
	}
	else {
		//get pic link from db or remote storage
	}
})();





/*(function displayProfilePicBtn() {
	const profilePicBtn = document.getElementsByClassName('profile-pic-btn');
	if (profilePicBtn[0].textContent == "") {
		Array.from(profilePicBtn).forEach((element) => element.textContent = username[0].toUpperCase());
	}
	for(let prof of profilePicBtn){
		if (prof.textContent.length == 1) {
		//Array.from(profilePicBtn).forEach((element) => element.textContent = username[0].toUpperCase());
		//prof.textContent=prof.textContent[1].toUpperCase();
		//prof.textContent="+";
			let fn = prof.textContent;
			//alert(fn[0])
			//prof.textContent=fn.toUpperCase();
	}
	else{
		//prof.textContent="-";
		//get pic link from db or remote storage
		//alert(prof.textContent+": "+prof.textContent.length)
		prof.style.backgroundImage = "URL(prof.textContent)";
		//prof.textContent = "";
	}
	}
	
})();

*/


function displayViewingModal(contentType) {
	/*const modalContainer = document.getElementById("viewing-modal");*/
	const modalContainer = document.getElementById("viewing-modal-container");
	const contents = [document.getElementById("todo-list-modal-content"), document.getElementById("project-list-modal-content")];

	collapseExpandedProjectCard();
	modalContainer.style.visibility = "visible";
	modalContainer.style.opacity = "1";

	if (contentType >= 0 && contentType < contents.length) {
		contents[contentType].style.display = "block";
		if (contentType === 1 && window.htmx) {
			htmx.ajax('GET', '/project/fragment/list', {
				target: '#projects-holder-container',
				swap: 'innerHTML'
			});
			htmx.ajax('GET', '/project/fragment/count', {
				target: '#project-count-info',
				swap: 'outerHTML'
			});
		}
		//document.body.classList.add('overflow-hidden')

		/*document.body.style.position = "fixed";
		document.body.style.overflowY = "scroll";*/
		document.body.classList.add('modal-open-disable-body-scrolling');

		const nav = document.getElementsByTagName("nav");


		const mediaQuery = window.matchMedia("(max-width: 790px)");
		const isMobile = mediaQuery.matches;
		/*if (!isMobile)
			nav[0].classList.add("modal-open");*/

		nav[0].classList.add("modal-open");
		document.body.classList.add('modal-open-body');

		modalContainer.classList.add('overflow-ver-scroll');
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

}

const expandedProjectCardOrigins = new WeakMap();

function toggleProjectCardExpanded(projectCard) {
	const isExpanded = expandedProjectCardOrigins.has(projectCard);
	document.dispatchEvent(new CustomEvent("project-list-expand", {
		detail: isExpanded ? null : projectCard,
		bubbles: true
	}));
}

function setProjectCardExpanded(projectCard, expanded) {
	const overlay = document.getElementById("project-expanded-overlay");
	if (!overlay) return;

	if (expanded && !expandedProjectCardOrigins.has(projectCard)) {
		const placeholder = document.createComment("project card position");
		projectCard.parentNode.insertBefore(placeholder, projectCard);
		expandedProjectCardOrigins.set(projectCard, { placeholder });
		overlay.appendChild(projectCard);
		overlay.classList.add("is-open");
		overlay.setAttribute("aria-hidden", "false");
	} else if (!expanded && expandedProjectCardOrigins.has(projectCard)) {
		const { placeholder } = expandedProjectCardOrigins.get(projectCard);
		if (placeholder.isConnected) {
			placeholder.parentNode.insertBefore(projectCard, placeholder);
			placeholder.remove();
		} else {
			const projectList = document.querySelector(
				"#projects-holder-container #project-list-table-content .d-flex.justify-content-around.flex-wrap");
			if (projectList) projectList.appendChild(projectCard);
		}
		projectCard.classList.remove("project-modal-boxes-container-expanded");
		expandedProjectCardOrigins.delete(projectCard);
		overlay.classList.remove("is-open");
		overlay.setAttribute("aria-hidden", "true");
	}

	if (expanded) projectCard.classList.add("project-modal-boxes-container-expanded");
}

document.addEventListener("project-list-expand", function(event) {
	const projectCards = Array.from(document.querySelectorAll(".project-modal-boxes-container"));
	projectCards.forEach(function(projectCard) {
		if (projectCard !== event.detail) setProjectCardExpanded(projectCard, false);
	});
	if (event.detail && projectCards.includes(event.detail)) {
		setProjectCardExpanded(event.detail, true);
	}
});

function collapseExpandedProjectCard() {
	document.dispatchEvent(new CustomEvent("project-list-expand", { detail: null, bubbles: true }));
}

function openProjectMemberPicker(projectId) {
	const picker = document.getElementById("project-members-picker");
	picker.dataset.projectId = projectId;
	picker.classList.add("is-open");
	picker.setAttribute("aria-hidden", "false");
	loadProjectMemberPicker(projectId);
}

function loadProjectMemberPicker(projectId) {
	const list = document.getElementById("project-members-picker-list");
	list.innerHTML = '<span class="text-muted">Loading connections...</span>';
	htmx.ajax("GET", "/user/fragment/connection-list?project=" + encodeURIComponent(projectId), {
		target: "#project-members-picker-list",
		swap: "innerHTML"
	});
}

function closeProjectMemberPicker() {
	const picker = document.getElementById("project-members-picker");
	picker.classList.remove("is-open");
	picker.setAttribute("aria-hidden", "true");
}

document.body.addEventListener("projectChanged", collapseExpandedProjectCard);
document.body.addEventListener("projectMembersChanged", function() {
	const picker = document.getElementById("project-members-picker");
	if (picker.classList.contains("is-open")) {
		loadProjectMemberPicker(picker.dataset.projectId);
	}
});

document.body.addEventListener("htmx:afterSwap", function(event) {
	if (!event.detail.target || event.detail.target.id !== "project-members-picker-list") return;
	const picker = document.getElementById("project-members-picker");
	const count = event.detail.target.querySelectorAll(".project-current-member").length;
	const badgeCount = document.getElementById("project-member-count-" + picker.dataset.projectId);
	if (badgeCount) badgeCount.textContent = String(count);
});

function closeViewingModal() {

	//change the state of "Create Project" button to default state
	//createItemButtonToDefault();
	collapseExpandedProjectCard();
	closeProjectMemberPicker();
	const projectForm = document.getElementById("add-project-form");
	const createProjectBtn = document.getElementsByClassName("create-project-btn");
	projectForm.style.height = 0;
	projectForm.style.padding = "0";
	for (let btn of createProjectBtn) {
		btn.value = "Create Project";
	}

	//change the state of "Add Item" button to default state
	const todoItemForm = document.getElementById("add-todo-item-form");
	const AddTodoItemBtn = document.getElementsByClassName("add-todo-item-btn");
	/*todoItemForm.style.height = 0;
	todoItemForm.style.padding = "0";*/
	for (let btn of AddTodoItemBtn) {
		btn.value = "Add Item";
	}


	/*var form = document.getElementById("viewing-modal");*/
	const modalContainer = document.getElementById("viewing-modal-container");
	modalContainer.style.opacity = "0";
	modalContainer.style.visibility = "hidden";
	const modalContents = [document.getElementById("todo-list-modal-content"), document.getElementById("project-list-modal-content")];

	for (let c of modalContents) {
		c.style.display = "none";
	}
	//document.body.classList.remove('overflow-hidden');
	document.body.classList.remove('modal-open-disable-body-scrolling');
	const nav = document.getElementsByTagName("nav");
	nav[0].classList.remove("modal-open");
	document.body.classList.remove('modal-open-body');
}

function sideMenuToggle() {

	const toggle = document.querySelector("#side-menu-toggle");
	const body = document.body;
	const isCollapsed = !body.classList.contains("side-menu-expanded");
	setSideMenuExpanded(isCollapsed, true);
}

function openUnfriendConfirmation(button) {
	const url = button.dataset.unfriendUrl;
	if (!url) {
		return;
	}

	openAppConfirmation({
		title: "Remove this connection?",
		message: "They will be removed from your connections. You can send a new request later.",
		confirmLabel: "Yes, unfriend",
		onConfirm: function() {
			htmx.ajax("POST", url, { target: "#connections-panel", swap: "none" });
		}
	});
}

function setSideMenuExpanded(expanded, persist) {
	const toggle = document.querySelector("#side-menu-toggle");
	const toggleArrow = document.getElementById("side-nav-arrow");
	const body = document.body;
	if (!toggle || !toggleArrow) {
		return;
	}

	body.classList.toggle("side-menu-expanded", expanded);
	toggleArrow.classList.toggle("fa-caret-left", expanded);
	toggleArrow.classList.toggle("fa-caret-right", !expanded);
	toggle.setAttribute("aria-expanded", String(expanded));
	toggle.setAttribute("aria-label", expanded ? "Collapse navigation" : "Expand navigation");

	if (persist) {
		try {
			window.localStorage.setItem("projectory-side-menu-state", expanded ? "expanded" : "collapsed");
		} catch (error) {
			console.warn("Could not save the sidebar state to local storage.", error);
		}
	}
}

setSideMenuExpanded(document.body.classList.contains("side-menu-expanded"), false);

/*	selectedSideMenuToggle();
function selectedSideMenuToggle(selectedSideMenu) {
	if(selectedSideMenu == undefined){
		//alert("nothing to see here");
		const selectedNav = window.localStorage.getItem("selected-side-menu");
		document.getElementById("side-menu-dashboard-link").classList.remove("side-nav-link-selected");
		document.getElementById(selectedNav).classList.add("side-nav-link-selected");
	}
	else{
		//alert("heyaaall");
		const menuLinks = ["side-menu-dashboard-link", "side-menu-connection-link", "side-menu-board-link", "side-menu-setting-link"];
	
	for (let links of menuLinks) {
		document.getElementById(links).classList.remove("side-nav-link-selected");
	}
	document.getElementById(menuLinks[selectedSideMenu]).classList.add("side-nav-link-selected");
	window.localStorage.setItem("selected-side-menu", menuLinks[selectedSideMenu]);
	}	
}*/

//auth user view active menu indicator
let paths = {
	"/project/board" : "side-menu-board-link",
	"/user/dashboard" : "side-menu-dashboard-link",
	"/user/connections" : "side-menu-connection-link",
	"/user/settings" : "side-menu-setting-link"	
}

selectedNavIndicator();
function selectedNavIndicator(){
		
	let currentPath = window.location.pathname;
	
	if (currentPath in paths){	
		//alert("hellloooo")
		document.getElementById(paths[currentPath]).classList.add("side-nav-link-selected");
		
	}
	/*if(currentPath in paths){
		document.getElementById(paths[currentPath]).classList.add("side-nav-link-selected");
	}*/
	//const menuLinks = ["side-menu-dashboard-link", "side-menu-connection-link", "side-menu-board-link", "side-menu-setting-link"];
	//if(window.location.pathname == )
};






/*(function displayProfilePicBtn() {
	const profilePicBtn = document.querySelectorAll('.profile-pic-btn.own-profile-pic');
	if (profilePicBtn ) {
		Array.from(profilePicBtn).forEach((element) => element.textContent = username[0].toUpperCase());
	}
	else {
		//get pic link from db or remote storage
	}
})();
*/



//display message when there is nothing( no project, project task, & todo list) on card & modal 
function contentEmptyMessage() {

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
function showTopCollapsableMenu(index, clickOrigin) {

	const selectedContent = document.getElementById(specificContentIds[index]);

	for (let specCont = 0; specCont < specificContentIds.length; specCont++) {
		document.getElementById(specificContentIds[specCont]).style.display = "none";
	}
	selectedContent.style.display = "block";


	const clickedIcon = document.getElementById(clickOrigin);
	const wasPanelOpen = topMenuContainer.classList.contains("top-collapsable-menu-container-expanded")
		&& clickedIcon.ariaLabel === "selected";

	const topNavIcons = document.getElementsByClassName("top-nav-links");

	if (clickedIcon.classList.contains("selected"))
		clickedIcon.classList.remove("selected");
	else {
		for (let icon of topNavIcons) {
			icon.classList.remove("selected");
		}
		clickedIcon.classList.add("selected");

		//clickedIcon.classList.add("selected");
	}

	if (topMenuContainer.classList.contains("top-collapsable-menu-container-expanded") && clickedIcon.ariaLabel == "selected") {
		collapseTopMenu();
	} else {
		//set clicked top menu icon to default		
		for (let icon of topNavIcons) {
			icon.ariaLabel = "not-selected";
		}
		topMenuContainer.classList.add("top-collapsable-menu-container-expanded");
	}



	clickedIcon.ariaLabel = "selected";
	if (index === 0 && !wasPanelOpen && window.htmx) {
		htmx.trigger(document.body, "notificationPanelOpened");
	}

	// Get arbitrary element with id "my-element"
	const menuIcons = document.getElementsByClassName('top-menu-icons');

	// Listen for click events on body
	document.body.addEventListener('click', function(event) {
		if (topMenuContainer.contains(event.target) || menuIcons[0].contains(event.target)) {
			//clicked inside of top menu
		} else {
			//clicked outside of top menu
			collapseTopMenu();
		}

	});
}

function collapseTopMenu() {
	topMenuContainer.classList.remove("top-collapsable-menu-container-expanded");
	//document.getElementById(specificContentIds[specCont]).style.display = "none";

	//set clicked top menu icon to default
	const topNavIcons = document.getElementsByClassName("top-nav-links");
	for (let icon of topNavIcons) {
		icon.ariaLabel = "not-selected";
		icon.classList.remove("selected");
	}
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
