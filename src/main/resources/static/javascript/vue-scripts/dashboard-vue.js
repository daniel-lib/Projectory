//const getProjectTasksUrl = '/project/getProjectTasks'
const dashboardVue = Vue.createApp({
	data(){
		return{
			projectTasksOnBoardPeekView:[],
			projectOnBoardCount:0
		}
	},
	created(){
		this.getTasksListOnAgileBoard()
	},
	methods:{
		getTasksListOnAgileBoard(){
			const projectId = window.localStorage.getItem("agile-board-selected-project")
			fetch(getProjectTasksUrl + "?project=" + projectId)
				.then(response => response.json())
				.then(data => {
					//alert(data[0].taskName)
					this.projectTasksOnBoardPeekView = data;
					this.projectOnBoardCount = data.length;
					//window.localStorage.setItem("agile-board-selected-project", projectId);
				})
		}
	}
});
dashboardVue.mount("#projectTaskOnBoardPeekView")