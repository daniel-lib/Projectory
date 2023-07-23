

//show "add project task" form
function showAddProjectTaskForm(id) {
	//add-project-task-button
	const addTaskForm = document.getElementById("add-project-task-form" + id);
	const AllAddTaskForms = document.getElementsByClassName("add-project-task-form");

	if (addTaskForm.style.height == "0px") {
		for (let f of AllAddTaskForms) {
			f.style.height = "0";
		}
		addTaskForm.style.height = "120px";
		TriggerBtn.style.backgroundColor = "rgba(119, 118, 123, 0.6)";
		alert("h");

	}
	else {
		addTaskForm.style.height = "0";
	}

}

//submit project task to server
function addTaskToProject(projId) {
	//alert("here");
	/*const title = document.getElementById("task-title"+id).value;
	const description = document.getElementById("task-description"+id).value;*/

	const addTaskForm = document.getElementById("addTaskForm" + projId);
	const title = addTaskForm.getElementsByClassName("task-title");
	const description = addTaskForm.getElementsByClassName("task-description");

	//alert(projId);
	fetch('/project/add-project-task?title=' + title[0].value + '&description=' + description[0].value + '&projId=' + projId)
		.then(response => response.text())
		.then(data => {
			let msg, notificationType;
			if (data == "1") {
				msg = "Project Task Added!";
				notificationType = "success";
			}
			else {
				msg = "Couldn't add project task. It's probably your fault.";
				notificationType = "error";
			}
			/*alert(msg);*/
			toggleNotification(notificationType, msg);
		})
}

//show "add project" form
function showAddProjectForm(source) {

	const projectForm = document.getElementById("add-project-form");
	const createProjectBtn = document.getElementsByClassName("create-project-btn");
	if (source == "card") {//if the click comes from dashboard card instead of modal
		//createItemButtonToDefault();		
		//change the state of "Create Project" button to default state
		/*projectForm.style.height = 0;
		projectForm.style.padding = "0";
		projectForm.style.border = "none";*/
		projectForm.classList.remove('add-project-form-expanded');
	}
	projectForm.classList.toggle('add-project-form-expanded');

	if (projectForm.classList.contains('add-project-form-expanded')) {
		for (let btns of createProjectBtn) {
			btns.value = "Exit Form";
		}
	} else {
		for (let btns of createProjectBtn) {
			btns.value = "Create Project";
		}	
	}
	/*if (projectForm.style.height == "0px") {
		projectForm.style.height = "270px";
		projectForm.style.padding = "0rem 1.5rem";
		projectForm.style.border = "1px solid rgb(192, 191, 188)";
		for (let btns of createProjectBtn) {
			btns.value = "Exit Form";
		}
	}
	else {
		projectForm.style.height = 0;
		projectForm.style.padding = "0";
		projectForm.style.border = "none";
		for (let btns of createProjectBtn) {
			btns.value = "Create Project";
		}
	}*/
}

//submit new project to server
function addProject() {
	const title = document.getElementById("project-title-input").value;
	const description = document.getElementById("project-description-input").value;
	const status = document.getElementById("project-status-input").value;
	fetch('/project/add-project-js?title=' + title + '&description=' + description + '&status=' + status)
		.then(response => response.json())
		.then(data => {
			alert(data);
		})
}
function createItemButtonToDefault() {
	//change the state of "Create Project" button to default state
	projectForm.style.height = 0;
	projectForm.style.padding = "0";
}


//expand/maximize container
function expandContainer(containerId, btnId) {
	const div = document.getElementById(containerId);
	const trigger = document.getElementById(btnId);
	const allBoxes = document.getElementsByClassName("project-modal-boxes");
	const allTriggerBtns = document.getElementsByClassName("top-dangling-x-btn");
	//const d = document.get

	if (trigger.title == "Expand") {
		//minimize all project boxes, change bg color to default
		for (let box of allBoxes) {
			box.classList.replace('project-modal-boxes-expanded', 'project-modal-boxes-default');
		}
		for (let btns of allTriggerBtns) {
			btns.classList.remove('fa-compress');
			btns.classList.add('fa-expand');
			btns.setAttribute("title", "Expand");
		}

		trigger.setAttribute("title", "Minimize");
		div.classList.remove("project-modal-boxes-default");
		div.classList.add("project-modal-boxes-expanded");
		div.scrollIntoView();
		trigger.classList.remove('fa-expand');
		trigger.classList.add('fa-compress');
	}
	else {
		trigger.setAttribute("title", "Expand");
		div.classList.add("project-modal-boxes-default");
		div.classList.remove("project-modal-boxes-expanded");
		trigger.classList.remove('fa-compress');
		trigger.classList.add('fa-expand');
	}
}
