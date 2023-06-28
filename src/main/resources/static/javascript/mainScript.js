	const signin = document.getElementById("signin-btn");
	signin.addEventListener("click", showLoginForm);

	function showLoginForm() {
		document.getElementById("login-form").style = "height: 500px";
	}
	



/*<script th:fragment="notification-board-script">*/
	/*function showActionNotification() {*/
		function showMainNotification(){
		let notificationBoard = document.getElementById("item-action-notification");
		let style2 = ""
		if (notificationBoard.innerHTML !== "") {
			//let color = "rgb(246, 97, 81)";
			let color = "black";
			let bgColor = "rgb(255, 129, 130, 0.4)";
			if (notificationBoard.innerHTML === "pass") {
				notificationBoard.innerHTML = "Welcome!";
				bgColor = "rgb(51, 209, 122, 0.4)";
			}
			else if (notificationBoard.innerHTML === "err") {
				displaySigninForm("login")
				notificationBoard.innerHTML = "Incorrect username or password";
				style2 = "border-top-right-radius: 0.375rem !important;border-top-left-radius: 0.375rem !important;"
				document.getElementById('username-input').select();
			}
			let style1 = "transition: all 1s ease-out;display: flex !important;background-color: " + bgColor + " !important;";
			
			notificationBoard.style =  style1+style2;
			setTimeout(hideNotificationBoard, 1500);
		}
	}
	function hideNotificationBoard() {
		let notificationBoard = document.getElementById("item-action-notification");
		//notificationBoard.style="display: flex !important;height:300px !important;";
		
	
	let style1="color:yellow;display: none !important;z-index: 1000;transition: all 1s ease-out;border:1px solid blue !important"
	let style2="transition-delay: 1s;";
		notificationBoard.style = style1+style2;	
	
		//notificationBoard.style = "color:yellow;display: flex !important;";				
	}
