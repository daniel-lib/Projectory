
function getLoginInfo(){	
	const proof = document.getElementById("login_indicator").textContent;
	const user_id = document.getElementById("current_user_id").textContent;
	  //alert("alllo");
	fetch('/user/check-login-info?userId=' + user_id + '&proof=' + proof)
		.then(response => response)
		.then(data => {
			const user = JSON.parse(data);
			alert(user.firstName);
			alert("ffff");
		})	
		
		
		


		
		
	fetch('/project/add-project-task?title=+' + title[0].value + '&description=' + description[0].value + '&projId=' + projId + '&status=' + status[0].value)
		.then(response => response.text())
		.then(data => {
			let msg, notificationType;
			if (data == "1") {
				msg = "Project Task Added!";
				notificationType = "success";
			}
			else {
				msg = "Couldn't add project task. It's probably your fault.";
				notificationType = "error";
			}
			alert(msg);
			toggleNotification(notificationType, msg);
		})
	
}