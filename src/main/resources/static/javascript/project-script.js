

//show "add project task" form
function showAddProjectTaskForm(id) {
	//add-project-task-button
	const addTaskForm = document.getElementById("add-project-task-form" + id);
	const AllAddTaskForms = document.getElementsByClassName("add-project-task-form");
	/*const a = document.getElementById(id);
	const b = a.getElementById(id);*/
	//alert("#"+id +" > .add-project-task-button");

	/*const q = document.querySelector("#1 > .add-project-task-button");*/

	//const q = document.querySelector("#"+id +" > .add-project-task-button");
	//alert("999");
	//const TriggerBtn = document.querySelectorAll("#"+id+" > #"+id);
	/*alert(q.textContent);
	const TriggerBtn = document.querySelector('#'+id+' > #'+id);*/

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
function addTaskToProject(projId) {
	//alert("here");
	/*const title = document.getElementById("task-title"+id).value;
	const description = document.getElementById("task-description"+id).value;*/

	const addTaskForm = document.getElementById("addTaskForm" + projId);
	const title = addTaskForm.getElementsByClassName("task-title");
	const description = addTaskForm.getElementsByClassName("task-description");

	//alert(projId);
	fetch('/user/add-project-task?title=' + title[0].value + '&description=' + description[0].value + '&projId=' + projId)
		.then(response => response.text())
		.then(data => {
			let msg;
			if(data == "1"){
				msg = "Project Task Added!";
			}
			else{
				msg = "adding project task was unsuccessful.";
			}
			alert(msg);
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
		projectForm.style.border = "none";
	}

	if (projectForm.style.height == "0px") {
		projectForm.style.height = "270px";
		projectForm.style.padding = "1.5rem";
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


//expand/maximize container
function expandContainer(containerId){
	const div = document.getElementById(containerId);
	div.style.width = "900px";
	div.style.height = "500px";
}

/*try{
	const modalBoxes= document.getElementsByClassName("project-modal-boxes");
for(let b of modalBoxes){
	b.addEventListener("mouseover", modalBoxesHoverEffect);
}

function modalBoxesHoverEffect(){
	alert("boooo");
	
}
}
catch(error){
	alert(error);
}
*/

