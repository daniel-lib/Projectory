function getLoginInfo(){
	const proof = getElementById("login_indicator");
	const user_id = getELementById("current_user_id");
	
	
	fetch('/user/check-login-info?UserId=' + userId + '&proof=' + proof)
		.then(response => response.json())
		.then(data => {
			alert(data.firstName);
		})	
	
}