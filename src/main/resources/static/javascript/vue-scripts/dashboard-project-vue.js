const fetchListUrl = '/user/api/todo-list/1';
const fetchListUrlREST = '/api/todo-list/1';
const fetchListUrlREST2 = '/api/todo-list/1';
const getProjectsUrl = '/project/getProjects';
const getProjectCountUrl = '/project/getProjectCount';
const getPublicConnectsUrl = '/user/getUserConnectionList';
const userDetailUrl = "/user/detail"



//project - display -modal
Vue.createApp({

	data() {
		return {
			projects: [],
			projectCount: null,
			connectsList: [],
			currentUserDetail: null
		}
	},
	created() {
		this.getProjects(),
			this.getProjectCount(),
			this.getCurrentUserDetail()
	},
	methods: {
		getCurrentUserDetail(){
			fetch(userDetailUrl)
				.then(response => response.json())
				.then(data => this.currentUserDetail = data)
		},
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
		showAddProjectMembersList(id, ev, on) {
			const container = document.getElementById("add-member-list-container-" + id);

			const inputArea = document.getElementById("add-project-members-btn-text");
			if (!container.classList.contains("show") && on != "blr") {
				this.collapseAllAddProjectMembersList();
				this.collapseAllAddProjectTaskForm();
				container.classList.add("show");
				fetch(getPublicConnectsUrl)
					.then(response => response.json())
					.then(data => this.connectsList = data)
				
			}
			else {
				if ((ev.currentTarget.value == "" && on == "blr") || ev.currentTarget.id != container.id) {
					//alert("here...")
					//alert("Container: "+container.id +" target: "+this.id)
					container.classList.remove("show");
				}
			}

		},
		collapseAllAddProjectMembersList(){
			const memberForm = document.getElementsByClassName("add-project-member-list-container");
			for(let formContainer of memberForm){
				formContainer.classList.remove("show");
			}
			
		}
		,
		searchThroughProjectMembersList() {
			fetch(getProjectsUrl)
				.then(response => response.json())
				.then(data => this.projects = data)
		},
		addProjectMember(projectId, username) {
			fetch(getProjectsUrl)
				.then(response => response.json())
				.then(data => this.projects = data)
		},
		//show "add project task" form
		showAddProjectTaskForm(id, trigger) {
			//add-project-task-button
			const addTaskForm = document.getElementById("add-project-task-form" + id);

			if (addTaskForm.style.height == "0px") {
				this.collapseAllAddProjectTaskForm();
				this.collapseAllAddProjectMembersList();
				addTaskForm.style.height = "150px";
				trigger.currentTarget.classList.add("toggled");
			}
			else {
				addTaskForm.style.height = "0";
				trigger.currentTarget.classList.remove("toggled");
			}
		},
		collapseAllAddProjectTaskForm() {
			const AllAddTaskForms = document.getElementsByClassName("add-project-task-form");
			for (let f of AllAddTaskForms) {
				f.style.height = "0";
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
