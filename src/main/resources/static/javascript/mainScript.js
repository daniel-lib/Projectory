

//display go to the top button
toggleGoTo();
// Listen for click events on body
window.addEventListener('scroll', function(event) {
	toggleGoTo();
});
function toggleGoTo() {
	const goToTopBtn = document.getElementsByClassName("go-to-top-btn");
	if (window.pageYOffset > 500)
		goToTopBtn[0].classList.add("go-to-top-btn-show");
	else
		goToTopBtn[0].classList.remove("go-to-top-btn-show");
}


//auth Notification
let eventPathsVariables = {
	"?auth_required?logout" : ["You have successfully signed out. See you soon.", "success"],
	"?auth_required" : ["Please, login to continue.", "error"],
	"?auth_required?error" : ["Incorrect credentials. Please try again.", "error"],
	"?auth_required?error" : ["Incorrect credentials. Please try again.", "error"],
	"?auth_successful" : ["Welcome back!", "success"],
	"?reg=success" : ["Registration was successful. Login with you credentials to continue.", "success"],
	"?reg=err" : ["Registration unsuccessful. Please try again.", "error"]	
}
displayEventNotification(window.location.search);
function displayEventNotification(currentPathVar){
		
	if(currentPathVar in eventPathsVariables){
		toggleNotification(eventPathsVariables[currentPathVar][1], eventPathsVariables[currentPathVar][0])
	}
	/*else{
		toggleNotification("error", "...")		
	}*/
	if(currentPathVar == "?auth_required?error" || currentPathVar == "?auth_required"){
	displaySigninForm('login')
	}
	
	if(currentPathVar == "?reg=err" || currentPathVar == "?reg=success"){
	displaySigninForm('signup')
	}
}



///error
/*const signin = document.getElementById("signin-btn");
signin.addEventListener("click", showLoginForm);

function showLoginForm() {
	document.getElementById("login-form").style = "height: 500px";
}
*/


/*function toggleAuthForm() {*/

function displaySigninForm(type) {
	const formContainer = document.getElementById("login-signup-form");
	const signInForm = document.getElementById("sign-in-form");
	const signUpForm = document.getElementById("sign-up-form");
	formContainer.style.display = "block";
	formContainer.style.visibility = "visible";
	formContainer.style.opacity = "1";
	if (type == "login") {

		//document.querySelector("#login-signup-form").style.height = "600px";		
		//form.style.display = "block";
		//form.style.height = "auto";	

		//signUpForm.style.opacity = "0";
		signUpForm.style.display = "none";

		signInForm.style.display = "block";
		signInForm.style.opacity = "1";
		document.getElementById("username-input").setAttribute("autofocus");
	}
	else if (type == "signup") {
		//signInForm.style.opacity = "0";
		signInForm.style.display = "none";

		signUpForm.style.display = "block";
		signUpForm.style.opacity = "1";
	}


}
function closeSigninForm() {
	const formContainer = document.getElementById("login-signup-form");
	formContainer.style.opacity = "0";
	formContainer.style.visibility = "collapse";
	formContainer.style.display = "none";
}



/*<script th:fragment="notification-board-script">*/
/*function showActionNotification() {*/
function showMainNotification() {
	let notificationBoard = document.getElementById("item-action-notification");
	let style2 = ""
	if (notificationBoard.textContent !== "") {
		//let color = "rgb(246, 97, 81)";
		let color = "black";
		let bgColor = "rgb(255, 129, 130, 0.4)";
		if (notificationBoard.textContent === "pass") {
			notificationBoard.textContent = "Welcome!";
			bgColor = "rgb(51, 209, 122, 0.4)";
		}
		else if (notificationBoard.textContent === "err") {
			displaySigninForm("login")
			notificationBoard.textContent = "Incorrect username or password";
			style2 = "border-top-right-radius: 0.375rem !important;border-top-left-radius: 0.375rem !important;"
			document.getElementById('username-input').select();
		}
		let style1 = "transition: all 1s ease-out;display: flex !important;background-color: " + bgColor + " !important;";

		notificationBoard.style = style1 + style2;
		setTimeout(hideNotificationBoard, 1500);
	}
}
function hideNotificationBoard() {
	let notificationBoard = document.getElementById("item-action-notification");
	//notificationBoard.style="display: flex !important;height:300px !important;";


	let style1 = "color:yellow;display: none !important;z-index: 1000;transition: all 1s ease-out;border:1px solid blue !important"
	let style2 = "transition-delay: 1s;";
	notificationBoard.style = style1 + style2;

	//notificationBoard.style = "color:yellow;display: flex !important;";				
}



//Notication bar popup(NEW) script
function moveNotificationToTopLayer() {
	const notification = document.querySelector('.notification');
	if (notification && notification.parentElement !== document.body) {
		document.body.appendChild(notification);
	}
}

moveNotificationToTopLayer();

function toggleNotification(notificationType, msgToUser) {
	moveNotificationToTopLayer();
	const notification = document.querySelector('.notification');
	const content = document.getElementById("notification-bar-content");
	const icon = document.getElementById("notification-icon");
	if (notificationType == undefined) {
		content.textContent = content.textContent;
		//clearTimeout(timeoutID);
	}
	else {
		content.textContent = msgToUser;
		//clearTimeout(timeoutID);
		if (notificationType === "success") {
			notification.classList.remove('notification-error');
			notification.classList.add('notification-success');
			icon.classList.replace('fa-triangle-exclamation', 'fa-circle-check');
		}
		else {
			notification.classList.remove('notification-success');
			notification.classList.add('notification-error');
			icon.classList.replace('fa-circle-check', 'fa-triangle-exclamation');
		}
	}

	notification.classList.remove('none');
	notification.classList.remove('hide');
	/*notification.classList.toggle('hide');*/

	//trigger automatic notification bar exit	
	timeoutID = window.setTimeout(hideNotificationBar, 3000);

}
function hideNotificationBar() {
	const notification = document.querySelector('.notification');
	notification.classList.add('hide');
}

document.addEventListener('htmx:afterRequest', function(event) {
	finishRequestButton(event.detail.elt);
	const triggerHeader = event.detail.xhr.getResponseHeader('HX-Trigger');
	if (triggerHeader && triggerHeader.charAt(0) === '{') {
		const triggers = JSON.parse(triggerHeader);
		if (triggers.notify) {
			toggleNotification(triggers.notify.type, triggers.notify.message);
		}
	}
});

let pendingAppConfirmationAction = null;

function getAppConfirmationDialog() {
	const dialog = document.getElementById('app-confirmation-dialog');
	if (!(dialog instanceof HTMLDialogElement)) {
		throw new Error('The shared confirmation dialog is missing from this page.');
	}
	return dialog;
}

function openAppConfirmation(options) {
	if (!options || typeof options.onConfirm !== 'function') {
		throw new TypeError('A confirmation action is required.');
	}

	const dialog = getAppConfirmationDialog();
	if (dialog.open) {
		closeAppConfirmation();
	}

	document.getElementById('app-confirmation-title').textContent = options.title || 'Confirm action';
	document.getElementById('app-confirmation-message').textContent = options.message || '';
	document.getElementById('app-confirmation-accept').textContent = options.confirmLabel || 'Continue';
	pendingAppConfirmationAction = options.onConfirm;
	dialog.showModal();
}

function closeAppConfirmation() {
	pendingAppConfirmationAction = null;
	getAppConfirmationDialog().close();
}

function acceptAppConfirmation() {
	const action = pendingAppConfirmationAction;
	pendingAppConfirmationAction = null;
	getAppConfirmationDialog().close();
	if (action) {
		action();
	}
}

function openSignOutConfirmation() {
	openAppConfirmation({
		title: 'Sign out?',
		message: 'Are you sure you want to sign out?',
		confirmLabel: 'Sign out',
		onConfirm: function() {
			const form = document.getElementById('sign-out-form');
			if (!(form instanceof HTMLFormElement)) {
				throw new Error('The sign-out form is missing from this page.');
			}
			form.requestSubmit();
		}
	});
}

const appConfirmationDialog = document.getElementById('app-confirmation-dialog');
if (appConfirmationDialog) {
	appConfirmationDialog.addEventListener('cancel', function() {
		pendingAppConfirmationAction = null;
	});
}

// Replace HTMX's native confirm prompt with the shared in-app dialog.
document.addEventListener('htmx:confirm', function(event) {
	if (!event.detail.question) {
		return;
	}

	event.preventDefault();
	if (typeof event.detail.issueRequest !== 'function') {
		throw new Error('HTMX confirmation is missing its request callback.');
	}

	const source = event.target instanceof HTMLElement ? event.target : null;
	openAppConfirmation({
		title: source && source.dataset.confirmTitle || 'Confirm action',
		message: event.detail.question,
		confirmLabel: source && source.dataset.confirmLabel || 'Continue',
		onConfirm: function() {
			event.detail.issueRequest(true);
		}
	});
});

const requestButtonStates = new WeakMap();
const requestButtonsBySource = new WeakMap();

function getRequestButton(event) {
	const source = event.detail.elt;
	if (source instanceof HTMLButtonElement) {
		return source;
	}

	const submitter = event.detail.triggeringEvent && event.detail.triggeringEvent.submitter;
	if (submitter instanceof HTMLButtonElement) {
		return submitter;
	}

	if (source instanceof HTMLFormElement) {
		const buttons = source.querySelectorAll('button[type="submit"], button:not([type])');
		if (buttons.length === 1) {
			return buttons[0];
		}
	}
	return null;
}

function showRequestButtonLoading(button, source) {
	if (!button) {
		return;
	}

	if (source) {
		requestButtonsBySource.set(source, button);
	}
	if (requestButtonStates.has(button)) {
		return;
	}

	const spinner = document.createElement('span');
	spinner.className = 'request-button-spinner';
	spinner.setAttribute('aria-hidden', 'true');
	requestButtonStates.set(button, {
		spinner: spinner,
		wasDisabled: button.disabled,
		ariaBusy: button.getAttribute('aria-busy')
	});
	button.prepend(spinner);
	button.disabled = true;
	button.setAttribute('aria-busy', 'true');
}

function startRequestButton(event) {
	showRequestButtonLoading(getRequestButton(event), event.detail.elt);
}

document.addEventListener('submit', function(event) {
	if (event.defaultPrevented || !(event.target instanceof HTMLFormElement)) {
		return;
	}
	const form = event.target;
	const submitter = event.submitter;
	const htmxRequest = form.matches('[hx-get], [hx-post], [hx-put], [hx-delete], [hx-patch]')
		|| (submitter instanceof HTMLElement
			&& submitter.matches('[hx-get], [hx-post], [hx-put], [hx-delete], [hx-patch]'));
	if (!htmxRequest && submitter instanceof HTMLButtonElement) {
		showRequestButtonLoading(submitter);
	}
});

function finishRequestButton(source) {
	const button = requestButtonsBySource.get(source)
		|| (source instanceof HTMLButtonElement ? source : null);
	if (!button) {
		return;
	}
	const state = requestButtonStates.get(button);
	if (!state) {
		return;
	}

	state.spinner.remove();
	button.disabled = state.wasDisabled;
	if (state.ariaBusy === null) {
		button.removeAttribute('aria-busy');
	} else {
		button.setAttribute('aria-busy', state.ariaBusy);
	}
	requestButtonStates.delete(button);
	requestButtonsBySource.delete(source);
}

document.addEventListener('htmx:beforeRequest', startRequestButton);
document.addEventListener('htmx:sendError', function(event) {
	finishRequestButton(event.detail.elt);
});
document.addEventListener('htmx:timeout', function(event) {
	finishRequestButton(event.detail.elt);
});

document.addEventListener('htmx:configRequest', function(event) {
	const csrfToken = document.querySelector('meta[name="_csrf"]');
	const csrfHeader = document.querySelector('meta[name="_csrf_header"]');
	if (csrfToken && csrfHeader) {
		event.detail.headers[csrfHeader.content] = csrfToken.content;
	}
});

//coockie privacy notice
(function() {
	const cookieNoticeBoard = document.getElementById("cookie-notice");
	const closeNoticeButton = document.getElementById("cookie-accept-btn");
	
	if (localStorage.getItem('cookie-status') === null) {
		window.setTimeout(()=>cookieNoticeBoard.classList.add("show"),1000);
		closeNoticeButton.addEventListener("click", ()=>{
		cookieNoticeBoard.classList.remove("show");
		localStorage.setItem('cookie-status', "policy-accepted");
	});
	} 
})();



/*document.addEventListener("click", (event) => {
	const isClickInsideNotification = notification.contains(event.target);
	const isButtonClicked = button.contains(event.target);

	if (!isClickInsideNotification && !isButtonClicked) {
		notification.classList.add('hide');
	}
});*/
