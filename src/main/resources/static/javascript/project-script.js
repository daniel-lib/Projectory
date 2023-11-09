



//submit project task to server
function addTaskToProject(projId) {
	//alert("here");
	/*const title = document.getElementById("task-title"+id).value;
	const description = document.getElementById("task-description"+id).value;*/

	const addTaskForm = document.getElementById("addTaskForm" + projId);
	const title = addTaskForm.getElementsByClassName("task-title");
	const description = addTaskForm.getElementsByClassName("task-description");
	const status = addTaskForm.getElementsByClassName("task-status");

	//alert(projId);
	fetch('/project/add-project-task?title=' + title[0].value + '&description=' + description[0].value + '&projId=' + projId + '&status=' + status[0].value)
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

//show "add project" formz
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
}


function createItemButtonToDefault() {
	//change the state of "Create Project" button to default state
	projectForm.style.height = 0;
	projectForm.style.padding = "0";
}



