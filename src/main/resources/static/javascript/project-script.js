//show "add project" form
function showAddProjectTaskForm(id) {
	//add-project-task-button
	const addTaskForm = document.getElementById("add-project-task-form" + id);
	const temp = document.getElementsByClassName("add-project-task-form");

	if (addTaskForm.style.height == "0px") {
		for (let t of temp) {
			t.style.height = "0";
		}
		addTaskForm.style.height = "120px";
	}
	else {
		addTaskForm.style.height = "0";
	}

}
function addProjectTask() {
	const title = document.getElementById("title-input").value;
	const detail = document.getElementById("detail-input").value;
	fetch('/project/add-project-task-js?title="' + title + '"&detail="' + detail + '"')
		.then(response => response.text())
		.then(data => {
			alert(data);
		})
}

//show "add project" form
function showAddProjectForm(source) {
	const projectForm = document.getElementById("add-project-form");
	const createProjectBtn = document.getElementsByClassName("create-project-btn");
	if (source == "card") {//if the click comes from dashboard card instead of modal
		//createItemButtonToDefault();		
		//change the state of "Create Project" button to default state
		projectForm.style.height = 0;
		projectForm.style.padding = "0";
	}

	if (projectForm.style.height == "0px") {
		projectForm.style.height = "270px";
		projectForm.style.padding = "1.5rem";
		for(let btns of createProjectBtn){
			btns.value = "Exit Form";
		}
	}
	else {
		projectForm.style.height = 0;
		projectForm.style.padding = "0";
		for(let btns of createProjectBtn){
			btns.value = "Create Project";
		}
	}
}
//add project form submit
function addProject() {
	const title = document.getElementById("project-title-input").value;
	const description = document.getElementById("project-description-input").value;
	const status = document.getElementById("project-status-input").value;
	fetch('/user/add-project-js?title=' + title + '&description=' + description + '&status=' + status)
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



