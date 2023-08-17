var structuredData = toHtml(ProjectStatusCount);
var jsonArray = JSON.parse(structuredData);
var arrayLength = jsonArray.length;
var numericValueData = [];
var projectStatusLabelData = [];
for (let i = 0; i < arrayLength; i++) {
	numericValueData[i] = jsonArray[i].labelCount;
	projectStatusLabelData[i] = jsonArray[i].statusLabel + "(" + jsonArray[i].labelCount + ")";
}

const projectsChart = document.getElementById('projects-chart');
new Chart(projectsChart, {
	type: 'pie',
	data: {
		labels: projectStatusLabelData,
		datasets: [{
			label: '# of projects',
			data: numericValueData,
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
});

function toHtml(data) {
	var txt = document.createElement("textarea");
	txt.innerHTML = data;
	return txt.value;
}