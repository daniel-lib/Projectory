const fetchListUrl = '/user/api/todo-list/1';
const fetchListUrlREST = '/api/todo-list/1';
const fetchListUrlREST2 = '/api/todo-list/1';
const getProjectsUrl = '/project/getProjects';
const getProjectCountUrl = '/project/getProjectCount';
const getPublicConnectsUrl = '/user/getUserConnectionList';







//project - display -modal
Vue.createApp({

	data() {
		return {
			projects: [],
			projectCount: null,
			connectsList: []
		}
	},
	created() {
		this.getProjects(),
			this.getProjectCount()
	},
	methods: {
		getProjects() {
			fetch(getProjectsUrl)
				.then(response => response.json())
				.then(data => this.projects = data)
		},
		getProjectCount() {
			fetch(getProjectCountUrl)
				.then(response => response.json())
				.then(data => this.projectCount = data)
		},
		showAddProjectMembersList(id) {
			/*const container=document.getElementById("add-member-list-container-+"+id);
			const memberList = document.createElement("div");
			memberList.setAttribute("id","member-list-container");
			memberList.classList.add("add-project-member-list-container")*/
			//alert("halo");
			//event.target.parentNode.append(memberList);
			//document.getElementById("boob").append(memberList);
			const container = document.getElementById("add-member-list-container-" + id);
			if (container.classList.contains("show")) {
				container.classList.remove("show");
			}
			else {
				container.classList.add("show");
				fetch(getPublicConnectsUrl)
					.then(response => response.json())
					.then(data => this.connectsList = data)
			}

		},
		searchThroughProjectMembersList() {
			fetch(getProjectsUrl)
				.then(response => response.json())
				.then(data => this.projects = data)
		},
		addProjectMember(projectId, username, event) {
			fetch(getProjectsUrl)
				.then(response => response.json())
				.then(data => this.projects = data)
		},
		//show "add project task" form
		showAddProjectTaskForm(id, trigger) {
			//add-project-task-button
			const addTaskForm = document.getElementById("add-project-task-form" + id);
			const AllAddTaskForms = document.getElementsByClassName("add-project-task-form");

			if (addTaskForm.style.height == "0px") {
				for (let f of AllAddTaskForms) {
					f.style.height = "0";
				}
				/*document.getElementsByClassName("add-project-task-button").forEach((btn)=>{
						btn.classList.remove("toggled")
					});*/
				for (let btn of document.getElementsByClassName("add-project-task-button")) {
					btn.classList.remove("toggled")
				}


				addTaskForm.style.height = "150px";
				trigger.currentTarget.classList.add("toggled");
			}
			else {
				addTaskForm.style.height = "0";
				trigger.currentTarget.classList.remove("toggled");
			}
		},
		//expand/maximize container
		expandContainer(containerId, btnId) {
			const div = document.getElementById(containerId);
			const trigger = btnId.target;
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

	}

}).mount("#projects-holder-container")


//agile-board
Vue.createApp({
	data() {
		return {
			projectList: [],
			showLoader: false,
			projectOnBoard: null,
		}
	},
	crated() {

	},
	methods: {
		selectProject() {

		},
		getProjectData() {
			this.showLoader = true;			
			fetch("/project/getProjects")
				.then(response => response.json())
				.then(data => {this.projectList = data
				this.showLoader = false;
				})
				
				
			/*	window.setTimeout(()=>{	
				}, 1000);*/			
		},
		selectProject(projectId) {
			//this.projectOnBoard="dd"
			for(let p of this.projectList){
				if(p.projectId === projectId)
				this.projectOnBoard = p;
				break;
			}
		},
		allowDrop(ev) {
			ev.preventDefault();
		},
		drag(ev) {
			ev.dataTransfer.setData("text", ev.target.id);
		},
		drop(ev) {
			ev.preventDefault();
			var data = ev.dataTransfer.getData("text");
			ev.target.appendChild(document.getElementById(data));
			ev.currentTarget.appendChild(document.getElementById(data));
			ev.currentTarget.classList.remove("highlighted");
		},
		cancelDropAreaHighlight(ev) {
			ev.currentTarget.classList.remove("highlighted");
		},
		dropAreaHighlight(ev) {
			ev.currentTarget.classList.add("highlighted");
		},
		boardOnFullScreen() {
			const boardContainer = document.getElementById("board-container");
			const fullScreenBtn = document.getElementById("board-full-screen-btn");
			const boardOuterContainer = document.getElementById("board-container-container");
			const boardInnerContainer = document.getElementById("board-inside-container");
			if (boardContainer.classList.contains("full-screen")) {
				boardContainer.classList.remove("full-screen");
				boardInnerContainer.classList.remove("full-screen");
				boardOuterContainer.classList.add("ms-5");
				fullScreenBtn.textContent = "Full Screen";
				document.body.style.overflow = "visible";
			}
			else {
				boardContainer.classList.add("full-screen");
				boardInnerContainer.classList.add("full-screen");
				boardOuterContainer.classList.remove("ms-5");
				fullScreenBtn.textContent = "Back";
				document.body.style.overflow = "hidden";
			}
		}
	}
}).mount("#agile-board")










