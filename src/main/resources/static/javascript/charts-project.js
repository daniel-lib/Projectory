const projectCount = '/project/status/count';
numericValueData = [];
projectStatusLabelData = [];
fetch(projectCount)
	.then(response => response.json())
	.then(data => {
		for (let i = 0; i < data.length; i++) {
			numericValueData[i] = data[i].labelCount;
			projectStatusLabelData[i] = data[i].statusLabel + "(" + data[i].labelCount + ")";
		}



		const projectsChart = document.getElementById('projects-chart');
		const toggle = document.getElementById("theme");
		Chart.defaults.color = 'goldenrod'; //change color of labels
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

	})


/*var structuredData = toHtml(ProjectStatusCount);
var jsonArray = JSON.parse(structuredData);
var arrayLength = jsonArray.length;
var numericValueData = [];
var projectStatusLabelData = [];
for (let i = 0; i < arrayLength; i++) {
	numericValueData[i] = jsonArray[i].labelCount;
	projectStatusLabelData[i] = jsonArray[i].statusLabel + "(" + jsonArray[i].labelCount + ")";
}*/





function toHtml(data) {
	var txt = document.createElement("textarea");
	txt.innerHTML = data;
	return txt.value;
}






// <block:actions:2>
const actions = [
	{
		name: 'pointStyle: circle (default)',
		handler: (chart) => {
			chart.data.datasets.forEach(dataset => {
				dataset.pointStyle = 'circle';
			});
			chart.update();
		}
	},
	{
		name: 'pointStyle: cross',
		handler: (chart) => {
			chart.data.datasets.forEach(dataset => {
				dataset.pointStyle = 'cross';
			});
			chart.update();
		}
	},
	{
		name: 'pointStyle: crossRot',
		handler: (chart) => {
			chart.data.datasets.forEach(dataset => {
				dataset.pointStyle = 'crossRot';
			});
			chart.update();
		}
	},
	{
		name: 'pointStyle: dash',
		handler: (chart) => {
			chart.data.datasets.forEach(dataset => {
				dataset.pointStyle = 'dash';
			});
			chart.update();
		}
	},
	{
		name: 'pointStyle: line',
		handler: (chart) => {
			chart.data.datasets.forEach(dataset => {
				dataset.pointStyle = 'line';
			});
			chart.update();
		}
	},
	{
		name: 'pointStyle: rect',
		handler: (chart) => {
			chart.data.datasets.forEach(dataset => {
				dataset.pointStyle = 'rect';
			});
			chart.update();
		}
	},
	{
		name: 'pointStyle: rectRounded',
		handler: (chart) => {
			chart.data.datasets.forEach(dataset => {
				dataset.pointStyle = 'rectRounded';
			});
			chart.update();
		}
	},
	{
		name: 'pointStyle: rectRot',
		handler: (chart) => {
			chart.data.datasets.forEach(dataset => {
				dataset.pointStyle = 'rectRot';
			});
			chart.update();
		}
	},
	{
		name: 'pointStyle: star',
		handler: (chart) => {
			chart.data.datasets.forEach(dataset => {
				dataset.pointStyle = 'star';
			});
			chart.update();
		}
	},
	{
		name: 'pointStyle: triangle',
		handler: (chart) => {
			chart.data.datasets.forEach(dataset => {
				dataset.pointStyle = 'triangle';
			});
			chart.update();
		}
	},
	{
		name: 'pointStyle: false',
		handler: (chart) => {
			chart.data.datasets.forEach(dataset => {
				dataset.pointStyle = false;
			});
			chart.update();
		}
	}
];
// </block:actions>

// <block:setup:1>
const data = {
	labels: ['Day 1', 'Day 2', 'Day 3', 'Day 4', 'Day 5', 'Day 6', 'Day 7'],
	datasets: [
		{
			label: 'Completed Project Tasks',
			//data: Utils.numbers({count: 6, min: -100, max: 100}),
			data: ['4', '3', '6', '2', '9', '1', '5'],
			//borderColor: Utils.CHART_COLORS.red,
			borderColor: 'rgb(255, 99, 132)',
			//backgroundColor: Utils.transparentize(Utils.CHART_COLORS.red, 0.5),
			backgroundColor: 'rgba(255, 99, 132, 0.3)',
			pointStyle: 'circle',
			pointRadius: 10,
			pointHoverRadius: 15,
			color: 'rgb(255, 99, 132)'
		}
	]
};




// </block:setup>

// <block:config:0>
const config = {
	type: 'line',
	data: data,
	options: {
		responsive: true,
		plugins: {
			title: {
				display: true,
				text: (ctx) => 'Point Style: ' + ctx.chart.data.datasets[0].pointStyle,
			}
		}
	}
};

//productivity
const projectsProductivityChart = document.getElementById('productivity-chart');
new Chart(projectsProductivityChart, {
	type: 'line',
	data: data,
	config: config,
	actions: actions
});



// </block:config>

/*module.exports = {
  actions: actions,
  config: config,
};
*/
