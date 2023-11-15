const projectCount = '/project/status/count';


Vue.createApp({
	data() {
		return {
			projectStatus: [],
			numericValueData: [],
			projectStatusLabelData: [],
			chartVue: new Chart(document.getElementById('projects-chart-vue-canvas'), {
				type: 'pie',
				data: {
					labels: this.projectStatusLabelData,
					datasets: [{
						label: '# of projects',
						data: this.numericValueData,
						borderWidth: 1,
						backgroundColor: ['#3fb97c', '#3487ed', '#f26400', '#f5a900'],
					}]
				},
				options: {

					title: {
						display: true,
						text: 'Project status count'
					}
				}
			}),


		}
	},
	created() {
		this.getProjectStatusCount();
	},
	onMounted() {
		new Chart(document.getElementById('projects-chart-vue-canvas'), {
			type: 'pie',
			data: {
				labels: this.projectStatusLabelData,
				datasets: [{
					label: '# of projects',
					data: this.numericValueData,
					borderWidth: 1,
					backgroundColor: ['#3fb97c', '#3487ed', '#f26400', '#f5a900'],
				}]
			},
			options: {

				title: {
					display: true,
					text: 'Project status count'
				}
			}
		})
	},
	methods: {
		getProjectStatusCount() {
			fetch(projectCount)
				.then(response => response.json())
				.then(data => {
					this.projectStatus = data

					for (let i = 0; i < data.length; i++) {
						this.numericValueData[i] = data[i].labelCount;
						this.projectStatusLabelData[i] = data[i].statusLabel + "(" + data[i].labelCount + ")";
					}
				})
		}

	}
}).mount("#projects-chart-vue");






