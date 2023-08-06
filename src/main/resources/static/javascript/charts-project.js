 const projectsChart = document.getElementById('projects-chart');
 new Chart(projectsChart, {	  
    type: 'pie',
    data: {
      labels: ['Not started', 'In progress', 'Completed', 'On Hold'],
      datasets: [{
        label: '# of projects',
        data: [ 3, 5, 2, 6],
        borderWidth: 1,
        backgroundColor: ['#f26400', '#3fb97c', '#3487ed', '#f5a900'],
      }]
    },
    options: {
      scales: {
        y: {
          beginAtZero: true
        }
      }
    }
  });