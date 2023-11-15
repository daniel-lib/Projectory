const searchUrl = "/search/all/";

Vue.createApp({
	
	data(){
		return{
			fetchedSearchableData: [],
			searchLoading: false,
		}
	},
	created(){
		
	},
	methods:{
		fetchSearchableData(keyword, event){
			//alert("search kw: "+keyword)
			if(keyword != ''){
				searchLoading = true;
			fetch(searchUrl+keyword)
				.then(response => response.json())
				.then(data => {
					this.fetchedSearchableData = data;
					searchLoading = false;
				})
			}
		},
		searchThroughData(){
			
		}
	}
}).mount("#search-bar");