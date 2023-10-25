const fetchListUrl = '/user/api/todo-list/1';
const fetchListUrlREST = '/api/todo-list/1';
const fetchListUrlREST2 = '/api/todo-list/1';







Vue.createApp({
	
	data(){
		projects:[];	
	},
	created(){
		
	},
	methods:{
		
	}
	
}).mount("#projects-holder-container")



Vue.createApp({
	data() {
		return {
			todoList: []
		}
	},
	created() {
		fetch(fetchListUrl)
			.then(response => response.json())
			.then(response => this.todoList = response)
			//.then(response => alert(JSON.stringify(response)))
		//alert("hello")
	}
}).mount("#vue-test-area")









//const {createApp} = Vue

const red = Vue.createApp({
	data() {
		return {
			todoList: [],
			dt: 'test'
		}
	},
	created() {
		fetch(fetchListUrlREST)
			.then(response => response.json())
			.then(data => this.todoList = data)
	}
}).mount("#app")

