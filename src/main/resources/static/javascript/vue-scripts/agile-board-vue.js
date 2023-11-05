const fetchListUrl = '/user/api/todo-list/1';
const fetchListUrlREST = '/api/todo-list/1';
const fetchListUrlREST2 = '/api/todo-list/1';
const getProjectsUrl = '/project/getProjects';
const getProjectTasksUrl = '/project/getProjectTasks';
const getProjectCountUrl = '/project/getProjectCount';
const getPublicConnectsUrl = '/user/getUserConnectionList';
const updateProjectTaskStatus = '/project/task/update/status'
const getProjectMemberListUrl = '/project/members';
const assignTaskUrl = '/task/update/assignee';


//agile-board
Vue.createApp({
	data() {
		return {
			projectList: [],
			showLoader: false,
			projectOnBoard: null, //for checking if project is selected
			projectTasksOnBoard: null, //tasks of selected project(to be on board)
			projectMembers: [],
		}
	},
	created() {
		this.getProjectData()
		var localStoredBoardProject = window.localStorage.getItem("agile-board-selected-project");
		//alert(localStoredBoardProject)
		if (localStoredBoardProject !== null) {
			const proj = parseInt(window.localStorage.getItem("agile-board-selected-project"));
			window.setTimeout(() => {
				this.selectProject(proj);
			}, 1000)
		}


		/*var localStoredBoardProject = window.localStorage.getItem("agile-board-selected-project");		
		if (localStoredBoardProject !== null){
			//alert("what?")
			//this.getProjectData();
			//alert(this.projectList[0].title)
			
			this.getProjectData();
			//alert(this.projectList[0].title)
				window.setTimeout(()=>{
					this.selectProject(localStoredBoardProject);
					//alert(localStoredBoardProject)
				}, 2000)
	}*/
	},
	methods: {
		//get list of projects for selecting projects to put on the board
		getProjectData() {
			this.showLoader = true;
			fetch("/project/getProjects")
				.then(response => response.json())
				.then(data => {
					this.projectList = data
					this.showLoader = false;
					return 1;
				})
				.catch(e => { return 0 })



		},
		selectProject(projectId) {
			var p;
			this.getProjectData();

			for (p of this.projectList) {
				//alert("gggeeee")
				if (p.projectId === projectId) {
					this.projectOnBoard = p;

					break;
				}
			}
			//fetch(getProjectTasksUrl + "?project=" + this.projectOnBoard.projectId)
			fetch(getProjectTasksUrl + "?project=" + projectId)
				.then(response => response.json())
				.then(data => {
					this.projectTasksOnBoard = data;
					window.localStorage.setItem("agile-board-selected-project", projectId);
				})


		},
		getProjectMembers(){
			//alert("lebek")
			fetch(getProjectMemberListUrl+"?project="+this.projectOnBoard.projectId)
				.then(response => response.json())
				.then(data => {
					this.projectMembers = data;
				})
			
		},
		assignTask(userId,taskId) {
			fetch(assignTaskUrl + "?user=" + userId + "?task=" + taskId)
				.then(response => response.json())
				.then(data => {
					if(data == 0){
						alert("error");
					}
					else{
						alert("success");
					}
					this.projectTasksOnBoard = this.projectTasksOnBoard;
				})
		},
		allowDrop(ev) {
			ev.preventDefault();
		},
		drag(taskID, ev) {
			ev.dataTransfer.setData("taskCard", ev.target.id);
			this.currentTask = taskID;
		},
		drop(ev) {
			ev.preventDefault();
			var data = ev.dataTransfer.getData("taskCard");
			const task = this.currentTask;
			const dropedCard = document.getElementById(data);
			var newStatus = "not started";
			//ev.target.appendChild(document.getElementById(data));
			if (ev.currentTarget.classList.contains("in-progress-section")) {
				newStatus = "in progress"
			}
			else if (ev.currentTarget.classList.contains("completed-section")) {
				newStatus = "done"
			}
			fetch(updateProjectTaskStatus + "?status=" + newStatus + "&task=" + task)
				.then(response => response.json())
				.then(data => {
					if (data == 1) {
						alert("success");
					}
					else
						alert("couldn't update task status")

				});
			ev.currentTarget.appendChild(dropedCard);
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










